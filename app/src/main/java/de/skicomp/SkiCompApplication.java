package de.skicomp;

import android.app.Application;

import de.skicomp.data.DatabaseManager;
import de.skicomp.data.manager.FriendManager;
import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.data.manager.UserManager;
import de.skicomp.network.SkiService;
import de.skicomp.network.WeatherService;
import de.skicomp.utils.ProgressDialogManager;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiCompApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Data
        SessionManager.createInstance(this);
        DatabaseManager.createInstance(this);
        UserManager.createInstance(this);
        SkiAreaManager.createInstance(this);
        FriendManager.createInstance(this);

        // Network
        SkiService.createInstance();
        WeatherService.createInstance();

        // Helper
        ProgressDialogManager.createInstance(this);
    }

}
