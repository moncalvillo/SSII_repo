package ssii.pai1.integrity.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssii.pai1.integrity.model.Item;
import ssii.pai1.integrity.repository.ServerRepository;

@Service
public class ServerService {
    

    @Autowired
    private ServerRepository serverRepo;

    public boolean verify(Item entity){
        Item foundEntity = serverRepo.findById(entity.getId()).get();
        if(foundEntity.getHashFile().equals(entity.getHashFile())){
            return true;
        }else{
            return false;
        }
    }

    public String createMAC(String hashFile, String token, String challenge) throws NoSuchAlgorithmException {
        String str = hashFile + token + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return encodedhash.toString();
    }
}
