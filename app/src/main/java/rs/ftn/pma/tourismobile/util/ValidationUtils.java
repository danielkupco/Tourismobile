package rs.ftn.pma.tourismobile.util;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Class that encapsulates validation logic.
 * Created by danielkupco on 5/18/16.
 */
public class ValidationUtils {

    /**
     * Validates name consists only letters. Can have spaces between.
     * @param name
     * @return boolean
     */
    public static boolean isAlphaNumValid(String name) {
        char[] chars = name.trim().toCharArray();

        for (char c : chars) {
            if(!(Character.isLetterOrDigit(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that name consists only letters. Can have spaces between.
     * @param name
     * @return boolean
     */
    public static boolean isNameValid(String name) {
        char[] chars = name.trim().toCharArray();

        for (char c : chars) {
            if(!(Character.isLetter(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates email for regex '*@*.*'
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        int monkey = email.indexOf("@");
        int dot = email.lastIndexOf(".");
        return monkey > 0 && dot > monkey + 1 && dot < email.length() - 1 ;
    }

    /**
     * Validates password has at least 4 characters.
     * @param password
     * @return boolean
     */
    public static boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
     * Validates field and sets the error messages if error is present.
     * @param field field that is being validated
     * @param validContent result of custom validation
     * @param emptyMessage message if field is empty
     * @param errorMessage message for custom validation error
     * @return validation status
     */
    public static boolean validateField(TextView field, boolean validContent, String emptyMessage, String errorMessage) {
        field.setError(null);
        boolean valid = true;

        final String value = field.getText().toString();
        if (TextUtils.isEmpty(value)) {
            field.setError(emptyMessage);
            valid = false;
        } else if (!validContent) {
            field.setError(errorMessage);
            valid = false;
        }

        return valid;
    }

}
