public class Main {
    public static void main(String[] args) {
        ShopMasterFacade checkout = new ShopMasterFacade();

        // =========================================================================
        // CASO DE PRUEBA 1: Compra Exitosa Completa
        // Combo principal ("Combo Setup Gamer") con Monitor ($800.000), Teclado ($200.000)
        // y un sub-combo ("Kit de Limpieza") con dos productos ($25.000 y $15.000).
        // Usuario válido ("cliente_vip") + Póliza de Garantía Extendida + Empaque de Regalo.
        // =========================================================================
        System.out.println(">>> CASO DE PRUEBA 1: COMPRA EXITOSA COMPLETA");
        Combo kitLimpieza = new Combo("Kit de Limpieza")
                .agregar(new Producto("Spray Limpiador Antiestático", 25000))
                .agregar(new Producto("Paño de Microfibra Especial", 15000));

        Combo comboSetupGamer = new Combo("Combo Setup Gamer")
                .agregar(new Producto("Monitor Gamer 27\"", 800000))
                .agregar(new Producto("Teclado Mecánico RGB", 200000))
                .agregar(kitLimpieza);

        checkout.ejecutarCompraRapida(
                "cliente_vip",
                comboSetupGamer,
                Extra.GARANTIA,
                Extra.EMPAQUE_REGALO
        );

        // =========================================================================
        // CASO DE PRUEBA 2: Rechazo por Tope de Dinero
        // Producto individual costoso ("Servidor Empresarial" de $6.000.000 COP)
        // + Manejo Prioritario Frágil ($25.000 COP) con usuario normal.
        // Supera el límite de $5.000.000 COP -> Rechazo sin tocar pasarela de EE. UU.
        // =========================================================================
        System.out.println(">>> CASO DE PRUEBA 2: RECHAZO POR TOPE DE DINERO");
        Producto servidorEmpresarial = new Producto("Servidor Empresarial", 6000000);

        checkout.ejecutarCompraRapida(
                "usuario_corporativo",
                servidorEmpresarial,
                Extra.MANEJO_FRAGIL
        );

        // =========================================================================
        // CASO DE PRUEBA 3: Rechazo por Usuario Reportado
        // Producto económico ("Mouse" de $150.000 COP), cliente = "BLOQUEADO".
        // La barrera de seguridad impide el paso inmediatamente y muestra alerta.
        // =========================================================================
        System.out.println(">>> CASO DE PRUEBA 3: RECHAZO POR USUARIO REPORTADO");
        Producto mouseEconomico = new Producto("Mouse Óptico", 150000);

        checkout.ejecutarCompraRapida(
                "BLOQUEADO",
                mouseEconomico
        );
    }
}