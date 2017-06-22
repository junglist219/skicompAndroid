package de.skicomp.data.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.skicomp.SessionManager;
import de.skicomp.data.DatabaseManager;
import de.skicomp.events.skiarea.SkiAreaEventFailure;
import de.skicomp.events.skiarea.SkiAreaEventSuccess;
import de.skicomp.models.SkiArea;
import de.skicomp.network.SkiService;
import de.skicomp.utils.helper.SkiAreaHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 18.05.17.
 */

public class SkiAreaManager implements Callback<List<SkiArea>> {

    private static final String TAG = SkiAreaManager.class.getSimpleName();

    private static SkiAreaManager instance;

    private final Context context;
    private List<SkiArea> skiAreaList;

    private SkiAreaManager(@NonNull final Context context) {
        this.context = context;
    }

    public static SkiAreaManager createInstance(@NonNull final Context context) {
        synchronized (SkiAreaManager.class) {
            if (SkiAreaManager.instance == null) {
                SkiAreaManager.instance = new SkiAreaManager(context);
            }
        }
        return SkiAreaManager.instance;
    }

    public static SkiAreaManager getInstance() {
        synchronized (SkiAreaManager.class) {
            return SkiAreaManager.instance;
        }
    }

    //----------------------------------------------------------------------------------------------

    public void loadSkiAreas() {
        SkiService.getInstance().getSkiAreas(this);
    }

    public void setSkiAreas(List<SkiArea> skiAreaList) {
        this.skiAreaList = skiAreaList;
        saveSkiAreasInDatabase();
    }

    public List<SkiArea> getSkiAreas() {
        if (skiAreaList == null) {
            skiAreaList = loadSkiAreasFromDatabase();
        }
        return skiAreaList;
    }

    public void resetSkiAreas() {
        DatabaseManager.getInstance().clearTable(SkiArea.class);
    }

    //----------------------------------------------------------------------------------------------
    public List<SkiArea> getFavoriteSkiAreas() {
        List<SkiArea> skiAreaFavoritesList = SessionManager.getInstance().getSkiAreaFavorites(UserManager.getInstance().getUser().getId());
        if (skiAreaFavoritesList == null) {
            skiAreaFavoritesList = new ArrayList<>();
        }
        return skiAreaFavoritesList;
    }

    public void addSkiAreaToFavorites(SkiArea skiArea) {
        if (!SkiAreaHelper.favoritesContainsSkiArea(skiArea)) {
            List<SkiArea> skiAreaFavorites = getFavoriteSkiAreas();
            skiAreaFavorites.add(skiArea);
            SessionManager.getInstance().setSkiAreaFavorites(UserManager.getInstance().getUser().getId(), skiAreaFavorites);
        }
    }

    public void removeSkiAreaFromFavorites(SkiArea skiArea) {
        List<SkiArea> skiAreaFavorites = getFavoriteSkiAreas();
        Iterator<SkiArea> iter = skiAreaFavorites.iterator();

        while (iter.hasNext()) {
            SkiArea favoriteSkiArea = iter.next();
            if (favoriteSkiArea.getId() == skiArea.getId()) {
                iter.remove();
            }
        }

        SessionManager.getInstance().setSkiAreaFavorites(UserManager.getInstance().getUser().getId(), skiAreaFavorites);
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------

    public List<SkiArea> loadSkiAreasFromDatabase() {
        try {
            return DatabaseManager.getInstance().getSkiAreaDao().queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - loadSkiAreaFromDatabase", e);
            return null;
        }
    }

    private void saveSkiAreasInDatabase() {
        try {
            for (SkiArea skiArea : skiAreaList) {
                DatabaseManager.getInstance().getSkiAreaDao().createOrUpdate(skiArea);
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException - saveSkiAreaInDatabase", e);
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onResponse(Call<List<SkiArea>> call, Response<List<SkiArea>> response) {
        if (response.isSuccessful()) {
            setSkiAreas(response.body());
            EventBus.getDefault().post(new SkiAreaEventSuccess());
        } else {
            EventBus.getDefault().post(new SkiAreaEventFailure(response));
        }
    }

    @Override
    public void onFailure(Call<List<SkiArea>> call, Throwable t) {
        EventBus.getDefault().post(new SkiAreaEventFailure(null));
    }
    
}
