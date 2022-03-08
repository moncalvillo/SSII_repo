package ssii.pai1.integrity.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssii.pai1.integrity.model.Item;
import ssii.pai1.integrity.service.ServerService;
import ssii.utils.LogLine;
import ssii.utils.LogType;

@RestController
@RequestMapping(value = "/server")
public class ServerController {
    


    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    private String challenge = "challenge";
    @Autowired
    private ServerService serverService;

    
    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String,String> requestVerification(HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> response = new HashMap<>();
        Item entity = new Item(req.getParameter("path"),req.getParameter("hashFile"));
        // Long token = Long.valueOf(req.getParameter("token"));
        response.put("hashFile", entity.getHashFile());
        if(!entity.isValid() || req.getParameter("token") == null || req.getParameter("token").isEmpty() || req.getParameter("token").isBlank()){
            String msg = "Bad parameters";
            String bodyParams = "Body: { path = " + req.getParameter("path") + " , hashFile = " + req.getParameter("hashFile") + " , token = " + req.getParameter("token") + " }";
            LogLine log = new LogLine(msg, LogType.BAD_PARAMETERS, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();
            response.put("error", "Bad parameters");
        }
        else if(serverService.verify(entity)){
            String msg = "Hash verified succesfully.";
            String bodyParams = "Body: { path = " + req.getParameter("path") + " , hashFile = " + req.getParameter("hashFile") + " , token = " + req.getParameter("token") + " }";
            LogLine log = new LogLine(msg, LogType.HASH_OK, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();
            try {
                response.put("mac", serverService.createMAC(entity.getHashFile(),req.getParameter("token"),challenge));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            String msg = "Hash not found in database. File could have been modified.";
            String bodyParams = "Body: { path = " + req.getParameter("path") + " , hashFile = " + req.getParameter("hashFile") + " , token = " + req.getParameter("token") + " }";
            LogLine log = new LogLine(msg, LogType.HASH_NOT_FOUND, req.getServletPath(), req.getMethod(), bodyParams);
            log.writeLog();
            response.put("error", "Hash file not found");
        }
        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map<String,String> test(Map<String,String> params, HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> response = new HashMap<>();
        try {
            response.put("mac", serverService.createMAC(params.get("hashFile"),params.get("token"),challenge));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public ResponseEntity test2(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("Hago algo.");
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
