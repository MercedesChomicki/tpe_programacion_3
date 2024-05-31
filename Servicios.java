package tpe;

import tpe.utils.CSVReader;

import java.util.*;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "tpe.Tarea" según sus decisiones
 * de implementación.
 */

public class Servicios {
	final static int MAXCRITICAS = 2;
	Map<String, Tarea> tareas;
	Map<String, Procesador> procesadores;
	Map<Tarea, Procesador> asignadas;



	/*
	 * Expresar la complejidad temporal del constructor.
	 * O(n): recorre todos los procesadores O(n) y todas las tareas O(n).
	 */
	public Servicios(String pathProcesadores, String pathTareas)
	{
		CSVReader reader = new CSVReader();
		procesadores = reader.readProcessors(pathProcesadores);
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


	/*
	BACK (estado e, solucionActual *sol)
	\\ e: nodo del árbol de soluciones
	\\sol: solución actúa, que se va construyendo. También puede llevarse la mejor
	encontrada hasta el momento en otro parámetro.
	{
 		if ( SOLUCION (e))
			OperarSobreSolución (e, sol);
		else {
			int nrohijo = 1;
			estado siguiente;
			while ( HIJOS (nrohijo, e, siguiente ) )
		 		{ if ( !PODA ( siguiente, sol) )
 					{ AgregarASolucionActual(siguiente, sol);
 						BACK ( siguiente, sol);
 						QuitarDeSolucionActual(siguiente, sol);
 					}
				nrohijo++;
 		}
	}
}
	 */
	/*PREGUNTAR SOBRE LA PRIORIDAD, ES NECESARIO ORDENARLOS?
	* ESTA BIEN APLICADO EL BACKTRACKING? creo que no :(*/
	public void asignarTareas(int tiempoX){
		Iterator<Tarea> itTareas = tareas.values().iterator();

		while(itTareas.hasNext()){
			Tarea t = itTareas.next();
			asignarTarea(t, tiempoX);
		}
	}

	private void asignarTarea(Tarea t, int tiempoX){
		Iterator<Procesador> itProces = procesadores.values().iterator();

		while(itProces.hasNext()){
			Procesador p = itProces.next();
			if(poda(p, t, tiempoX)){
				p.settCriticas(p.gettCriticas()+1);
				asignadas.put(t, p);

			}
		}
	}


	private boolean poda(Procesador p, Tarea t, int tiempoX) {
		if((p.gettCriticas() == MAXCRITICAS)){
			return false;
		}
		if(!p.getRefrigerado()){
			if(t.getTiempo() > tiempoX){
				return false;
			}
		}
		return true;
	}
}