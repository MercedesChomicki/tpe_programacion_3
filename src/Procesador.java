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

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getRefrigerado() {
        return refrigerado;
    }

    public void setRefrigerado(Boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCantTCriticas() {
        return cantTCriticas;
    }

    public void setCantTCriticas(int cantTCriticas) {
        this.cantTCriticas = cantTCriticas;
    }

    public void setTiempoTotal(int tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
