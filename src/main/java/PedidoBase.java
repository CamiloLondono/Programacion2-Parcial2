public class PedidoBase implements Pedido {
    private final Componente contenido;

    public PedidoBase(Componente contenido) {
        this.contenido = contenido;
    }

    public Componente getContenido() { return contenido; }

    @Override
    public String getDescripcion() { return "Pedido de " + contenido.getNombre(); }

    @Override
    public double getCosto() { return contenido.getPrecio(); }
}

