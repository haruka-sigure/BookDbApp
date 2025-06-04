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
            private const val AUTHOR_ID="author_id"
            private const val PUBLISHER_ID="publisher_id"
            private const val AUTHOR_TABLE="authors"
            private const val AUTHOR_NAME="name"
            private const val PUBLISHER_TABLE="publishers"
            private const val PUBLISHER_NAME="name"

            private var bookDbHelper:BookDbHelper?=null

            fun init(context: Context){
                if (bookDbHelper==null) bookDbHelper=BookDbHelper(context)
            }

            fun getInstance():BookDbHelper?{
                return bookDbHelper
            }
        }
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateAuthorTable="CREATE TABLE $AUTHOR_TABLE($ID integer primary key,$AUTHOR_NAME nvarcher(20));"
        val sqlCreatePublisgerTable="CREATE TABLE $PUBLISHER_TABLE($ID integer primary key,$PUBLISHER_NAME nvarcher(20));"
        val sqlCreateBOOKTable="CREATE TABLE $BOOK_TABLE($ID integer primary key,$BOOK_TITLE nvarcher(30),"+
                "$AUTHOR_ID integer,$PUBLISHER_ID integer,"+
                "foreign key($AUTHOR_ID) references $AUTHOR_TABLE($ID),"+
                "foreign key($PUBLISHER_ID) references $PUBLISHER_TABLE($ID));"
        db?.execSQL(sqlCreateAuthorTable)
        db?.execSQL(sqlCreatePublisgerTable)
        db?.execSQL(sqlCreateBOOKTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion!=newVersion){
            db?.execSQL("DROP TABLE IF EXISTS $BOOK_TABLE")
            onCreate(db)
        }
    }

    fun addAuthor(name:String){
        val cv=ContentValues()
        cv.put(AUTHOR_NAME,name)
        writableDatabase.insert(AUTHOR_TABLE,null,cv)
    }

    fun addPublisher(name:String){
        val cv=ContentValues()
        cv.put(PUBLISHER_NAME,name)
        writableDatabase.insert(PUBLISHER_TABLE,null,cv)
    }

    fun addBook(book: Book){
        var c=readableDatabase.query(AUTHOR_TABLE, arrayOf(ID),"$AUTHOR_NAME=?", arrayOf(book.author),null,null,null)
        if (c.count==0) return
        c.moveToFirst()
        val authorId=c.getInt(0)

        var c=readableDatabase.query(PUBLISHER_TABLE, arrayOf(ID),"$PUBLISHER_NAME=?", arrayOf(book.publisher),null,null,null)
        if (c.count==0) return
        c.moveToFirst()
        val publisherId=c.getInt(0)

        val cv=ContentValues()
        cv.put(BOOK_TITLE,book.title)
        cv.put(AUTHOR_ID,authorId)
        cv.put(PUBLISHER_ID,publisherId)

        writableDatabase.insert(BOOK_TABLE,null,cv)
        writableDatabase.close()

    }
    fun getAllBooks():ArrayList<Book>{
        val c=readableDatabase.rawQuery(
            "SELECT $BOOK_TABLE.$ID,$BOOK_TABLE.$BOOK_TITLE,"+
            "$AUTHOR_TABLE.$AUTHOR_NAME,$PUBLISHER_TABLE.$PUBLISHER_NAME "+
            "FROM $BOOK_TABLE "+
            "JOIN $AUTHOR_TABLE ON $BOOK_TABLE.$AUTHOR_ID=$AUTHOR_TABLE.$ID "+
            "JOIN $PUBLISHER_TABLE ON $BOOK_TABLE.$PUBLISHER_ID=$PUBLISHER_TABLE.$ID;",null
        )

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
