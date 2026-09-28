package utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.example.triviaquiz.R;

public class ThemeManager {

    public static int getThemeRes(int selectedThemeId) {
        switch (selectedThemeId) {
            case 12: return R.style.Theme_TriviaQuiz_Dark;
            case 13: return R.style.Theme_TriviaQuiz_Ocean;
            case 14: return R.style.Theme_TriviaQuiz_Sunset;
            default: return R.style.Theme_TriviaQuiz_Light;
        }
    }

    public static void apply(Activity activity) {
        int themeId = CurrentUser.instance != null
                ? CurrentUser.instance.selectedThemeId
                : -1;
        activity.setTheme(getThemeRes(themeId));
    }

    //save local if firebase takes a while or pag naka logout
    public static void saveLocal(Context context, int selectedThemeId) {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("selectedThemeId", selectedThemeId)
                .apply();
    }

    public static int loadLocal(Context context) {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getInt("selectedThemeId", -1);
    }
    public static int getColorFromAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        if (theme.resolveAttribute(attr, typedValue, true)) {
            return typedValue.data;
        }
        return ContextCompat.getColor(context,R.color.surfaceWhite);
    }
}