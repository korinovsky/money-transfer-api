package main.helper;


/**
 * Class {@code Amount} is a container for static functions for Amount calculating.
 */
public class Amount {

    private static short decimals = 2;

    public static long toAmountFormat(double amount) {
        return Math.round(amount * Math.pow(10, decimals));
    }

    public static double fromAmountFormat(long amount) {
        return amount / Math.pow(10, decimals);
    }

}
