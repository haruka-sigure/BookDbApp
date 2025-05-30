package com.example.bookdbapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.util.concurrent.Flow.Publisher



class BookDbHelper private constructor( val context: Context):
    SQLiteOpenHelper(context, DB_FILE,null,1){

        companion object{
            private const val DB_FILE="book.db"
            private const val BOOK_TABLE="books"
            private const val ID="id"
            private const val BOOK_TITLE="book_title"
            private const val BOOK_AUTHOR="book_author"
            private const val BOOK_PUBLISHER="book_publisher"

            private var bookDbHelper:BookDbHelper?=null

            fun init(context: Context){
                if (bookDbHelper==null) bookDbHelper=BookDbHelper(context)
            }

            fun getInstance():BookDbHelper?{
                return bookDbHelper
            }
        }
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
    fun addBook(book: Book){
        val cv=ContentValues()
        cv.put(BOOK_TITLE,book.title)
        cv.put(BOOK_AUTHOR,book.author)
        cv.put(BOOK_PUBLISHER,book.publisher)

        writableDatabase.insert(BOOK_TABLE,null,cv)
        writableDatabase.close()

    }
    fun getAllBooks():ArrayList<Book>{
        val c=readableDatabase.query(BOOK_TABLE, arrayOf(ID,BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER),
        null,null,null,null,null)

        val books =arrayListOf<Book>()

        if (c.count!=0){
            c.moveToFirst()
            do {
                val b=Book(c.getInt(0),c.getString(1),c.getString(2),c.getString(3))
                books.add(b)
            }while (c.moveToNext())
        }
        return books
    }
    fun deletbook(id:Int){
        writableDatabase.delete(BOOK_TABLE,"$ID=?", arrayOf(id.toString()))
        writableDatabase.close()
    }

    fun updateBook(book: Book) {
        val cv=ContentValues()
        cv.put(BOOK_TITLE,book.title)
        cv.put(BOOK_AUTHOR,book.author)
        cv.put(BOOK_PUBLISHER,book.publisher)

        writableDatabase.update(BOOK_TABLE,cv,"$ID = ?", arrayOf(book.id.toString()))
        writableDatabase.close()
    }

    fun queryBooks(book: Book):ArrayList<Book>{
        var conditionExit=false
        var selection=""

        if (book.title.isNotEmpty()){
            selection="$BOOK_TITLE='${book.title}'"
            conditionExit=true
        }
        if (book.author.isNotEmpty()){
            if (!conditionExit){
                selection="$BOOK_AUTHOR='${book.author}'"
                conditionExit=true
            }
            else{
                selection="$selection AND $BOOK_AUTHOR='${book.author}'"
            }
        }
        if (book.publisher.isNotEmpty()){
            if (!conditionExit){
                selection="$BOOK_PUBLISHER='${book.publisher}'"
                conditionExit=true
            }
            else{
                selection="$selection AND $BOOK_PUBLISHER='${book.publisher}'"
            }
        }

        val c=readableDatabase.query(BOOK_TABLE, arrayOf(ID,BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER),
            selection,null,null,null,null)

        val books =arrayListOf<Book>()

        if (c.count!=0){
            c.moveToFirst()
            do {
                val b=Book(c.getInt(0),c.getString(1),c.getString(2),c.getString(3))
                books.add(b)
            }while (c.moveToNext())
        }
        return books
    }

}
