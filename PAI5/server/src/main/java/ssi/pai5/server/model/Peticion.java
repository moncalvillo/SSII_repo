package ssi.pai5.server.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table
public class Peticion {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic
    private Long id;

    @Column
    private Integer camas;

    @Column
    private Integer mesas;

    @Column
    private Integer sillas;

    @Column
    private Integer sillones;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column
    private Boolean verificacion;

    @Column
    private String nonce;

    public Peticion() {

    }

    public Peticion(Integer camas, Integer mesas, Integer sillas, Integer sillones, Date timestamp,
            Boolean verificacion, String nonce) {
        this.camas = camas;
        this.mesas = mesas;
        this.sillas = sillas;
        this.sillones = sillones;
        this.timestamp = timestamp;
        this.verificacion = verificacion;
        this.nonce = nonce;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCamas() {
        return camas;
    }

    public void setCamas(Integer camas) {
        this.camas = camas;
    }

    public Integer getMesas() {
        return mesas;
    }

    public void setMesas(Integer mesas) {
        this.mesas = mesas;
    }

    public Integer getSillas() {
        return sillas;
    }

    public void setSillas(Integer sillas) {
        this.sillas = sillas;
    }

    public Integer getSillones() {
        return sillones;
    }

    public void setSillones(Integer sillones) {
        this.sillones = sillones;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }

    @Override
    public String toString() {
        return "Peticion [camas=" + camas + ", id=" + id + ", mesas=" + mesas + ", sillas=" + sillas + ", sillones="
                + sillones + ", timestamp=" + timestamp + ", verificacion=" + verificacion + "]";
    }

}
