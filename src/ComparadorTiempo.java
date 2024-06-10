package tpe;

import java.util.Comparator;

public class ComparadorTiempo implements Comparator<Tarea> {

    @Override
    public int compare(Tarea t1, Tarea t2) {
        return t1.getTiempo().compareTo(t2.getTiempo());
    }
}
