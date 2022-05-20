package ssi.pai5.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ssi.pai5.server.model.Certificado;
import ssi.pai5.server.model.Peticion;
import ssi.pai5.server.model.RatioVerificacion;
import ssi.pai5.server.model.Tendencia;
import ssi.pai5.server.model.TendenciaMensual;
import ssi.pai5.server.repository.CertificadoRepository;
import ssi.pai5.server.repository.PeticionRepository;

@Service
public class ServerService {

    @Autowired
    CertificadoRepository certificadoRepository;

    @Autowired
    PeticionRepository peticionRepository;

    public PublicKey getPublicKey(Long id) throws CertificateException, KeyStoreException {
        KeyStore keystore;
        try {
            File file = ResourceUtils.getFile("classpath:springboot.p12");
            FileInputStream is = new FileInputStream(file);
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "password".toCharArray());
            String alias = id.toString();

            Certificate cert = keystore.getCertificate(alias);

            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            return publicKey;

        } catch (KeyStoreException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Boolean verifyMessage(Map<String, String> params) throws NumberFormatException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PublicKey publicKey = this.getPublicKey(Long.valueOf(params.get("idEmpleado")));

        String mensaje = params.get("camas") + params.get("mesas") + params.get("sillas")
                + params.get("sillones") + params.get("idEmpleado") + params.get("nonce");
        Signature sg = Signature.getInstance("SHA256withRSA");
        sg.initVerify(publicKey);
        sg.update(mensaje.getBytes());

        Boolean verificacion = sg.verify(hexStringToByteArray(params.get("firma")));

        return verificacion;
    }

    public Peticion savePeticion(Peticion peticion) {
        return this.peticionRepository.save(peticion);
    }

    public Integer findNonce(String nonce) {
        return this.peticionRepository.findNonce(nonce);
    }

    public Integer countPeticionesEn4Horas(Date start, Date end) {
        return this.peticionRepository.countPeticionesEn4Horas(start, end);
    }

    private Map<Integer, Map<Integer, List<Peticion>>> groupByMonthAndYear(List<Peticion> peticiones) {
        Map<Integer, Map<Integer, List<Peticion>>> peticionesByYearAndMonth = new HashMap<>();

        for (Peticion peticion : peticiones) {
            Date date = peticion.getTimestamp();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);

            if (!peticionesByYearAndMonth.containsKey(year)) {
                peticionesByYearAndMonth.put(year, new HashMap<>());
            }

            Map<Integer, List<Peticion>> peticionesByMonth = peticionesByYearAndMonth.get(year);

            if (!peticionesByMonth.containsKey(month)) {
                peticionesByMonth.put(month, new ArrayList<>());
            }

            List<Peticion> peticionesAgrupadas = peticionesByMonth.get(month);
            peticionesAgrupadas.add(peticion);

        }

        return peticionesByYearAndMonth;
    }

    private List<RatioVerificacion> calculateRatioVerifications(
            Map<Integer, Map<Integer, List<Peticion>>> peticionesGroupedByYearAndMonth) {

        List<RatioVerificacion> ratioVerificaciones = new ArrayList<>();

        for (Entry<Integer, Map<Integer, List<Peticion>>> peticionesByYear : peticionesGroupedByYearAndMonth
                .entrySet()) {
            Integer year = peticionesByYear.getKey();

            for (Entry<Integer, List<Peticion>> peticionesByMonth : peticionesByYear.getValue().entrySet()) {
                Integer month = peticionesByMonth.getKey();
                List<Peticion> peticiones = peticionesByMonth.getValue();

                Double verificacionesPositivas = (double) peticiones.stream()
                        .filter(peticion -> peticion.getVerificacion())
                        .count();
                Double ratio = verificacionesPositivas / peticiones.size();

                RatioVerificacion ratioVerificacion = new RatioVerificacion(year, month, ratio);
                ratioVerificaciones.add(ratioVerificacion);

            }
        }
        return ratioVerificaciones;

    }

    private List<TendenciaMensual> calculateTendenciaMensual(List<RatioVerificacion> ratioVerificaciones) {
        List<TendenciaMensual> tendenciaMensuales = new ArrayList<>();
        ratioVerificaciones.sort((x, y) -> {
            Integer xYear = x.getYear();
            Integer yYear = y.getYear();

            int yearComp = xYear.compareTo(yYear);

            if (yearComp != 0) {
                return yearComp;
            }

            Integer xMonth = x.getMonth();
            Integer yMonth = y.getMonth();

            return xMonth.compareTo(yMonth);

        });
        for (int i = 0; i < ratioVerificaciones.size(); i++) {
            RatioVerificacion ratioVerificacion = ratioVerificaciones.get(i);
            Tendencia tendencia;
            if (i < 2) {
                tendencia = Tendencia.NULA;
            } else {
                Double p3 = ratioVerificaciones.get(i).getRatio();
                Double p2 = ratioVerificaciones.get(i - 1).getRatio();
                Double p1 = ratioVerificaciones.get(i - 2).getRatio();
                if ((p3 > p1 && p3 > p2) || (p3 < p1 && p3 == p2) || (p3.equals(p1) && p3 > p2)) {
                    tendencia = Tendencia.POSITIVA;
                } else if (p3 < p1 || p3 < p2) {
                    tendencia = Tendencia.NEGATIVA;
                } else {
                    tendencia = Tendencia.NULA;
                }

            }

            TendenciaMensual tendenciaMensual = new TendenciaMensual(ratioVerificacion, tendencia);
            tendenciaMensuales.add(tendenciaMensual);
        }

        return tendenciaMensuales;
    }

    public void saveTendenciaMensualToFile() {
        var peticiones = this.peticionRepository.findAll();
        var peticionesGroupedByYearAndMonth = groupByMonthAndYear(peticiones);
        var ratioVerificacions = calculateRatioVerifications(peticionesGroupedByYearAndMonth);
        var tendenciaMensuals = calculateTendenciaMensual(ratioVerificacions);
        String lines = tendenciaMensuals.stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        Path output = Paths.get("informe.txt");
        try {
            Files.write(output, lines.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
