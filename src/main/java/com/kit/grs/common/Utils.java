package com.kit.grs.common;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Utils {

    public static boolean valueExists(Object[] values, int index) {
        if (values == null || values.length ==0) {
            return false;
        }
        if (values.length <=index) {
            return false;
        }
        if (values[index] == null) {
            return false;
        }
        return true;
    }

    public static Long getLongValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger) value).longValue();
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }

        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        if (value instanceof Float) {
            return ((Float) value).longValue();
        }

        if (value instanceof Double) {
            return ((Double)value).longValue();
        }

        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (Throwable t) {
                return null;
            }
        }

        return 0L;
    }
}
