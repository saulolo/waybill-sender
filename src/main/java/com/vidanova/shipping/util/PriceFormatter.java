package com.vidanova.shipping.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utilidad para formatear precios en estilo colombiano:
 * separador de miles con punto (.) y prefijo "$".
 *
 * <p>Ejemplos:
 * <ul>
 *   <li>"79900"  → "$79.900"</li>
 *   <li>"150000" → "$150.000"</li>
 *   <li>"5000"   → "$5.000"</li>
 * </ul>
 */
public final class PriceFormatter {

    /** Formato con punto como separador de miles (estilo colombiano / español). */
    private static final DecimalFormat FORMATTER;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');   // miles con punto
        symbols.setDecimalSeparator(',');    // decimales con coma (no se usan, pero se define)
        FORMATTER = new DecimalFormat("#,###", symbols);
    }

    private PriceFormatter() {
        // Clase de utilidad — no instanciar
    }

    /**
     * Formatea un valor numérico como String al estilo colombiano.
     *
     * @param rawValue cadena con solo dígitos (ej. "79900")
     * @return precio formateado con prefijo "$" (ej. "$79.900"),
     *         o {@code rawValue} sin cambios si no es parseable
     */
    public static String format(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) return "";
        try {
            long value = Long.parseLong(rawValue.trim());
            return "$" + FORMATTER.format(value);
        } catch (NumberFormatException e) {
            return rawValue;
        }
    }

    /**
     * Elimina el prefijo "$" y los separadores de miles "."
     * para obtener el valor numérico en bruto.
     *
     * @param formatted precio formateado (ej. "$79.900")
     * @return cadena de solo dígitos (ej. "79900")
     */
    public static String strip(String formatted) {
        if (formatted == null) return "";
        return formatted.replace("$", "").replace(".", "").trim();
    }
}
