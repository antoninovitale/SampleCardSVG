package sample.svgcard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public class Utils {


    private static final String MY_PREFERENCES = "app_preferences";

    private static SharedPreferences sharedPreferences;

    @SuppressWarnings("unused")
    private static void initialize(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    /**
     * converte density independent pixel (dp) in pixel (px)
     *
     * @param context
     * @param dp
     * @return
     */
    public static int convertDpToPx(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float logicalDensity = metrics.density;
        int px = (int) (dp * logicalDensity + 0.5);
        return px;
    }

    /**
     * converte pixel (px) in density independent pixel (dp)
     *
     * @param context
     * @param px
     * @return
     */
    public static int convertPxToDp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float logicalDensity = metrics.density;
        int dp = (int) ((px - 0.5) / logicalDensity);
        return dp;
    }

}
