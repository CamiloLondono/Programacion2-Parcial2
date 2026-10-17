// Patrón Proxy: intercepta el cobro para validar usuario bloqueado y tope de $5.000.000 COP
// sin siquiera instanciar ni tocar el procesador bancario real en caso de rechazo.
public class ProxySeguridadPago implements ProcesadorPago {
    private static final double TOPE_MAXIMO_COP = 5_000_000.0;
    private static final String USUARIO_REPORTADO = "BLOQUEADO";
    private static final String MERCHANT_KEY_DEFAULT = "SM-USA-LIVE-998877";

    // Se mantiene en null hasta que una transacción supere todas las validaciones (Lazy Initialization).
    private ProcesadorPago procesadorReal;

    @Override
    public boolean cobrarEnPesos(String usuario, double montoCOP) {
        System.out.println("  [ProxySeguridad] Verificando transacción de '" + usuario
                + "' por $" + Formato.pesos(montoCOP) + " COP...");

        if (usuario != null && usuario.trim().equalsIgnoreCase(USUARIO_REPORTADO)) {
            System.out.println("  [ALERTA DE SEGURIDAD - RECHAZADO] El usuario '" + usuario
                    + "' se encuentra reportado como BLOQUEADO. No se contactó a la pasarela bancaria.");
            return false;
        }

        if (montoCOP > TOPE_MAXIMO_COP) {
            System.out.println("  [CONTROL DE CUPOS - RECHAZADO] El monto de $" + Formato.pesos(montoCOP)
                    + " COP supera el tope máximo permitido de $" + Formato.pesos(TOPE_MAXIMO_COP)
                    + " COP. No se contactó a la pasarela bancaria.");
            return false;
        }

        // Solo si las validaciones son exitosas se instancia y se invoca el procesador real.
        if (procesadorReal == null) {
            procesadorReal = new AdaptadorStripeUSA(new StripeUSA(), MERCHANT_KEY_DEFAULT);
        }

        boolean aprobado = procesadorReal.cobrarEnPesos(usuario, montoCOP);
        if (aprobado) {
            System.out.println("  [AUDITORÍA: Transacción aprobada para " + usuario
                    + " por un valor de $" + Formato.pesos(montoCOP) + " COP]");
        }
        return aprobado;
    }
}