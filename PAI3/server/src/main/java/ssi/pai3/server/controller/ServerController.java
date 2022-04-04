package ssi.pai3.server.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssi.pai3.server.model.Message;
import ssi.pai3.server.model.User;
import ssi.pai3.server.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, String> requestVerification(HttpServletRequest req, @RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        User user = null;
        try {
            user = new User(params.get("username"), params.get("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Message message = new Message(params.get("message"));

        if (serverService.verifyUser(user)) {
            Message messageSaved = serverService.saveMessage(message);
            response.put("code", "200");
            response.put("data", "Mensaje secreto almacenado correctamente");
            System.out.println("Message: " + messageSaved);
        } else {
            response.put("code", "401");
            response.put("data", "Las credenciales no son correctas. El mensaje no se ha almacenado");
        }
        return response;
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public Map<String, String> createUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        
        System.out.println(user);
        if (!serverService.userExists(user)) {
            
            serverService.createUser(user);
            response.put("code", "200");
            response.put("data", "Usuario registrado.");
            System.out.println("User: " + user);
        } else {
            response.put("code", "403");
            response.put("data", "El usuario ya existe en la base de datos");
        }
        return response;
    }

}
