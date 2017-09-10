package bignews.myapplication.db;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by lazycal on 2017/9/10.
 */

public class Preferences {
    public static final String PREFS_NAME = "MyPrefsFile";
    private Context context;
    public int classTags; // 1 << (i - 1), i \in [1, 12]
    public boolean isNight;
    public boolean onlyText;
    public Set<String> shieldKeywords;

    public Preferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        settings.getInt("classTags", classTags);
        settings.getBoolean("isNight", isNight);
        settings.getBoolean("onlyText", onlyText);
        settings.getStringSet("shieldKeywords", shieldKeywords);
    }

    Preferences(Context context, Preferences old_settings) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        this.classTags = old_settings.classTags;
        this.isNight = old_settings.isNight;
        this.onlyText = old_settings.onlyText;
        this.shieldKeywords = old_settings.shieldKeywords;
    }

    public boolean commit() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("classTags", classTags);
        editor.putBoolean("isNight", isNight);
        editor.putBoolean("onlyText", onlyText);
        editor.putStringSet("shieldKeywords", shieldKeywords);
        return editor.commit();
    }
}
