package com.oasis.atm;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberFormatUtil {
    private NumberFormatUtil() {}
    public static String format(BigDecimal value) {
        NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        f.setMinimumFractionDigits(2);
        f.setMaximumFractionDigits(2);
        return f.format(value);
    }
    public static String group(String digits) {
        try { return String.format("%,d", Long.parseLong(digits)); }
        catch (Exception e) { return digits; }
    }
}
