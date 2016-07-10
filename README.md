# SQLite-Assets-Helper
SQLite helper library.
 
 - Load database from file
 - Synchronized access to the database
 
# Usage

Clone the repo and add the library module to your project. 
It's better that you fork it first, so you can send pull requests latter.

**Step 1**
Add _myDb.db_ file to _yourProject/src/main/assets_

**Step 2**
Create DataBase, note the commented out optional settings.

```java
dataBase = new DataBase.Builder(context, "myDb").
//        setAssetsPath(). // default "databases"
//        setDatabaseErrorHandler().
//        setCursorFactory().
//        setUpgradeCallback()
//        setVersion(). // default 1
build();
```

**Step 3**
Write your queries

```java
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
```

You can also use the _CursorList_, it extend the _ArrayList_ and add some performance boost,
It will prevent you from _calling cursor.getColumnIndex("first_name")_ more than once.

```Java
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
```

See the sample module for the full example

# License 
Apache License Version 2.0, January 2004
http://www.apache.org/licenses/
