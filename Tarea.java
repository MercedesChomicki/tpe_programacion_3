package tpe;

public class Tarea {
    String id;
	String nombre;
	Integer tiempo;
	Boolean critica;
	Integer prioridad;

    public Tarea(){}

    public Tarea(String id, String nombre, Integer tiempo, Boolean critica, Integer prioridad){
        this.id = id;
    }

    public String getId(){
        return id;
    }

}
