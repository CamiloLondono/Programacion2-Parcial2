import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

// Utilidad pequeña para imprimir montos.
public class Formato {
    private static final DecimalFormatSymbols SIMBOLOS = new DecimalFormatSymbols(new Locale("es", "CO"));

    public static String pesos(double v) {
        return new DecimalFormat("#,##0", SIMBOLOS).format(v);
    }

    public static String dolares(double v) {
        return new DecimalFormat("#,##0.00", SIMBOLOS).format(v);
    }
}

