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
	private List<Tarea> tareasPorPrioridad;

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
		tareasPorPrioridad = new ArrayList<>(tareas.values());
		this.dividirPorCriticidad();
		this.ordenarTareasPorPrioridad();
		mejorAsignacion = new HashMap<>();
		tiempoMaximoOptimo = Integer.MAX_VALUE;
		corteBusqueda = false;
		asignarClaves(mejorAsignacion);
    }

	private void asignarClaves(Map<Procesador, List<Tarea>> mejorAsignacion){
		//Inicializamos mejorAsignacion: asignamos como keys todos los procesadores y como values ArrayList vacios
		for (Procesador p : procesadores.values()) {
			mejorAsignacion.put(p, new ArrayList<>());
		}
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
		Comparator<Tarea> comparador = new ComparadorPrioridad();
		Collections.sort(tareasPorPrioridad, comparador);
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
		List<Tarea> resultantesDelRango = new LinkedList<>();
		Iterator<Tarea> itTareas = tareasPorPrioridad.iterator();
		while(itTareas.hasNext() && itTareas.next().getPrioridad() <= prioridadSuperior){
			Tarea t = itTareas.next();
			if(t.getPrioridad() >= prioridadInferior){
				resultantesDelRango.add(t);
			}
		}
		return resultantesDelRango;
	}

	public void asignarTareas(int tiempoMaxNoRefrigerado) {
		//Creamos un nuevos HashMap para ir guardando el estado de asignaciones
		Map<Procesador, List<Tarea>> asignacionActual = new HashMap<>();

		//Inicializamos asignacionActual igual a mejorAsignacion que tiene asignados todos los procesadores como clave y la lista vacia
		asignacionActual = new HashMap<>(mejorAsignacion);

		// Buscamos la mejor solución (mínimo tiempo máximo de ejecución)
		backtracking(asignacionActual,tareasPorPrioridad, 0, tiempoMaxNoRefrigerado);
	}

	private void backtracking(Map<Procesador, List<Tarea>> asignacionActual, List<Tarea> tasks, int indice, int tiempoMaximoNoRefrigerado) {

		if(corteBusqueda){return;}

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
				// PODA2: Si el tiempo obtenido es el menor tiempo posible (porque por ejemplo la tarea más lenta es 100 y tiempo no puede ser menor a eso), corto la ejecucion
				if(!poda1(p, t) && corteBusqueda){
					asignacionActual.get(p).add(t);
					p.incrementarTiempoTotal(t.getTiempo());
					if(t.esCritica()){
						p.incrementarTCriticas();
					}
					backtracking(asignacionActual, tasks, indice + 1, tiempoMaximoNoRefrigerado);
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

	// Devuelve la tarea que lleva mayor tiempo de procesamiento
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
		// Imprimir la asignación resultante
		for (Map.Entry<Procesador, List<Tarea>> entry : mejorAsignacion.entrySet()) {
			System.out.println("Procesador: " + entry.getKey().getId());
			for (Tarea tarea : entry.getValue()) {
				System.out.println("  - " + tarea.getNombre());
			}
		}
	}

	/* Teniendo en cuenta el tiempo de la tarea de tiempo maximo, ir agregando a los procesadores (por prioridad)
	* las tareas y cuando llegue a ese tiempo maximo, cambiar de procesador. Si el recorrido de los procesadores termina
	* (no tenemos mas procesadores), tendria que volver a empezar para seguir asignando desde el primer procesador */

	public void greedy(int tiempoMaxNoRefrigerado){

		ArrayList<Procesador> processors = new ArrayList<>(procesadores.values());

		int posP=0;
		for(int posT = tareasPorPrioridad.size()-1; posT >= 0; posT--){
			Tarea t = tareasPorPrioridad.get(posT);
			Procesador p = processors.get(posP);

			if(p.getTiempoTotal() + t.getTiempo() <= tiempoMaximoTareas()
					&& esAsignacionValida(p, t, tiempoMaxNoRefrigerado))
			{
				mejorAsignacion.get(p).add(t);
				p.incrementarTiempoTotal(t.getTiempo());
			}

			//Si recorrimos todos los procesadores y aún quedan tareas por asignar, volvemos a recorrer desde el primer procesador
			if(posP == processors.size()) posP=0;

			posP++;
		}

		/* Ejemplo: Tmax=100, Tareas = [t1=20, t2,=30, t3=40, t4=50, t5=100, t6=60, t7=10, T=50]
		p1= |20|30|40| -->90+50<=100 NO --->
		p2= |50| --> 50+100<=100 NO --->
		P3= |100| --> 100+60 <=100 NO --->
		P4= |60|10| --> 70+50<=100 NO -->
		P1= |20|30|40|50|
		*/
	}



}