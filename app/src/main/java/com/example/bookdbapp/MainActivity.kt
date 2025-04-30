package com.example.bookdbapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookdbapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityMainBinding

    private lateinit var bookList: ArrayList<Book>
    private lateinit var bookRecyclerViewAdapter: BookRecyclerViewAdapter

    private class BookRecyclerViewAdapter(private val books:List<Book>):
        RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>(){
            data class ViewHolder(private val bookView:View):
                    RecyclerView.ViewHolder(bookView){
                        val tvTitle=bookView.findViewById<TextView>(R.id.tvTitle)
                        val tvAuthor=bookView.findViewById<TextView>(R.id.tvAuthor)
                        val tvPublisher=bookView.findViewById<TextView>(R.id.tvPublisher)
                    }
        override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):ViewHolder{
            val v=LayoutInflater.from(parent.context)
                .inflate(R.layout.book_item, parent,false)
            return ViewHolder(v)
        }
        override fun onBindViewHolder(holder:ViewHolder,position:Int){
            val book=books[position]
            holder.tvTitle.text=book.title
            holder.tvAuthor.text=book.author
            holder.tvPublisher.text=book.publisher
        }

        override fun getItemCount(): Int {
            return books.size
        }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        /*val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        bookList= arrayListOf()
        bookRecyclerViewAdapter= BookRecyclerViewAdapter(bookList)
        val rvBookList=findViewById<RecyclerView>(R.id.rvBooklist)
        rvBookList.adapter=bookRecyclerViewAdapter
        rvBookList.layoutManager=LinearLayoutManager(this)

        BookDbHelper.init(this)
        val dbHelper=BookDbHelper.getInstance()

        dataBinding.btAdd.setOnClickListener {
            val book=Book(dataBinding.edtitle.text.toString(),dataBinding.edauthor.text.toString(),
                dataBinding.edpublisher.text.toString())
            dbHelper?.addBook(book)
        }
        dataBinding.btlist.setOnClickListener {
            bookList.clear()
            val books=dbHelper?.getAllBooks()
            books?.let {
                bookList.addAll(books)
                bookRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }