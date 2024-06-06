package tpe;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("./src/main/java/tpe/datasets/Procesadores.csv", "./src/main/java/tpe/datasets/Tareas.csv");
		int tiempoMaxNoRefrigerado = 100;

		servicios.greedy(tiempoMaxNoRefrigerado);
		servicios.getMejorAsignacion();
	}
}
