package tpe;

import tpe.utils.CSVReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "tpe.Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	CSVReader reader;
	Map<String, Tarea> tareas;
	/*
     * Expresar la complejidad temporal del constructor.
     */
	public Servicios(String pathProcesadores, String pathTareas)
	{
		reader = new CSVReader();
		reader.readProcessors(pathProcesadores);
		tareas = reader.readTasks(pathTareas);

	}
	
	/*
     * Expresar la complejidad temporal del servicio 1.
     * O(1)
     */
	public Tarea servicio1(String ID) {
		if(ID != null){
			return tareas.get(ID);
		}
		return null;
	}
    
    /*
     * Expresar la complejidad temporal del servicio 2.
     */
	public List<Tarea> servicio2(boolean esCritica) {
		return null;
	}

    /*
     * Expresar la complejidad temporal del servicio 3.
     */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		return null;
	}

}
