public class EmpaqueRegalo extends PedidoDecorator {
    public EmpaqueRegalo(Pedido pedido) { super(pedido); }

    @Override
    public String getDescripcion() { return pedido.getDescripcion() + " + Empaque de Regalo"; }

    @Override
    public double getCosto() { return pedido.getCosto() + 15000; }
}

