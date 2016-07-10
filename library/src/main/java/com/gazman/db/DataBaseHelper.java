package com.gazman.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.requery.android.database.DatabaseErrorHandler;
import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
class DataBaseHelper extends SQLiteOpenHelper {
    private String dataBasePath;
    private final String dataBaseName;
    private final Context context;
    private
    @Nullable
    UpgradeCallback callback;
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
        }
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

    public void setCallback(@Nullable UpgradeCallback callback) {
        this.callback = callback;
    }

    private void createDataBase() {
        getReadableDatabase();
        close();
        copyDataBase();
    }

    private void copyDataBase() {
        InputStream input;
        String assetsPath = this.assetsPath + dataBaseName + ".db";
        try {
            input = context.getAssets().open(assetsPath);
        } catch (IOException e) {
            throw new Error("Failed open DB " + assetsPath);
        }
        String outFileName = dataBasePath + dataBaseName;
        OutputStream output;
        try {
            output = new FileOutputStream(outFileName);
        } catch (FileNotFoundException e) {
            throw new Error("Database not found in assets " + outFileName);
        }
        byte[] buffer = new byte[1024];
        try {
            int length = input.read(buffer);
            while (length > 0) {
                output.write(buffer, 0, length);
                length = input.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public SQLiteDatabase openDataBase() {
//        String mPath = dataBasePath + dataBaseName;
//        return SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (callback != null) {
            callback.onUpgrade(db, oldVersion, newVersion);
        }
    }


}
