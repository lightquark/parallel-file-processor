package org.lightquark.parallelfileprocessor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CardNumberUtils {

    private static final Pattern REGEXP = Pattern.compile("^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|"
            + "(?<mastercard>5[1-5][0-9]{14})|"
            + "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|"
            + "(?<amex>3[47][0-9]{13})|"
            + "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|"
            + "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$");

    public static boolean validate(String cardNumber) {
        return validateByRegexp(cardNumber) && validateByLush(cardNumber);
    }

    private static boolean validateByRegexp(String cardNumber) {
        return REGEXP.matcher(cardNumber).matches();
    }

    private static boolean validateByLush(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = (int) cardNumber.charAt(i) - 0x30;
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static String generate(String prefix, int length) {
        Random random = new Random();
        int cnt = length - 1;
        int[] cardNumber = new int[length];

        // Add prefix and random numbers
        for (int i = 0; i < cnt; i++) {
            cardNumber[i] = (i < prefix.length()) ? prefix.charAt(i) - 0x30 : random.nextInt(10);
        }

        // Computing sum
        boolean alternate = true;
        int sum = 0;
        int dbl;
        while (cnt-- > 0) {
            if (alternate) {
                dbl = 2 * cardNumber[cnt];
                sum += (dbl > 9) ? (dbl % 10 + 1) : dbl;
            } else {
                sum += cardNumber[cnt];
            }
            alternate = !alternate;
        }
        // Add the check digit
        cardNumber[length - 1] = (9 * sum) % 10;

        StringBuilder sb = new StringBuilder();
        Arrays.stream(cardNumber).forEach(sb::append);
        return sb.toString();
    }
}
