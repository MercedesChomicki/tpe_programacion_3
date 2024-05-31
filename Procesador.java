package tpe;

public class Procesador {
    private String id;
    private String codigo;
    private Boolean refrigerado;
    private int anio;
    private int tCriticas;

    public Procesador(String id, String codigo, Boolean refrigerado, int anio) {
        this.id = id;
        this.codigo = codigo;
        this.refrigerado = refrigerado;
        this.anio = anio;
    }

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Boolean getRefrigerado() {
        return refrigerado;
    }

    public int getAnio() {
        return anio;
    }

    public int gettCriticas() {
        return tCriticas;
    }

    public void settCriticas(int tCriticas) {
        this.tCriticas = tCriticas;
    }
}
