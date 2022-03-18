package ssi.pai2.server.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssi.pai2.server.model.Item;
import ssi.pai2.server.repository.ServerRepository;


@Service
public class ServerService {


    private ServerRepository serverRepo;

    @Autowired
    public ServerService(ServerRepository serverRepository){
        this.serverRepo = serverRepository;
    }

    public boolean verify(Item entity) {
       return false;
    }

    public String createMAC(String hashFile, String token, String challenge) throws NoSuchAlgorithmException {
        String str = hashFile + token + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHex(encodedhash);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
