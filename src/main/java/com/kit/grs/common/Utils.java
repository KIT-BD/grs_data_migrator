package com.kit.grs.common;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Utils {

    public static final String INDEX_MOVEMENT = "index_complaint_movements";
    public static final String INDEX_COMPLAINTS = "index_complaints";

    public static final String[] complain_fields = new String[] {
            "custom_layer", "id", "layer_level", "medium_of_submission", "office_id","office_origin", "self_motivated", "tracking_number", "grievance_type"
    };

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

    public static boolean isInList(String value, String... values) {
        if (value == null) {
            return false;
        }
        if (values == null) {
            return false;
        }
        for (String val : values) {
            if (val == null) {
                continue;
            }
            if (val.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
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
