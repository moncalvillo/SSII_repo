package ssi.pai5.server.model;

public class TendenciaMensual extends RatioVerificacion {

    Tendencia tendencia;

    public TendenciaMensual(RatioVerificacion ratioVerificacion, Tendencia tendencia) {
        super(ratioVerificacion.getYear(), ratioVerificacion.getMonth() + 1, ratioVerificacion.getRatio());

        this.tendencia = tendencia;
    }

    public Tendencia getTendencia() {
        return tendencia;
    }

    public void setTendencia(Tendencia tendencia) {
        this.tendencia = tendencia;
    }

    @Override
    public String toString() {
        return String.format("%d/%d %.2f %s", super.getYear(), super.getMonth(), super.getRatio(),
                getTendencia().toString());
    }

}
