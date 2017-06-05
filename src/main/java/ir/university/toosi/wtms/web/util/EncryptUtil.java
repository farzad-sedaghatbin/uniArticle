package ir.university.toosi.wtms.web.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class EncryptUtil {
    private static final BigDecimal SQRT_DIG = new BigDecimal(150);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    public String encrypt(String value) {
        value = "1" + value;
        BigInteger bValue = new BigInteger(value);
        bValue = bValue.pow(2);
        return bValue.toString();
    }

    public String decrypt(String value) {
        BigDecimal bValue = new BigDecimal(value);
        bValue = bigSqrt(bValue);
        int endIndex = bValue.toString().indexOf(".") + 50;
        value = bValue.toString().substring(0, endIndex);
        bValue = new BigDecimal(value);
        if (bValue.subtract(new BigDecimal(bValue.toBigInteger())).compareTo(new BigDecimal("0")) > 0) {
            return null;
        }
        return bValue.toBigInteger().toString().substring(1);
    }

    private BigDecimal bigSqrt(BigDecimal c) {
        return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
    }

    private BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1) {
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }
}