package com.example.orhan_ucar_odev7.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.orhan_ucar_odev7.models.Note

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "note.db"
        private const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DETAIL = "detail"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_DETAIL TEXT, $COLUMN_DATE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNoteToDatabase(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_DETAIL, note.detail)
        contentValues.put(COLUMN_DATE, note.date)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun updateNoteInDatabase(note: Note): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_DETAIL, note.detail)
        contentValues.put(COLUMN_DATE, note.date)
        val rowsAffected = db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
        return rowsAffected > 0
    }

    fun deleteNoteFromDatabase(note: Note) {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun getAllNotes(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}
