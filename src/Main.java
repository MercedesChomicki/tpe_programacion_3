package tpe;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("datasets/Procesadores.csv", "datasets/Tareas.csv");
		int tiempoMaxNoRefrigerado = 60;

		//servicios.backtracking(tiempoMaxNoRefrigerado);
		//servicios.solucionBacktracking();

		servicios.greedy(tiempoMaxNoRefrigerado);
		servicios.solucionGreedy();

	}
}
