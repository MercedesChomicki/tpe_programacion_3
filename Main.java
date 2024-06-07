package tpe;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("./src/main/java/tpe/datasets/Procesadores.csv", "./src/main/java/tpe/datasets/Tareas.csv");
		int tiempoMaxNoRefrigerado = 100;

		//servicios.greedy(tiempoMaxNoRefrigerado);
		servicios.backtracking(tiempoMaxNoRefrigerado);
		servicios.getMejorAsignacion();

		/*
		T1;Tarea1;50;true;92 --> P1
		T3;Tarea3;100;false;70 --> P2
		T4;Tarea4;20;true;58 --> P3
		T2;Tarea2;10;false;31 --> P3
		*/
	}
}
