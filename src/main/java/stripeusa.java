// Subsistema externo (Adaptee): librería de pagos de Estados Unidos cuyo código no se puede modificar.
public class StripeUSA {

    public boolean chargeInDollars(double amountUSD, String customerEmail, String merchantKey) {
        if (merchantKey == null || merchantKey.trim().isEmpty()) {
            System.out.println("  [StripeUSA] ERROR: Invalid merchant key.");
            return false;
        }
        System.out.println("  [StripeUSA] Connecting to US payment gateway with key [" + merchantKey + "]...");
        System.out.println("  [StripeUSA] Charging $" + Formato.dolares(amountUSD) + " USD to customer: " + customerEmail);
        System.out.println("  [StripeUSA] Payment processed successfully in United States.");
        return true;
    }
}