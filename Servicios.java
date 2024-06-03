package tpe;

import tpe.utils.CSVReader;

import java.util.*;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "tpe.Tarea" según sus decisiones
 * de implementación.
 */

public class Servicios {
	private static final int MAXCRITICAS = 2;
	private Map<String, Tarea> tareas;
	private Map<String, Procesador> procesadores;
	private List<Tarea> criticas;
	private List<Tarea> noCriticas;
	private Map<Procesador, List<Tarea>> mejorAsignacion;
	private int tiempoMaximoOptimo; //infinito
	private boolean corteBusqueda;


	/*
	 * Expresar la complejidad temporal del constructor.
	 * O(n): recorre todos los procesadores O(n) y todas las tareas O(n).
	 */
	public Servicios(String pathProcesadores, String pathTareas)
	{
		CSVReader reader = new CSVReader();
		procesadores = reader.readProcessors(pathProcesadores);
		tareas = reader.readTasks(pathTareas);
		criticas = new ArrayList<>();
		noCriticas = new ArrayList<>();
		this.dividirPorCriticidad();
		this.ordenarTareasPorPrioridad();
		mejorAsignacion = new HashMap<>();
		tiempoMaximoOptimo = Integer.MAX_VALUE;
		corteBusqueda = false;
    }

	private void dividirPorCriticidad(){
		for(Tarea t: tareas.values()){
			if(t.esCritica()){
				criticas.add(t);
			} else {
				noCriticas.add(t);
			}
		}
	}

	private void ordenarTareasPorPrioridad(){
		//ordenar
	}

	/*
	 * Expresar la complejidad temporal del servicio 1.
	 * O(1): al ser HashMap accede directamente a la Tarea a partir del ID.
	 */
	public Tarea servicio1(String ID) {
		return tareas.get(ID);
	}

	/*
	 * Expresar la complejidad temporal del servicio 2.
	 * O(1): devuelve el ArrayList de tareas criticas o no criticas segun se pase por parametro
	 */
	public List<Tarea> servicio2(boolean esCritica) {
		if(esCritica){
			return new ArrayList<>(criticas);
		} else {
			return new ArrayList<>(noCriticas);
		}
	}

	/*
	 * Expresar la complejidad temporal del servicio 3.
	 * O(n): recorre (en el peor de los casos) todas las tareas (si las prioridades son los extremos)
	 */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {

		//ordenar por prioridad -->en el constructor
		//pivote o busco el menor dentro de las tareas con la prioridad inferior pasada por parametro y de ahi recorro hasta la superior

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

	public void asignarTareas(int tiempoMaxNoRefrigerado) {
		//Pasamos todos los valores del mapa tareas al ArrayList tasks
		ArrayList<Tarea> tasks = new ArrayList<>(tareas.values());

		//Creamos un nuevos HashMap para ir guardando el estado de asignaciones
		Map<Procesador, List<Tarea>> asignacionActual = new HashMap<>();

		//Inicializamos asignacionActual: asignamos como keys todos los procesadores y como values ArrayList vacios
		for (Procesador p : procesadores.values()) {
			asignacionActual.put(p, new ArrayList<>());
		}

		// Buscamos la mejor solución (mínimo tiempo máximo de ejecución)
		backtracking(asignacionActual, tasks, 0, tiempoMaxNoRefrigerado);
	}

	private void backtracking(Map<Procesador, List<Tarea>> asignacionActual, List<Tarea> tasks, int indice, int tiempoMaximoNoRefrigerado) {

		if(corteBusqueda){
			return;
		}

		// Si no encuentra más tareas, se compara el tiempo maximo de la asignacion actual con el tiempo maximo previo (inicializado como "infinito")
		if (indice == tasks.size()) {
			int tiempoMaximo = calcularTiempoMaximoActual(asignacionActual);

			// Si el tiempo obtenido es menor al previo lo reemplazamos para obtener el minimo posible
			if (tiempoMaximo < tiempoMaximoOptimo) {
				tiempoMaximoOptimo = tiempoMaximo;
				mejorAsignacion = new HashMap<>(asignacionActual);

				if(poda2()){
					corteBusqueda = true;
					return;
				}
			}
			return;
		}

		Tarea t = tasks.get(indice);
		for (Procesador p : asignacionActual.keySet()) {
			if (esAsignacionValida(p, t, tiempoMaximoNoRefrigerado)) {

				// PODA1: Si el tiempo que se obtendría ya es mayor al tiempo minimo obtenido, no tiene sentido asignarlo. Cortamos ejecucion de esa rama
				if(!poda1(p, t)){
					asignacionActual.get(p).add(t);
					p.incrementarTiempoTotal(t.getTiempo());
					if(t.esCritica()){
						p.incrementarTCriticas();
					}
					backtracking(asignacionActual, tasks, indice + 1, tiempoMaximoNoRefrigerado);

					// PODA2: Si el tiempo obtenido es el menor tiempo posible (porque por ejemplo la tarea más lenta es 100 y tiempo no puede ser menor a eso), corto la ejecucion
					if(corteBusqueda) return;

					asignacionActual.get(p).remove(t);
					p.decrementarTiempoTotal(t.getTiempo());
					if(t.esCritica()){
						p.decrementarTCriticas();
					}
				}
			}
		}
	}

	private boolean esAsignacionValida(Procesador p, Tarea t, int tiempoMaximoNoRefrigerado) {
		int tiempoTotal = p.getTiempoTotal();
		int tareasCriticas = p.cantTCriticas();

		if (t.esCritica() && tareasCriticas >= 2) return false;
		if (!p.esRefrigerado() && (tiempoTotal + t.getTiempo()) > tiempoMaximoNoRefrigerado) return false;

		return true;
	}

	private boolean poda1(Procesador p, Tarea t){
		return (p.getTiempoTotal() + t.getTiempo()) > tiempoMaximoOptimo;
	}

	private boolean poda2(){
		return tiempoMaximoOptimo == tiempoMaximoTareas();
	}

	private int calcularTiempoMaximoActual(Map<Procesador, List<Tarea>> asignacionActual) {
		int tiempoMaximo = 0;
		// Con keySet() obtenemos todas las claves de la asignacion actual (todos los procesadores)
		for(Procesador p : asignacionActual.keySet()){
			// Por cada procesador, obtenemos el tiempo total de procesamiento
			int tiempoTotal = p.getTiempoTotal();
			// Si el tiempoTotal de p es mayor al tiempoMaximo que habiamos obtenido anteriormente, lo reemplazamos
			if(tiempoTotal > tiempoMaximo){
				tiempoMaximo = tiempoTotal;
			}
		}
		return tiempoMaximo;
	}

	public int tiempoMaximoTareas(){
		int tiempoMax = 0;
		for(Tarea t: tareas.values()){
			if(t.getTiempo() > tiempoMax){
				tiempoMax = t.getTiempo();
			}
		}
		return tiempoMax;
	}

	public void getMejorAsignacion(){
		for (Map.Entry<Procesador, List<Tarea>> entry : mejorAsignacion.entrySet()) {
			Procesador procesador = entry.getKey();
			List<Tarea> tareas = entry.getValue();

			System.out.println("Procesador: "+procesador);
			for(Tarea tarea: tareas){

				System.out.println("hola");
			}
		}
	}

//	public void asignarTareas(int tiempoX){
//		ArrayList<Tarea> tasks = new ArrayList<>(tareas.values());
//		ArrayList<Procesador> processors = new ArrayList<>(procesadores.values());
//		Asignacion solucionActual = new Asignacion();
//
//		back(solucionActual, tasks, processors, tiempoX);
//		//back(solucionActual, tasks, tiempoX);
//	}
//
//	private void back(Asignacion solucionActual, ArrayList<Tarea> tasks, ArrayList<Procesador> processors, int tiempoX){
//	//private void back(Asignacion solucionActual, ArrayList<Tarea> tasks, int tiempoX){
//
//		//SOLUCION 1:
//		//[(T1, P1)]
//		//[(T1, P1), (T2, P1)]
//		//[(T1, P1), (T2, P1), (T3, P1)]
//		//[(T1, P1), (T2, P1), (T3, P1), (T4, P1)]
//
//		if(tasks.isEmpty()){
//			if(mejorSolucion == null || solucionActual.tiempoTotal() < mejorSolucion.tiempoTotal()){
//				mejorSolucion = solucionActual;
//			}
//		} else {
////			Iterator<Procesador> itProces = procesadores.values().iterator();
////			while(itProces.hasNext()){
////				Procesador p = itProces.next();
//				Procesador p = processors.getFirst();
//				Tarea t = tasks.getFirst();
//				if(cumpleRestricciones(p,t,tiempoX)){
//					solucionActual.asignarTarea(t,p);
//					solucionActual.incrementarTiempo(t.getTiempo());
//					tasks.removeFirst();
//					processors.removeFirst();
//					back(solucionActual, tasks, processors, tiempoX);
//					poda(processors);
//				}
//
//
//					//tasks.agregarAlComienzo(); //?
//
////				if(poda(p, t, tiempoX)){
////					if(t.esCritica()){
////						p.incrementarTCriticas();
////					}
////					//p.incrementarTiempo(t.getTiempo())
////					//asignaciones.put(t, p);
////
////				}
//			//}
//		}
//	}

	private void poda(ArrayList<Procesador> processors) {
		if(processors != null){
			processors.clear();
		}
	}

//	private boolean cumpleRestricciones(Procesador p, Tarea t, int tiempoX) {
//		if((p.gettCriticas() == MAXCRITICAS)){
//			return false;
//		}
//		if(!p.getRefrigerado()){
//			if(t.getTiempo() > tiempoX){ //p.tiempoTotalEjecucion + t.tiempoEjecucion
//				return false;
//			}
//		}
//		return true;
//	}
}