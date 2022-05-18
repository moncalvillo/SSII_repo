package ssi.pai5.server.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssi.pai5.server.model.Certificado;
import ssi.pai5.server.model.Peticion;
import ssi.pai5.server.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, Object> requestVerification(HttpServletRequest req, @RequestBody Map<String, String> params)
            throws InvalidKeyException, InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException {

        File file = ResourceUtils.getFile("classpath:springboot.p12");
        FileInputStream is = new FileInputStream(file);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "password".toCharArray());

        String alias = "springboot";

        Key key = keystore.getKey(alias, "password".toCharArray());
        String encoded = ServerService.toHex(key.getEncoded());
        System.out.println(encoded);

        Map<String, Object> response = new HashMap<>();
        List<String> arrayErrors = new ArrayList<>();
        response.put("errors", arrayErrors);

        Boolean verificacion = false;
        Integer camas = Integer.valueOf(params.get("camas"));
        Integer mesas = Integer.valueOf(params.get("mesas"));
        Integer sillas = Integer.valueOf(params.get("sillas"));
        Integer sillones = Integer.valueOf(params.get("sillones"));

        Peticion peticion = new Peticion(camas, mesas, sillas, sillones, new Date(), verificacion, params.get("nonce"));

        // Poner límite de 3 peticiones en 4 horas
        Integer nPeticiones = this.serverService.countPeticionesEn4Horas(new Date(),
                new Date(System.currentTimeMillis() - (3600 * 1000 * 4)));
        if (nPeticiones > 4) {
            arrayErrors.add("Limite de peticiones en 4 horas alcanzado");
        } else {
            if (params.containsKey("camas") && params.containsKey("mesas") && params.containsKey("sillas") &&
                    params.containsKey("sillones") && params.containsKey("idEmpleado") && params.containsKey("firma")
                    && params.containsKey("nonce")) {

                // Validación de mensaje (entre 0 y 300)
                if (camas < 0 || camas > 300 || mesas < 0 || mesas > 300 || sillas < 0 || sillas > 300 || sillones < 0
                        || sillones > 300) {
                    arrayErrors.add("Los campos debes estar entre 0 y 300");
                }

                // Comprobar nonce
                if (this.serverService.findNonce(params.get("nonce")) > 0) {
                    arrayErrors.add("Nonce repetido");
                }

                // Con el id coger el certificado
                Certificado certificado = this.serverService.getCertificado(Long.valueOf(params.get("idEmpleado")));
                if (certificado != null) {
                    String clavePublica = certificado.getClavePublica();
                    byte[] publicBytes = Base64.decodeBase64(clavePublica);
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    PublicKey pubKey = keyFactory.generatePublic(keySpec);

                    String mensaje = params.get("camas") + params.get("mesas") + params.get("sillas")
                            + params.get("sillones") + params.get("idEmpleado") + params.get("nonce");
                    Signature sg = Signature.getInstance("SHA256withRSA");
                    sg.initVerify(pubKey);
                    sg.update(mensaje.getBytes());
                    verificacion = sg.verify(params.get("firma").getBytes());

                    if (!verificacion) {
                        arrayErrors.add("La firma no es correcta");
                    }

                    response.put("ok", verificacion);

                } else {
                    arrayErrors.add("No existe empleado con ID: " + params.get("idEmpleado"));
                }

            } else {
                arrayErrors.add("Entrada invalida");
            }
            this.serverService.savePeticion(peticion);
        }

        return response;
    }

}
