package com.gazman.db

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteTransactionListener
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteStatement
import io.requery.android.database.sqlite.SQLiteDatabase
import java.io.IOException
import java.util.*

class DbProxy(
    private val db: SupportSQLiteDatabase
) : SupportSQLiteDatabase {

    override val attachedDbs = db.attachedDbs
    override val isDatabaseIntegrityOk = db.isDatabaseIntegrityOk
    override val isDbLockedByCurrentThread = db.isDbLockedByCurrentThread
    override val isOpen = db.isOpen
    override val isReadOnly = db.isReadOnly
    override val isWriteAheadLoggingEnabled = db.isWriteAheadLoggingEnabled
    override val maximumSize = db.maximumSize
    override var pageSize = db.pageSize
    override val path = db.path
    override var version = db.version


    override fun compileStatement(sql: String): SupportSQLiteStatement {
        return db.compileStatement(sql)
    }

    override fun delete(table: String, whereClause: String?, whereArgs: Array<out Any?>?) =
        db.delete(table, whereClause, whereArgs)

    override fun beginTransaction() {
        db.beginTransaction()
    }

    override fun beginTransactionNonExclusive() {
        db.beginTransactionNonExclusive()
    }

    override fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener) {
        db.beginTransactionWithListener(transactionListener)
    }

    override fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener) {
        db.beginTransactionWithListenerNonExclusive(transactionListener)
    }

    override fun endTransaction() {
        db.endTransaction()
    }

    override fun setTransactionSuccessful() {
        db.setTransactionSuccessful()
    }

    override fun update(
        table: String,
        conflictAlgorithm: Int,
        values: ContentValues,
        whereClause: String?,
        whereArgs: Array<out Any?>?
    ) = db.update(table, conflictAlgorithm, values, whereClause, whereArgs)

    override fun inTransaction(): Boolean {
        return db.inTransaction()
    }

    override fun yieldIfContendedSafely(): Boolean {
        return db.yieldIfContendedSafely()
    }

    override fun yieldIfContendedSafely(sleepAfterYieldDelayMillis: Long): Boolean {
        return db.yieldIfContendedSafely(sleepAfterYieldDelayMillis)
    }

    override fun setMaximumSize(numBytes: Long): Long {
        return db.setMaximumSize(numBytes)
    }

    override fun query(query: String): Cursor {
        log(query)
        return db.query(query)
    }

    override fun query(query: String, bindArgs: Array<out Any?>): Cursor {
        log(query)
        return db.query(query, bindArgs)
    }

    override fun query(query: SupportSQLiteQuery): Cursor {
        return db.query(query)
    }

    override fun query(query: SupportSQLiteQuery, cancellationSignal: CancellationSignal?) =
        db.query(query, cancellationSignal)

    @Throws(SQLException::class)
    override fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return db.insert(table, conflictAlgorithm, values)
    }

    fun insert(table: String, values: ContentValues): Long {
        return db.insert(table, SQLiteDatabase.CONFLICT_NONE, values)
    }

    fun delete(table: String) = db.delete(table, null, null)
    fun delete(table: String, whereClause: String?) = db.delete(table, whereClause, null)

    fun update(
        table: String,
        values: ContentValues,
        whereClause: String?,
        whereArgs: Array<String?>?
    ): Int {
        return db.update(table, SQLiteDatabase.CONFLICT_NONE, values, whereClause, whereArgs)
    }

    @Throws(SQLException::class)
    override fun execSQL(sql: String) {
        log(sql)
        db.execSQL(sql)
    }

    override fun execSQL(sql: String, bindArgs: Array<out Any?>) {
        db.execSQL(sql, bindArgs)
    }

    private fun log(query: String) {
        queryLogger?.onQuery(query)
    }

    override fun needUpgrade(newVersion: Int): Boolean {
        return db.needUpgrade(newVersion)
    }

    override fun setLocale(locale: Locale) {
        db.setLocale(locale)
    }

    override fun setMaxSqlCacheSize(cacheSize: Int) {
        db.setMaxSqlCacheSize(cacheSize)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun setForeignKeyConstraintsEnabled(enabled: Boolean) {
        db.setForeignKeyConstraintsEnabled(enabled)
    }

    override fun enableWriteAheadLogging(): Boolean {
        return db.enableWriteAheadLogging()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun disableWriteAheadLogging() {
        db.disableWriteAheadLogging()
    }

    @Throws(IOException::class)
    override fun close() {
        db.close()
    }

    fun insertWithOnConflict(
        tableName: String,
        values: ContentValues,
        conflictIgnore: Int
    ) {
        db.insert(tableName, conflictIgnore, values)
    }

    fun rawQuery(sql: String, selectionArgs: Array<Any?>? = null): Cursor {
        log(sql)
        return if (selectionArgs != null) {
            db.query(sql, selectionArgs)
        } else {
            db.query(sql)
        }
    }

    fun interface QueryLogger {
        fun onQuery(query: String?)
    }

    companion object {
        const val CONFLICT_IGNORE = SQLiteDatabase.CONFLICT_IGNORE
        const val CONFLICT_REPLACE = SQLiteDatabase.CONFLICT_REPLACE
        var queryLogger: QueryLogger? = null
    }
}