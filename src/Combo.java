import java.util.ArrayList;
import java.util.List;

// Compuesto del Composite: contiene productos u otros combos (cualquier nivel).
public class Combo implements Componente {
    private final String nombre;
    private final List<Componente> hijos = new ArrayList<>();

    public Combo(String nombre) {
        this.nombre = nombre;
    }

    public Combo agregar(Componente c) {
        hijos.add(c);
        return this;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() {
        double total = 0;
        for (Componente c : hijos) {
            total += c.getPrecio();   // recursivo: no importa si es producto o combo
        }
        return total;
    }

    @Override
    public void mostrar(String sangria) {
        System.out.println(sangria + "+ " + nombre + " (combo) = $" + Formato.pesos(getPrecio()));
        for (Componente c : hijos) {
            c.mostrar(sangria + "    ");
        }
    }
}

