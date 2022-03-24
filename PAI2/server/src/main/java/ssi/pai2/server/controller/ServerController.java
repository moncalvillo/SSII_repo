package ssi.pai2.server.controller;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssi.pai2.server.model.Message;
import ssi.pai2.server.service.ServerService;
import ssi.pai2.server.utils.LogLine;
import ssi.pai2.server.utils.LogType;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

    private String challenge = "challenge";
    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, String> requestVerification(HttpServletRequest req, @RequestBody Map<String, String> params)
            throws NoSuchAlgorithmException {
        Map<String, String> response = new HashMap<>();
        String mensaje = "";
        Message entity = new Message(params);
        if (!entity.isValid()) {
            String msg = "Bad parameters";
            String bodyParams = "Body: { origen = " + entity.getOrigen() + " , destino = "
                    + entity.getDestino() + " , cantidad = " + entity.getCantidad() + " , nonce = " + entity.getNonce()
                    + " , mac = " + entity.getMac() + " }";
            LogLine log = new LogLine(msg, LogType.BAD_PARAMETERS, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();
            mensaje = "REQUEST_ERROR";
        } else if (serverService.verify(entity, challenge)) {
            String msg = "Transaction verified and succesful";
            String bodyParams = "Body: { origen = " + entity.getOrigen() + " , destino = "
                    + entity.getDestino() + " , cantidad = " + entity.getCantidad() + " , nonce = " + entity.getNonce()
                    + " , mac = " + entity.getMac() + " }";
            LogLine log = new LogLine(msg, LogType.MESSAGE_OK, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();
            mensaje = "OK";

        } else {
            String msg = "INTEGRATION_ERROR";
            String bodyParams = "Body: { origen = " + entity.getOrigen() + " , destino = "
                    + entity.getDestino() + " , cantidad = " + entity.getCantidad() + " , nonce = " + entity.getNonce()
                    + " , mac = " + entity.getMac() + " }";
            LogLine log = new LogLine(msg, LogType.INTEGRATION_ERROR, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();

            mensaje = "INTEGRATION_ERROR";
            
        }
        response.put("mensaje", mensaje);
        response.put("mac", serverService.createMAC(mensaje + entity.getNonce(), challenge));

        return response;
    }

    @RequestMapping(value = "/proxy", method = RequestMethod.POST)
    public Map<String, String> proxyRequest(HttpServletRequest req, @RequestBody Map<String, String> params)
            throws NoSuchAlgorithmException {
        var input = params;

        String attackType = input.get("attackType");

        if (attackType == null) {
            attackType = "";
        }

        Map<String, String> output = new HashMap<>();

        if (attackType.equals("modifyDestiny")) {
            input.put("destino", "88887777");
        } else if (attackType.equals("modifyQuantity")) {
            input.put("cantidad", "10000");
        } else if (attackType.equals("modifyNonce")) {
            input.put("nonce", UUID.randomUUID().toString());
        } else if (attackType.equals("replyAttack")) {
            this.requestVerification(req, input);
        }

        output = this.requestVerification(req, input);

        return output;
    }

}
