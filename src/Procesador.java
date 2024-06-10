package tpe;

public class Procesador {
    private String id;
    private String codigo;
    private Boolean refrigerado;
    private int anio;
    private int cantTCriticas;
    private int tiempoTotal;

    public Procesador(String id, String codigo, Boolean refrigerado, int anio) {
        this.id = id;
        this.codigo = codigo;
        this.refrigerado = refrigerado;
        this.anio = anio;
        cantTCriticas = 0;
        tiempoTotal = 0;
    }

    public String getId(){
        return id;
    }

    public Boolean esRefrigerado() {
        return refrigerado;
    }

    public int cantTCriticas() {
        return cantTCriticas;
    }

    public void incrementarTCriticas(){
        this.cantTCriticas++;
    }

    public void decrementarTCriticas(){
        this.cantTCriticas--;
    }

    public int getTiempoTotal(){
        return tiempoTotal;
    }

    public void incrementarTiempoTotal(int tiempoTarea){
        this.tiempoTotal += tiempoTarea;
    }

    public void decrementarTiempoTotal(int tiempoTarea){
        this.tiempoTotal -= tiempoTarea;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
