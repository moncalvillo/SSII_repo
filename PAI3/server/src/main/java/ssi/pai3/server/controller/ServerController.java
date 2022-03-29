package ssi.pai3.server.controller;

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

import ssi.pai3.server.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, String> requestVerification(HttpServletRequest req, @RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        String username = params.get("username");
        String password = params.get("password");
        String message = params.get("message");

        if (username.equals("admin") && password.equals("admin")) {
            response.put("code", "200");
            response.put("data", "Mensaje secreto almacenado correctamente");
            System.out.println("Message: " + message);
        } else {
            response.put("code", "401");
            response.put("data", "Las credenciales no son correctas, el mensaje no se ha almacenado");
        }
        return response;
    }

}
