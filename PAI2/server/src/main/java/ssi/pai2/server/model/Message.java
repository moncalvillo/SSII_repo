package ssi.pai2.server.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Transient
    private String origen;
    @Transient
    private String destino;
    @Transient
    private String cantidad;
    @Transient
    private String mac;


    @Column
    private String nonce;

    public Message(Map<String, String> params) {
        this.origen = params.get("origen");
        this.destino = params.get("destino");
        this.cantidad= params.get("cantidad");
        this.nonce = params.get("nonce");
        this.mac =params.get("mac");
    }

    public Message(){

    }



    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }



    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isValid() {
        boolean b = false;
        b =  !this.cantidad.isBlank()  && !this.nonce.isBlank() && this.nonce != null;
        b = b && !this.mac.isBlank()  && this.mac != null;
        b = b && this.destino.toString().length() == 8 && this.origen.toString().length() == 8 && !this.cantidad.isBlank();
        return b;
    }

    

}
