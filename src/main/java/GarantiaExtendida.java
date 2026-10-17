public class GarantiaExtendida extends PedidoDecorator {
    public GarantiaExtendida(Pedido pedido) { super(pedido); }

    @Override
    public String getDescripcion() { return pedido.getDescripcion() + " + Garantía Extendida"; }

    @Override
    public double getCosto() { return pedido.getCosto() * 1.10; }
}
