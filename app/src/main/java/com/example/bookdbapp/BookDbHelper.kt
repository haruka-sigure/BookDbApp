package com.example.bookdbapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.util.concurrent.Flow.Publisher

const val DB_FILE="book.db"
const val BOOK_TABLE="books"
const val ID="id"
const val BOOK_TITLE="book_title"
const val BOOK_AUTHOR="book_author"
const val BOOK_PUBLISHER="book_publisher"

class BookDbHelper (private val context: Context):
    SQLiteOpenHelper(context, DB_FILE,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateTable="CREATE TABLE $BOOK_TABLE($ID integer primary key,$BOOK_TITLE nvarcher(30),$BOOK_AUTHOR nvarcher(20),$BOOK_PUBLISHER nvarcher(50));"
        db?.execSQL(sqlCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion!=newVersion){
            db?.execSQL("DROP TABLE IF EXISTS $BOOK_TABLE")
            onCreate(db)
        }
    }
    fun addBook(title: String,author:String,publisher: String){
        val cv=ContentValues()
        cv.put(BOOK_TITLE,title)
        cv.put(BOOK_AUTHOR,author)
        cv.put(BOOK_PUBLISHER,publisher)

        /*
        writableDatabase.insert(BOOK_TITLE,title)
        writableDatabase.insert(BOOK_AUTHOR,author)
        writableDatabase.insert(BOOK_PUBLISHER,publisher)
        */

        writableDatabase.insert(BOOK_TABLE,null,cv)
        writableDatabase.close()

    }
    fun printAllBooks(){
        val c=readableDatabase.query(BOOK_TABLE, arrayOf(BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER),
        null,null,null,null,null)
        if (c.count!=0){
            c.moveToFirst()
            do {
                Toast.makeText(context,"Book:${c.getString(0)},${c.getString(1)},${c.getString(2)}",
                    Toast.LENGTH_SHORT).show()
            }while (c.moveToNext())
        }
    }

}
