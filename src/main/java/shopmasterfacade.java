// Patrón Facade: simplifica el proceso completo de compra ("One-Click Checkout") en un único método de alto nivel.
public class ShopMasterFacade {
    private final ProcesadorPago procesadorPago;

    public ShopMasterFacade() {
        // El sistema siempre opera a través de la barrera de seguridad (Proxy)
        this.procesadorPago = new ProxySeguridadPago();
    }

    public boolean ejecutarCompraRapida(String usuario, Componente itemCatalogo, Extra... extras) {
        System.out.println("======================================================================");
        System.out.println("INICIANDO ONE-CLICK CHECKOUT | Cliente: " + usuario);
        System.out.println("----------------------------------------------------------------------");
        System.out.println("1. Desglose del catálogo seleccionado:");
        itemCatalogo.mostrar("   ");

        // Construcción del pedido base y envoltura dinámica con Decoradores
        Pedido pedido = new PedidoBase(itemCatalogo);
        double costoBase = pedido.getCosto();

        if (extras != null) {
            for (Extra extra : extras) {
                if (extra == null) continue;
                switch (extra) {
                    case GARANTIA:
                        pedido = new GarantiaExtendida(pedido);
                        break;
                    case EMPAQUE_REGALO:
                        pedido = new EmpaqueRegalo(pedido);
                        break;
                    case MANEJO_FRAGIL:
                        pedido = new ManejoFragil(pedido);
                        break;
                }
            }
        }

        double costoFinal = pedido.getCosto();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("2. Resumen del pedido y servicios post-venta:");
        System.out.println("   Detalle : " + pedido.getDescripcion());
        System.out.println("   Subtotal: $" + Formato.pesos(costoBase) + " COP");
        System.out.println("   Total   : $" + Formato.pesos(costoFinal) + " COP");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("3. Procesamiento de pago y control de seguridad:");
        boolean resultado = procesadorPago.cobrarEnPesos(usuario, costoFinal);
        System.out.println("   Estado final de la orden: " + (resultado ? "EXITOSA" : "RECHAZADA"));
        System.out.println("======================================================================\n");
        return resultado;
    }
}