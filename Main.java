package tpe;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("./src/main/java/tpe/datasets/Procesadores.csv", "./src/main/java/tpe/datasets/Tareas.csv");
		int tiempoMaxNoRefrigerado = 60;

		//servicios.backtracking(tiempoMaxNoRefrigerado);
		//servicios.solucionBacktracking();

		servicios.greedy(tiempoMaxNoRefrigerado);
		servicios.solucionGreedy();

	}
}
