package ssi.pai5.server.controller;

import java.security.PublicKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssi.pai5.server.model.Peticion;
import ssi.pai5.server.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, Object> requestVerification(HttpServletRequest req, @RequestBody Map<String, String> params) {

        Map<String, Object> response = new HashMap<>();
        List<String> arrayErrors = new ArrayList<>();
        response.put("errors", arrayErrors);

        try {
            Integer camas = Integer.valueOf(params.get("camas"));
            Integer mesas = Integer.valueOf(params.get("mesas"));
            Integer sillas = Integer.valueOf(params.get("sillas"));
            Integer sillones = Integer.valueOf(params.get("sillones"));
            Boolean verificacion = false;

            // Poner límite de 3 peticiones en 4 horas
            Integer nPeticiones = this.serverService.countPeticionesEn4Horas(new Date(),
                    new Date(System.currentTimeMillis() - (3600 * 1000 * 4)));
            if (nPeticiones > 3) {
                arrayErrors.add("Limite de peticiones en 4 horas alcanzado");
            } else {
                if (params.containsKey("camas") && params.containsKey("mesas") && params.containsKey("sillas") &&
                        params.containsKey("sillones") && params.containsKey("idEmpleado")
                        && params.containsKey("firma")
                        && params.containsKey("nonce")) {

                    // Validación de mensaje (entre 0 y 300)
                    if (camas < 0 || camas > 300 || mesas < 0 || mesas > 300 || sillas < 0 || sillas > 300
                            || sillones < 0
                            || sillones > 300) {
                        arrayErrors.add("Los campos debes estar entre 0 y 300");
                    }

                    // Comprobar nonce
                    if (this.serverService.findNonce(params.get("nonce")) > 0) {
                        arrayErrors.add("Nonce repetido");
                    }

                    // Con el id coger el certificado
                    try {
                        PublicKey publicKey = this.serverService.getPublicKey(Long.valueOf(params.get("idEmpleado")));
                        if (publicKey != null) {
                            verificacion = this.serverService.verifyMessage(params);

                            if (!verificacion) {
                                arrayErrors.add("La firma no es correcta");
                            }

                            response.put("ok", verificacion);

                        } else {
                            arrayErrors.add("No existe empleado con ID: " + params.get("idEmpleado"));
                        }
                    } catch (Exception e) {
                        arrayErrors.add("Ha habido un problema verificando la firma");

                    }

                } else {
                    arrayErrors.add("Entrada invalida");
                }

            }

            Peticion peticion = new Peticion(camas, mesas, sillas, sillones, new Date(), verificacion,
                    params.get("nonce"));

            this.serverService.savePeticion(peticion);
            serverService.saveTendenciaMensualToFile();
            System.out.println(peticion);
        } catch (Exception e) {
            arrayErrors.add("Ha ocurrido un error a la hora de verificar la peticion");
        }
        return response;
    }

}
