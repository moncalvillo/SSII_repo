package ssii.pai1.integrity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ssii.pai1.integrity.model.Entity;
import ssii.pai1.integrity.service.ServerService;

@Controller
@RequestMapping(value = "/server")
public class ServerController {

    @Autowired
    private ServerService service;

    
    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Entity requestVerification(Entity entitiy, BindingResult result, HttpServletRequest req, HttpServletResponse resp){
        if(entitiy.isValid()){

        }
        return null;
    }
}
