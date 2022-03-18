package ssi.pai2.server.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssi.pai2.server.service.ServerService;



@RestController
@RequestMapping(value = "/server")
public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    private String challenge = "challenge";
    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String, String> requestVerification(HttpServletRequest req, HttpServletResponse resp) {
        
        return null;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map<String, String> test(Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> response = new HashMap<>();
        
        return response;
    }

    
}
