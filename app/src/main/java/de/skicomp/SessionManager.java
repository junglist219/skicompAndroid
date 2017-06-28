package de.skicomp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.skicomp.data.manager.FriendManager;
import de.skicomp.data.manager.UserManager;
import de.skicomp.models.SkiArea;
import de.skicomp.network.SkiService;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SessionManager {

    private static final String GLOBAL_PREFS = "global_";
    private static final String USER_PREFS = "user_";

    private static final String KEY_USERNAME = "keyUsername";
    private static final String KEY_PW = "keyPW";

    private static final String KEY_SKI_AREA_FAVORITES = "keySkiAreaFavorites";

    private static SessionManager instance;

    private SharedPreferences globalPrefs;
    private SharedPreferences userPrefs;

    private Gson gson;
    private Type listTypeSkiArea;

    private SessionManager(Context context) {
        synchronized (SessionManager.class) {
            globalPrefs = context.getSharedPreferences(GLOBAL_PREFS, Context.MODE_PRIVATE);
            userPrefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

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
        userPrefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return userPrefs.getString(KEY_USERNAME, "");
    }

    public void setPassword(String password) {
        userPrefs.edit().putString(KEY_PW, password).apply();
    }

    public String getPassword() {
        return userPrefs.getString(KEY_PW, "");
    }

    public void setSkiAreaFavorites(int userID, List<SkiArea> skiAreaList) {
        globalPrefs.edit().putString(KEY_SKI_AREA_FAVORITES.concat(String.valueOf(userID)), gson.toJson(skiAreaList)).apply();
    }

    public List<SkiArea> getSkiAreaFavorites(int userID) {
        return gson.fromJson(globalPrefs.getString(KEY_SKI_AREA_FAVORITES.concat(String.valueOf(userID)), ""), listTypeSkiArea);
    }

    public boolean isLoggedIn() {
        return !SessionManager.getInstance().getUsername().isEmpty() && !SessionManager.getInstance().getPassword().isEmpty();
    }

    public void clearUserData() {
        userPrefs.edit().clear().apply();
        SkiService.getInstance().reset();
        UserManager.getInstance().resetUser();
        FriendManager.getInstance().resetFriends();
    }

}
