package tpe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solucion {
    private String nombre;
    private Map<Procesador, List<Tarea>> resultado;
    private int tiempo;
    private int contador;

    public Solucion(String nombre, Map<Procesador, List<Tarea>> resultado) {
        this.nombre = nombre;
        this.resultado = new HashMap<>(resultado);
    }

    public Solucion(String nombre, Map<Procesador, List<Tarea>> resultado, int tiempo, int contador) {
        this.nombre = nombre;
        this.resultado = new HashMap<>(resultado);
        this.tiempo = tiempo;
        this.contador = contador;
    }

    public String getMejorAsignacion(){
        String mejorAsig = "";
        // Imprimir la asignación resultante
        for(Map.Entry<Procesador, List<Tarea>> entry : resultado.entrySet()) {
            Procesador procesador = entry.getKey();
            List<Tarea> tareasAsignadas = entry.getValue();

            mejorAsig += "\n Procesador: " + procesador.getId();

            if (tareasAsignadas.isEmpty()) {
                mejorAsig += "\n  Sin tareas asignadas.";
            } else {
                for (Tarea tarea : tareasAsignadas) {
                    mejorAsig += "\n  Tarea: " + tarea.getNombre();
                }
            }
        }
        return mejorAsig;
    }

    public int getTiempoMaximoOptimo(){
        return tiempo;
    }

    public int getContador(){
        return contador;
    }

    @Override
    public String toString() {
        if(!getMejorAsignacion().isEmpty()) {
            return nombre+"\n"+
                    "Solución obtenida: " + getMejorAsignacion()+
                    "\nSolución obtenida (tiempo máximo de ejecución): " + getTiempoMaximoOptimo() +
                    "\nMétrica para analizar el costo de la solución: "+ getContador();
        }
        return nombre+"\n"+
                "Solución obtenida: No existe solución";
    }
}