package com.poojithairosha.core.util;

import java.text.DecimalFormat;

public class DecimalFormatter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static String format(double value) {
        return DECIMAL_FORMAT.format(value);
    }

}
