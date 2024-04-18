package il.meuhedet.temielderpeople.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NANE = "userName";
    private SharedPreferences prefs;

    public SharedPreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_NANE, userName);
        editor.apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NANE, null);
    }
}
