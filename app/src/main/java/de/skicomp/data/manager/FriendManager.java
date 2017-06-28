package de.skicomp.data.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;

import de.skicomp.SessionManager;
import de.skicomp.data.DatabaseManager;
import de.skicomp.events.friend.FriendEventFailure;
import de.skicomp.events.friend.FriendEventSuccess;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.models.Friend;
import de.skicomp.network.SkiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendManager implements Callback<List<Friend>> {

    private static final String TAG = FriendManager.class.getSimpleName();

    private static FriendManager instance;

    private final Context context;
    private List<Friend> friends;

    private FriendManager(@NonNull final Context context) {
        this.context = context;
    }

    public static FriendManager createInstance(@NonNull final Context context) {
        synchronized (FriendManager.class) {
            if (FriendManager.instance == null) {
                FriendManager.instance = new FriendManager(context);
            }
        }
        return FriendManager.instance;
    }

    public static FriendManager getInstance() {
        synchronized (FriendManager.class) {
            return FriendManager.instance;
        }
    }

    //----------------------------------------------------------------------------------------------

    public void loadFriends() {
        SkiService.getInstance().getFriends(this, SessionManager.getInstance().getUsername());
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        saveFriendsInDatabase();
    }

    public List<Friend> getFriends() {
        if (friends == null) {
            friends = loadFriendsFromDatabase();
        }
        return friends;
    }

    public void resetFriends() {
        DatabaseManager.getInstance().clearTable(Friend.class);
    }

    //----------------------------------------------------------------------------------------------

    public List<Friend> loadFriendsFromDatabase() {
        try {
            return DatabaseManager.getInstance().getFriendDao().queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - loadFriendsFromDatabase", e);
            return null;
        }
    }

    private void saveFriendsInDatabase() {
        try {
            for (Friend friend : friends) {
                DatabaseManager.getInstance().getFriendDao().createOrUpdate(friend);
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - saveFriendsInDatabase", e);
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
        if (response.isSuccessful()) {
            setFriends(response.body());
            EventBus.getDefault().post(new FriendEventSuccess());
        } else {
            EventBus.getDefault().post(new FriendEventFailure(response));
        }
    }

    @Override
    public void onFailure(Call<List<Friend>> call, Throwable t) {
        EventBus.getDefault().post(new UserEventFailure(null));
    }

}
