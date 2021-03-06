package de.skicomp.data.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;

import de.skicomp.data.DatabaseManager;
import de.skicomp.events.skiarea.SkiAreaEventFailure;
import de.skicomp.events.skiarea.SkiAreaEventSuccess;
import de.skicomp.models.SkiArea;
import de.skicomp.network.SkiService;
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
