package com.gazman.assetsdb;

import android.content.Context;
import android.database.Cursor;

import com.gazman.db.CursorList;
import com.gazman.db.DataBase;
import com.gazman.db.DataBaseQueryCallback;
import com.gazman.db.callbacks.MainThreadCallback;

import java.util.ArrayList;

import io.requery.android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public class UsersDataBase {

    private final DataBase dataBase;

    public UsersDataBase(Context context) {
        dataBase = new DataBase.Builder(context, "myDb").
//                setAssetsPath(). // default "databases"
//                setDatabaseErrorHandler().
//                setCursorFactory().
//                setUpgradeCallback()
//                setVersion(). // default 1
        build();

    }

    public void getUsers1(final MainThreadCallback<ArrayList<UserData>> callback) {
        dataBase.getReadableDatabase(new DataBaseQueryCallback() {
            @Override
            public void onQuery(SQLiteDatabase db) {
                ArrayList<UserData> list = new ArrayList<>();

                Cursor cursor = db.rawQuery("select * from users", null);
                if(cursor != null && cursor.moveToFirst()){
                    do {
                        int firstNameIndex = cursor.getColumnIndex("first_name");
                        int lastNameIndex = cursor.getColumnIndex("last_name");
                        UserData userData = new UserData();
                        userData.firstName = cursor.getString(firstNameIndex);
                        userData.lastName = cursor.getString(lastNameIndex);
                        list.add(userData);
                    }while (cursor.moveToNext());
                }
                callback.setResponse(list);
            }
        });
    }

    public void getUsers2(final MainThreadCallback<ArrayList<UserData>> callback) {
        dataBase.getReadableDatabase(new DataBaseQueryCallback() {
            @Override
            public void onQuery(SQLiteDatabase db) {
                Cursor cursor = db.rawQuery("select * from users", null);
                ArrayList<UserData> users = new CursorList<UserData>(cursor) {

                    public int lastNameIndex;
                    private int firstNameIndex;

                    @Override
                    protected void initColumnIndexes(Cursor cursor) {
                        firstNameIndex = cursor.getColumnIndex("first_name");
                        lastNameIndex = cursor.getColumnIndex("last_name");
                    }

                    @Override
                    protected UserData processRaw(Cursor cursor) {
                        UserData userData = new UserData();
                        userData.firstName = cursor.getString(firstNameIndex);
                        userData.lastName = cursor.getString(lastNameIndex);
                        return userData;
                    }
                };

                callback.setResponse(users);
                cursor.close();
            }
        });
    }

    public void close(){
        dataBase.close();
    }
}

