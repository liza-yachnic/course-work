package utils;

public class Check {
    public static boolean isNumber(String number) {
        return number.matches("\\d+?");
    }

    public static boolean isFloat(String fltStr) {
        try {
            Float.parseFloat(fltStr);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{1,3}( |-)?[(]?\\d{2}[)]?( |-)?\\d{3}( |-)?\\d{4}$");
    }

    public static boolean isEmail(String email) {
        return email.matches("^.+@.+\\..{1,3}");
    }
}
