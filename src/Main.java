package tpe;

import java.util.List;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("src/datasets/Procesadores.csv", "src/datasets/Tareas.csv");
		int tiempoMaxNoRefrigerado = 200;

		/* SERVICIO 1 */
		//Tarea t = servicios.servicio1("T2");
		//System.out.println(t);

		/* SERVICIO 2 */
		//List<Tarea> tareas = servicios.servicio2(true);
		//System.out.println(tareas);

		/* SERVICIO 3 */
		//List<Tarea> tareasEntrePrioridades = servicios.servicio3(30,60);
		//System.out.println(tareasEntrePrioridades);

		/* BACKTRACKING */
		Solucion backtracking = servicios.backtracking(tiempoMaxNoRefrigerado);
		System.out.println(backtracking);

		/* GREEDY */
		//Solucion greedy = servicios.greedy(tiempoMaxNoRefrigerado);
		//System.out.println(greedy);

	}
}
