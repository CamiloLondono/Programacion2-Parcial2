public abstract class PedidoDecorator implements Pedido {
    protected final Pedido pedido;

    protected PedidoDecorator(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String getDescripcion() { return pedido.getDescripcion(); }

    @Override
    public double getCosto() { return pedido.getCosto(); }
}

