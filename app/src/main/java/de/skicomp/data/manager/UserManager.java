package de.skicomp.data.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;

import de.skicomp.SessionManager;
import de.skicomp.data.DatabaseManager;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.events.user.UserEventSuccess;
import de.skicomp.models.User;
import de.skicomp.network.SkiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 18.05.17.
 */

public class UserManager implements Callback<User> {

    private static final String TAG = UserManager.class.getSimpleName();

    private static UserManager instance;

    private final Context context;
    private User user;

    private UserManager(@NonNull final Context context) {
        this.context = context;
    }

    public static UserManager createInstance(@NonNull final Context context) {
        synchronized (UserManager.class) {
            if (UserManager.instance == null) {
                UserManager.instance = new UserManager(context);
            }
        }
        return UserManager.instance;
    }

    public static UserManager getInstance() {
        synchronized (UserManager.class) {
            return UserManager.instance;
        }
    }

    //----------------------------------------------------------------------------------------------

    public void loadUser(String username, String password) {
        SkiService.getInstance().getUser(this, username, password);
    }

    public void updateUser(User user) {
        SkiService.getInstance().updateUser(this, SessionManager.getInstance().getUsername(), user);
    }

    public void registerUser(User user) {
        SkiService.getInstance().register(this, user);
    }

    public void setUser(User user) {
        this.user = user;
        saveUserInDatabase();
    }

    public User getUser() {
        if (user == null) {
            user = loadUserFromDatabase();
        }
        return user;
    }

    public void resetUser() {
        DatabaseManager.getInstance().clearTable(User.class);
    }

    //----------------------------------------------------------------------------------------------

    public User loadUserFromDatabase() {
        try {
            return DatabaseManager.getInstance().getUserDao().queryForAll().get(0);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - loadUserFromDatabase", e);
            return null;
        }
    }

    private void saveUserInDatabase() {
        try {
            DatabaseManager.getInstance().getUserDao().createOrUpdate(user);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - saveUserInDatabase", e);
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
            setUser(response.body());
            EventBus.getDefault().post(new UserEventSuccess());
        } else {
            EventBus.getDefault().post(new UserEventFailure(response));
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        EventBus.getDefault().post(new UserEventFailure(null));
    }
}
