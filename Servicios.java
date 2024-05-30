package tpe;

import tpe.utils.CSVReader;

import java.util.*;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "tpe.Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	Map<String, Tarea> tareas;

	/*
	 * Expresar la complejidad temporal del constructor.
	 * O(n): recorre todos los procesadores O(n) y todas las tareas O(n).
	 */
	public Servicios(String pathProcesadores, String pathTareas)
	{
		CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores);
		tareas = reader.readTasks(pathTareas);
	}

	/*
	 * Expresar la complejidad temporal del servicio 1.
	 * O(1): al ser HashMap accede directamente a la Tarea a partir del ID.
	 */
	public Tarea servicio1(String ID) {
		if(ID != null){
			return tareas.get(ID);
		}
		return null;
	}

	/*
	 * Expresar la complejidad temporal del servicio 2.
	 * O(n): recorre todos los valores de las tareas criticas (o no criticas) segun se pase por parametro
	 */
	public List<Tarea> servicio2(boolean esCritica) {
		List<Tarea> tareasEsCritica = new LinkedList<>();
		Iterator<Tarea> itTareas = tareas.values().iterator();
		while(itTareas.hasNext()){
			Tarea t = itTareas.next();
			if(t.getCritica() == esCritica){
				tareasEsCritica.add(t);
			}
		}
		return tareasEsCritica;
	}

	/*
	 * Expresar la complejidad temporal del servicio 3.
	 * O(n): recorre (en el peor de los casos) todas las tareas (si las prioridades son los extremos)
	 */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> resultantesDelRango = new LinkedList<>();
		Iterator<Tarea> itTareas = tareas.values().iterator();
		while(itTareas.hasNext()){
			Tarea t = itTareas.next();
			if((prioridadInferior>t.getPrioridad()) && (t.getPrioridad()<prioridadSuperior)){
				resultantesDelRango.add(t);
			}
		}
		return resultantesDelRango;
	}

}