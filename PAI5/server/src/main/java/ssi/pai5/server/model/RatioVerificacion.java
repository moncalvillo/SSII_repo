package ssi.pai5.server.model;

public class RatioVerificacion {
    private Integer year;
    private Integer month;
    private Double ratio;

    public RatioVerificacion(Integer year, Integer month, Double ratio) {
        this.year = year;
        this.month = month;
        this.ratio = ratio;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

}
