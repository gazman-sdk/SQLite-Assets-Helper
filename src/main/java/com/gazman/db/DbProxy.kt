package com.gazman.db

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteTransactionListener
import android.os.Build
import android.os.CancellationSignal
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteStatement
import java.io.IOException
import java.util.*

class DbProxy(private val db: SQLiteDatabase) : SupportSQLiteDatabase {
    override fun compileStatement(sql: String): SQLiteStatement {
        return db.compileStatement(sql)
    }

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

    override fun inTransaction(): Boolean {
        return db.inTransaction()
    }

    override fun isDbLockedByCurrentThread(): Boolean {
        return db.isDbLockedByCurrentThread
    }

    override fun yieldIfContendedSafely(): Boolean {
        return db.yieldIfContendedSafely()
    }

    override fun yieldIfContendedSafely(sleepAfterYieldDelay: Long): Boolean {
        return db.yieldIfContendedSafely(sleepAfterYieldDelay)
    }

    override fun getVersion(): Int {
        return db.version
    }

    override fun setVersion(version: Int) {
        db.version = version
    }

    override fun getMaximumSize(): Long {
        return db.maximumSize
    }

    override fun setMaximumSize(numBytes: Long): Long {
        return db.setMaximumSize(numBytes)
    }

    override fun getPageSize(): Long {
        return db.pageSize
    }

    override fun setPageSize(numBytes: Long) {
        db.pageSize = numBytes
    }

    override fun query(query: String): Cursor {
        log(query)
        return db.query(query)
    }

    override fun query(query: String, bindArgs: Array<Any>): Cursor {
        log(query)
        return db.query(query, bindArgs)
    }

    override fun query(query: SupportSQLiteQuery): Cursor {
        return db.query(query)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun query(query: SupportSQLiteQuery, cancellationSignal: CancellationSignal): Cursor {
        return db.query(query, cancellationSignal)
    }

    @Throws(SQLException::class)
    override fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return db.insert(table, conflictAlgorithm, values)
    }

    fun insert(table: String?, nullColumnHack: String?, values: ContentValues?): Long {
        return db.insert(table, nullColumnHack, values)
    }

    override fun delete(table: String, whereClause: String, whereArgs: Array<Any>?): Int {
        return db.delete(table, whereClause, whereArgs)
    }

    override fun update(
        table: String,
        conflictAlgorithm: Int,
        values: ContentValues,
        whereClause: String,
        whereArgs: Array<Any>
    ): Int {
        return db.update(table, conflictAlgorithm, values, whereClause, whereArgs)
    }

    fun update(
        table: String?,
        values: ContentValues?,
        whereClause: String?,
        whereArgs: Array<String?>?
    ): Int {
        return db.update(table, values, whereClause, whereArgs)
    }

    @Throws(SQLException::class)
    override fun execSQL(query: String) {
        log(query)
        db.execSQL(query)
    }

    @Throws(SQLException::class)
    override fun execSQL(query: String, bindArgs: Array<Any?>) {
        log(query)
        db.execSQL(query)
    }

    private fun log(query: String) {
        val queryLogger = queryLogger
        queryLogger?.onQuery(query)
    }

    override fun isReadOnly(): Boolean {
        return db.isReadOnly
    }

    override fun isOpen(): Boolean {
        return db.isOpen
    }

    override fun needUpgrade(newVersion: Int): Boolean {
        return db.needUpgrade(newVersion)
    }

    override fun getPath(): String {
        return db.path
    }

    override fun setLocale(locale: Locale) {
        db.setLocale(locale)
    }

    override fun setMaxSqlCacheSize(cacheSize: Int) {
        db.setMaxSqlCacheSize(cacheSize)
    }

    override fun setForeignKeyConstraintsEnabled(enable: Boolean) {
        db.setForeignKeyConstraintsEnabled(enable)
    }

    override fun enableWriteAheadLogging(): Boolean {
        return db.enableWriteAheadLogging()
    }

    override fun disableWriteAheadLogging() {
        db.disableWriteAheadLogging()
    }

    override fun isWriteAheadLoggingEnabled(): Boolean {
        return db.isWriteAheadLoggingEnabled
    }

    override fun getAttachedDbs(): List<Pair<String, String>> {
        return db.attachedDbs
    }

    override fun isDatabaseIntegrityOk(): Boolean {
        return db.isDatabaseIntegrityOk
    }

    @Throws(IOException::class)
    override fun close() {
        db.close()
    }

    fun insertWithOnConflict(
        tableName: String?,
        nullColumnHack: String?,
        values: ContentValues?,
        conflictIgnore: Int
    ) {
        db.insertWithOnConflict(tableName, nullColumnHack, values, conflictIgnore)
    }

    fun rawQuery(sql: String?, selectionArgs: Array<Any?>?): Cursor {
        return db.rawQuery(sql, selectionArgs)
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