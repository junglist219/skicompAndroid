package de.skicomp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.skicomp.data.manager.UserManager;
import de.skicomp.models.SkiArea;
import de.skicomp.network.SkiService;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SessionManager {

    private static final String GLOBAL_PREFS = "globalPrefs_";

    private static final String KEY_USERNAME = "keyUsername";
    private static final String KEY_PW = "keyPW";
    private static final String KEY_SKI_AREAS = "keySkiAreas";

    private static SessionManager instance;

    private SharedPreferences sharedPreferences;

    private Gson gson;
    private Type listTypeSkiArea;

    private SessionManager(Context context) {
        synchronized (SessionManager.class) {
            sharedPreferences = context.getSharedPreferences(GLOBAL_PREFS, Context.MODE_PRIVATE);

            gson = new Gson();
            listTypeSkiArea = new TypeToken<List<SkiArea>>() {}.getType();
        }
    }

    public static void createInstance(Context context) {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager(context);
                }
            }
        }
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString(KEY_PW, password).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PW, "");
    }

    public void setSkiAreas(List<SkiArea> skiAreaList) {
        sharedPreferences.edit().putString(KEY_SKI_AREAS, gson.toJson(skiAreaList)).apply();
    }

    public List<SkiArea> getSkiAreas() {
        return gson.fromJson(sharedPreferences.getString(KEY_SKI_AREAS, ""), listTypeSkiArea);
    }

    public boolean isLoggedIn() {
        return !SessionManager.getInstance().getUsername().isEmpty() && !SessionManager.getInstance().getPassword().isEmpty();
    }

    public void clearUserData() {
        setPassword("");
        setUsername("");
        SkiService.getInstance().reset();

        UserManager.getInstance().resetUser();
    }

}
