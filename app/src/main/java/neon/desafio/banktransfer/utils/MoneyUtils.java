package neon.desafio.banktransfer.utils;

import org.androidannotations.annotations.Extra;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyUtils {
    private static final Locale BRAZIL = new Locale("pt", "BR");
    private static final DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);

    public static DecimalFormat getLabeledMoneyFormatter() {
        return new DecimalFormat("¤###,###,##0.00", REAL);
    }

    public static DecimalFormat getMoneyFormatter() {
        return new DecimalFormat("###,###,##0.00", REAL);
    }

    public static String formatCurrency(String valueToFormat){
        String formatted = null;
        try{
            formatted = MoneyUtils.getMoneyFormatter().format(Float.parseFloat(valueToFormat));
        } catch (Exception e) {
            formatted = "--";
        }
        return formatted;
    }

    public static String formatLabeledCurrency(String valueToFormat){
        String formatted = null;
        try{
            formatted = MoneyUtils.getLabeledMoneyFormatter().format(Float.parseFloat(valueToFormat));
        } catch (Exception e) {
            formatted = "--";
        }
        return formatted;
    }

    public static double getUnformattedValue(String formattedValue) {
        try {
            return Double.parseDouble(formattedValue
                    .replace("R$ ", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim());
        }catch (Exception e) {
            return 0.0;
        }
    }
}
