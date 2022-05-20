package ssi.pai5.server.model;

public enum Tendencia {
    POSITIVA("+"),
    NEGATIVA("-"),
    NULA("0");

    private final String name;

    private Tendencia(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns
        // false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
