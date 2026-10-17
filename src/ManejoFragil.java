public class ManejoFragil extends PedidoDecorator {
    public ManejoFragil(Pedido pedido) { super(pedido); }

    @Override
    public String getDescripcion() { return pedido.getDescripcion() + " + Manejo Prioritario Frágil"; }

    @Override
    public double getCosto() { return pedido.getCosto() + 25000; }
}
