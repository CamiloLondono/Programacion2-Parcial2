public class Producto implements Componente {
    private final String nombre;
    private final double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }

    @Override
    public void mostrar(String sangria) {
        System.out.println(sangria + "- " + nombre + ": $" + Formato.pesos(precio));
    }
}


