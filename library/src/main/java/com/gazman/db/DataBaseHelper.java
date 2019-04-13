package com.gazman.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by Ilya Gazman on 7/9/2016.
 */
class DataBaseHelper extends SQLiteOpenHelper {
    private String dataBasePath;
    private final String dataBaseName;
    private final Context context;

    @Nullable
    UpgradeCallback upgradeCallback;
    Runnable buildCallback;
    private String assetsPath;

    DataBaseHelper(Context context, String dataBaseName, int version, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler error) {
        super(context, dataBaseName, factory, version, error);
        this.context = context;
        this.dataBaseName = dataBaseName;
    }

    public void load() {
        String path = context.getApplicationInfo().dataDir;
        dataBasePath = path + "/databases/";
        boolean isDataBaseExist = new File(dataBasePath + dataBaseName).exists();
        if (!isDataBaseExist) {
            createDataBase();
            if (buildCallback != null) {
                buildCallback.run();
            }
        }
        invokeUpgrade();
    }

    private void invokeUpgrade() {
        getReadableDatabase();
    }

    public void setAssetsPath(@NonNull String assetsPath) {
        if (assetsPath.startsWith("/")) {
            assetsPath = assetsPath.substring(1);
        }
        if (!assetsPath.endsWith("/")) {
            assetsPath += "/";
        }
        this.assetsPath = assetsPath;
    }

    public void setUpgradeCallback(@Nullable UpgradeCallback upgradeCallback) {
        this.upgradeCallback = upgradeCallback;
    }

    public void setBuildCallback(Runnable buildCallback) {
        this.buildCallback = buildCallback;
    }

    private void createDataBase() {
        getReadableDatabase();
        close();
        copyDataBase();
    }

    private void copyDataBase() {
        String assetsPath = this.assetsPath + dataBaseName + ".db";
        String outFileName = dataBasePath + dataBaseName;
        try (InputStream input = context.getAssets().open(assetsPath);
             OutputStream output = new FileOutputStream(outFileName)) {
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);
            while (length != -1) {
                if (length > 0) {
                    output.write(buffer, 0, length);
                }
                length = input.read(buffer);
            }
        } catch (IOException e) {
            Crashlytics.logException(e);
            throw new Error("Database not found in assets " + outFileName);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (upgradeCallback != null) {
            DBThread.execute(() -> upgradeCallback.onUpgrade(db, oldVersion, newVersion));
        }
    }


}
