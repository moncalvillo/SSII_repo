package ssi.pai5.server.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String clavePublica;

    public Certificado() {

    }

    public Certificado(String clavePublica) {
        this.clavePublica = clavePublica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClavePublica() {
        return clavePublica;
    }

    public void setClavePublica(String clavePublica) {
        this.clavePublica = clavePublica;
    }

    @Override
    public String toString() {
        return "Certificado [clavePublica=" + clavePublica + ", id=" + id + "]";
    }
    
}
