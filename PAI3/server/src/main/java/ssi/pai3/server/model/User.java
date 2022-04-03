package ssi.pai3.server.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;
    @Column
    private String password;

    public User(){

    }

    public User(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        BigInteger bi = new BigInteger(1, encodedhash);
        this.password =  String.format("%0" + (encodedhash.length << 1) + "X", bi);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        BigInteger bi = new BigInteger(1, encodedhash);
        this.password =  String.format("%0" + (encodedhash.length << 1) + "X", bi);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", password=" + password + ", username=" + username + "]";
    }

    
    
}
