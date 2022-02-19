package com.gazman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteStatement;

public class DbProxy implements SupportSQLiteDatabase {
    public static final int CONFLICT_IGNORE = SQLiteDatabase.CONFLICT_IGNORE;
    public static final int CONFLICT_REPLACE = SQLiteDatabase.CONFLICT_REPLACE;
    public static @Nullable
    QueryLogger queryLogger;
    private final SQLiteDatabase db;

    public DbProxy(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public SQLiteStatement compileStatement(String sql) {
        return db.compileStatement(sql);
    }

    @Override
    public void beginTransaction() {
        db.beginTransaction();
    }

    @Override
    public void beginTransactionNonExclusive() {
        db.beginTransactionNonExclusive();
    }

    @Override
    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        db.beginTransactionWithListener(transactionListener);
    }

    @Override
    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        db.beginTransactionWithListenerNonExclusive(transactionListener);
    }

    @Override
    public void endTransaction() {
        db.endTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    @Override
    public boolean inTransaction() {
        return db.inTransaction();
    }

    @Override
    public boolean isDbLockedByCurrentThread() {
        return db.isDbLockedByCurrentThread();
    }

    @Override
    public boolean yieldIfContendedSafely() {
        return db.yieldIfContendedSafely();
    }

    @Override
    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return db.yieldIfContendedSafely(sleepAfterYieldDelay);
    }

    @Override
    public int getVersion() {
        return db.getVersion();
    }

    @Override
    public void setVersion(int version) {
        db.setVersion(version);
    }

    @Override
    public long getMaximumSize() {
        return db.getMaximumSize();
    }

    @Override
    public long setMaximumSize(long numBytes) {
        return db.setMaximumSize(numBytes);
    }

    @Override
    public long getPageSize() {
        return db.getPageSize();
    }

    @Override
    public void setPageSize(long numBytes) {
        db.setPageSize(numBytes);
    }

    @Override
    public Cursor query(String query) {
        log(query);
        return db.query(query);
    }

    @Override
    public Cursor query(String query, Object[] bindArgs) {
        log(query);
        return db.query(query, bindArgs);
    }

    @Override
    public Cursor query(SupportSQLiteQuery query) {
        return db.query(query);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Cursor query(SupportSQLiteQuery query, CancellationSignal cancellationSignal) {
        return db.query(query, cancellationSignal);
    }

    @Override
    public long insert(String table, int conflictAlgorithm, ContentValues values) throws SQLException {
        return db.insert(table, conflictAlgorithm, values);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return db.insert(table, nullColumnHack, values);
    }

    @Override
    public int delete(String table, String whereClause, Object[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    @Override
    public int update(String table, int conflictAlgorithm, ContentValues values, String whereClause, Object[] whereArgs) {
        return db.update(table, conflictAlgorithm, values, whereClause, whereArgs);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    @Override
    public void execSQL(String query) throws SQLException {
        log(query);
        db.execSQL(query);
    }

    @Override
    public void execSQL(String query, Object[] bindArgs) throws SQLException {
        log(query);
        db.execSQL(query);
    }

    private void log(String query) {
        QueryLogger queryLogger = DbProxy.queryLogger;
        if (queryLogger != null) {
            queryLogger.onQuery(query);
        }
    }

    @Override
    public boolean isReadOnly() {
        return db.isReadOnly();
    }

    @Override
    public boolean isOpen() {
        return db.isOpen();
    }

    @Override
    public boolean needUpgrade(int newVersion) {
        return db.needUpgrade(newVersion);
    }

    @Override
    public String getPath() {
        return db.getPath();
    }

    @Override
    public void setLocale(Locale locale) {
        db.setLocale(locale);
    }

    @Override
    public void setMaxSqlCacheSize(int cacheSize) {
        db.setMaxSqlCacheSize(cacheSize);
    }

    @Override
    public void setForeignKeyConstraintsEnabled(boolean enable) {
        db.setForeignKeyConstraintsEnabled(enable);
    }

    @Override
    public boolean enableWriteAheadLogging() {
        return db.enableWriteAheadLogging();
    }

    @Override
    public void disableWriteAheadLogging() {
        db.disableWriteAheadLogging();
    }

    @Override
    public boolean isWriteAheadLoggingEnabled() {
        return db.isWriteAheadLoggingEnabled();
    }

    @Override
    public List<Pair<String, String>> getAttachedDbs() {
        return db.getAttachedDbs();
    }

    @Override
    public boolean isDatabaseIntegrityOk() {
        return db.isDatabaseIntegrityOk();
    }

    @Override
    public void close() throws IOException {
        db.close();
    }

    public void insertWithOnConflict(String tableName, String nullColumnHack, ContentValues values, int conflictIgnore) {
        db.insertWithOnConflict(tableName, nullColumnHack, values, conflictIgnore);
    }

    public Cursor rawQuery(String sql, Object[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public interface QueryLogger {
        void onQuery(String query);
    }
}
