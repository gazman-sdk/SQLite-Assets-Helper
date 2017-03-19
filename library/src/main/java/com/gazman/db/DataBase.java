package com.gazman.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;

import io.requery.android.database.DatabaseErrorHandler;
import io.requery.android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public class DataBase {

    private DataBaseHelper helper;

    void init(DataBaseHelper helper){
        this.helper = helper;
    }

    public void getReadableDatabase(final DataBaseQueryCallback queryCallback){
        DBThread.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                queryCallback.onQuery(helper.getReadableDatabase());
            }
        });
    }

    public void getWritableDatabase(final DataBaseQueryCallback queryCallback){
        DBThread.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                queryCallback.onQuery(helper.getWritableDatabase());
            }
        });
    }

    public void makeTransaction(final DataBaseQueryCallback queryCallback){
        DBThread.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    db.beginTransaction();
                    queryCallback.onQuery(db);
                    db.setTransactionSuccessful();
                }
                catch (Exception e){
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                finally {
                    db.endTransaction();
                }
            }
        });
    }

    public void close(){
        DBThread.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                helper.close();
            }
        });
    }

    public static class Builder {
        private final Context context;
        private final String dataBaseName;
        private String assetsPath = "databases";
        private SQLiteDatabase.CursorFactory cursorFactory;
        private DatabaseErrorHandler databaseErrorHandler;
        private int version = 1;
        private UpgradeCallback upgradeCallback;
        private Runnable buildCallback;

        /**
         * Creates new database with default assetsPath: "databases"
         */
        public Builder(Context context, String dataBaseName) {
            this.context = context;
            this.dataBaseName = dataBaseName;
        }

        public Builder setAssetsPath(@NonNull String assetsPath) {
            this.assetsPath = assetsPath;
            return this;
        }

        public Builder setBuildCallback(Runnable buildCallback) {
            this.buildCallback = buildCallback;
            return this;
        }

        public Builder setCursorFactory(SQLiteDatabase.CursorFactory cursorFactory) {
            this.cursorFactory = cursorFactory;
            return this;
        }

        public Builder setDatabaseErrorHandler(DatabaseErrorHandler databaseErrorHandler) {
            this.databaseErrorHandler = databaseErrorHandler;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setUpgradeCallback(UpgradeCallback upgradeCallback) {
            this.upgradeCallback = upgradeCallback;
            return this;
        }

        public DataBase build() {
            final DataBase dataBase = new DataBase();
            DBThread.EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    DataBaseHelper helper = new DataBaseHelper(context, dataBaseName, version, cursorFactory, databaseErrorHandler);
                    helper.setAssetsPath(assetsPath);
                    helper.setUpgradeCallback(upgradeCallback);
                    helper.setBuildCallback(buildCallback);
                    helper.load();
                    dataBase.init(helper);
                }
            });
            return dataBase;
        }
    }
}
