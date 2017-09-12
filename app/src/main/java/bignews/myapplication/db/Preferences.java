package bignews.myapplication.db;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lazycal on 2017/9/10.
 */

public class Preferences {
    public static final String PREFS_NAME = "MyPrefsFile";
    public int classTags; // 1 << (i - 1), i \in [1, 12]
    public boolean isNight;
    public boolean isOffline;
    public boolean onlyText;
    public Set<String> shieldKeywords;

    public Preferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        classTags = settings.getInt("classTags", 0);
        isNight = settings.getBoolean("isNight", false);
        onlyText = settings.getBoolean("onlyText", false);
        isOffline = settings.getBoolean("isOffline", false);
        shieldKeywords = settings.getStringSet("shieldKeywords", new HashSet<String>());
    }

    Preferences(Context context, Preferences old_settings) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        this.classTags = old_settings.classTags;
        this.isNight = old_settings.isNight;
        this.onlyText = old_settings.onlyText;
        this.isOffline = old_settings.isOffline;
        this.shieldKeywords = old_settings.shieldKeywords;
    }

    public boolean commit(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("classTags", classTags);
        editor.putBoolean("isNight", isNight);
        editor.putBoolean("onlyText", onlyText);
        editor.putBoolean("isOffline", isOffline);
        editor.putStringSet("shieldKeywords", shieldKeywords);
        return editor.commit();
    }

    @Override
    public String toString() {
        return "Preferences{" +
                "classTags=" + classTags +
                ", isNight=" + isNight +
                ", isOffline=" + isOffline +
                ", onlyText=" + onlyText +
                ", shieldKeywords=" + shieldKeywords +
                '}';
    }
}
