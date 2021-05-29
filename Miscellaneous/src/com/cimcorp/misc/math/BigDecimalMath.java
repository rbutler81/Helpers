package com.cimcorp.misc.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalMath {

    public static boolean LT(BigDecimal val1, BigDecimal val2) {
        return (val1.compareTo(val2) == -1);
    }

    public static boolean EQ(BigDecimal val1, BigDecimal val2) {
        return (val1.compareTo(val2) == 0);
    }

    public static boolean GT(BigDecimal val1, BigDecimal val2) {
        return (val1.compareTo(val2) == 1);
    }

    public static boolean LEQ(BigDecimal val1, BigDecimal val2) {
        return (val1.compareTo(val2) <= 0);
    }

    public static boolean GEQ(BigDecimal val1, BigDecimal val2) {
        return (val1.compareTo(val2) >= 0);
    }

    public static BigDecimal divide(BigDecimal quotient, BigDecimal divisor) {
        int scale = 0;
        if (quotient.scale() > divisor.scale()) {
            scale = quotient.scale();
        } else {
            scale = divisor.scale();
        }
        return quotient.divide(divisor, scale, RoundingMode.HALF_UP);
    }

    public static int divide(int quotient, int divisor) {
        return new BigDecimal(quotient).divide(new BigDecimal(divisor), 0, RoundingMode.HALF_UP).intValue();
    }

    public static int divide(BigDecimal quotient, BigDecimal divisor, int scale) {
        return quotient.divide(divisor, scale, RoundingMode.HALF_UP).intValue();
    }

    public static BigDecimal divide(int quotient, int divisor, int scale) {
        return new BigDecimal(quotient).divide(new BigDecimal(divisor), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiply(int a, BigDecimal b) {
        return new BigDecimal(a).multiply(b);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public static BigDecimal add(int a, BigDecimal b) {
        return new BigDecimal(a).add(b);
    }

    public static int add(int a, BigDecimal b, int precision) {
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        return new BigDecimal(a).add(b, mc).intValue();
    }

    public static BigDecimal subtract(int a, BigDecimal b) {
        return new BigDecimal(a).subtract(b);
    }

    public static int subtract(int a, BigDecimal b, int precision) {
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        return new BigDecimal(a).subtract(b, mc).intValue();
    }

}
