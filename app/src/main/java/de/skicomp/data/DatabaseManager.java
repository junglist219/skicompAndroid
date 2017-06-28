package de.skicomp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.skicomp.models.Friend;
import de.skicomp.models.SkiArea;
import de.skicomp.models.User;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseManager.class.getSimpleName();

    private static final String DB_NAME = "skicomp.db";
    private static final int DB_VERSION = 1;

    private static DatabaseManager instance;

    private Dao<User, Integer> userDao;
    private Dao<SkiArea, Integer> skiAreaDao;
    private Dao<Friend, Integer> friendDao;

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        connectionSource = new AndroidConnectionSource(this);
    }

    public static DatabaseManager createInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(context);
                }
            }
        }

        return instance;
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(TAG, "create tables");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, SkiArea.class);
            TableUtils.createTable(connectionSource, Friend.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d(TAG, "update tables");
    }

    //---------

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
                Log.e(TAG, "could not create user DAO", e);
            }
        }
        return userDao;
    }

    public Dao<SkiArea, Integer> getSkiAreaDao() {
        if (skiAreaDao == null) {
            try {
                skiAreaDao = getDao(SkiArea.class);
            } catch (SQLException e) {
                Log.e(TAG, "could not create skiarea DAO", e);
            }
        }
        return skiAreaDao;
    }

    public Dao<Friend, Integer> getFriendDao() {
        if (friendDao == null) {
            try {
                friendDao = getDao(Friend.class);
            } catch (SQLException e) {
                Log.e(TAG, "could not create friend DAO", e);
            }
        }
        return friendDao;
    }

    //---------

    public void clearTable(Class clazz) {
        try {
            TableUtils.clearTable(connectionSource, clazz);
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't clear table " + clazz.getSimpleName(), e);
        }
    }
}
