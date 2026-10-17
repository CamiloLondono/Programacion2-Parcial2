// Patrón Adapter: adapta StripeUSA (en dólares e inglés) al contrato local ProcesadorPago (en pesos).
public class AdaptadorStripeUSA implements ProcesadorPago {
    private static final double TASA_CAMBIO_USD = 4000.0;
    private final StripeUSA stripeUSA;
    private final String merchantKey;

    public AdaptadorStripeUSA(StripeUSA stripeUSA, String merchantKey) {
        this.stripeUSA = stripeUSA;
        this.merchantKey = merchantKey;
    }

    @Override
    public boolean cobrarEnPesos(String usuario, double montoCOP) {
        double montoUSD = montoCOP / TASA_CAMBIO_USD;
        String customerEmail = usuario.contains("@") ? usuario : usuario + "@shopmaster.com";
        System.out.println("  [AdaptadorStripeUSA] Convirtiendo $" + Formato.pesos(montoCOP)
                + " COP -> $" + Formato.dolares(montoUSD) + " USD (Tasa: 1 USD = $4.000 COP)");
        return stripeUSA.chargeInDollars(montoUSD, customerEmail, merchantKey);
    }
}