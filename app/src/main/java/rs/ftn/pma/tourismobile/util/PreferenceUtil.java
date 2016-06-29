package rs.ftn.pma.tourismobile.util;

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

    public static boolean isCommaArrayEmpty(String array) {
        return !(getCommaArrayLength(array) > 0 && array.length() > 0);
    }

    public static int getCommaArrayLength(String array) {
        return array.split(",").length;
    }

    public static int[] getCommaArrayNumbers(String array) {
        int length = getCommaArrayLength(array);
        String[] values = array.split(",");
        int[] numbers = new int[length];
        for (int i = 0; i < length; i++) {
            numbers[i] = Integer.valueOf(values[i]);
        }
        return numbers;
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
