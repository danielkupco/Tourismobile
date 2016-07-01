package rs.ftn.pma.tourismobile.util;

import android.text.TextUtils;
import android.view.View;

/**
 * Created by Daniel Kupčo on 28.06.2016.
 */
public class PreferenceUtil {

    public static String addNumberToCommaArray(String array, int number) {
        if (array.length() == 0) {
            return number + "";
        } else {
            return String.format("%s, %d", array, number);
        }
    }

    public static String removeNumberFromCommaArray(String array, int number) {
        if(array.length() == 0) {
            return "";
        }
        int length = getCommaArrayLength(array);
        String[] values = array.split(",");
        String[] retVal = new String[length - 1];
        for (int i = 0, j = 0; i < length; i++) {
            if(Integer.valueOf(values[i].trim()) != number) {
                retVal[j] = values[i];
                j++;
            }
        }
        return TextUtils.join(",", retVal);
    }

    public static boolean isCommaArrayEmpty(String array) {
        return !(getCommaArrayLength(array) > 0 && array.length() > 0);
    }

    public static int getCommaArrayLength(String array) {
        return array.split(",").length;
    }

    public static int[] getCommaArrayNumbers(String array) {
        if(array.length() == 0) {
            return new int[0];
        }
        int length = getCommaArrayLength(array);
        String[] values = array.split(",");
        int[] numbers = new int[length];
        for (int i = 0; i < length; i++) {
            numbers[i] = Integer.valueOf(values[i].trim());
        }
        return numbers;
    }

    public static boolean isNumberInCommaArray(String array, int number) {
        if(array.length() == 0) {
            return false;
        }
        int length = getCommaArrayLength(array);
        String[] values = array.split(",");
        for (int i = 0; i < length; i++) {
            if(Integer.valueOf(values[i].trim()) == number) {
                return true;
            }
        }
        return false;
    }

    public static int getSelectionModeVisibility(boolean mode) {
        if(mode) {
            return View.VISIBLE;
        }
        else {
            return View.INVISIBLE;
        }
    }

}
