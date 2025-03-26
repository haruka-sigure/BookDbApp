package com.example.bookdbapp

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookdbapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityMainBinding

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
        BookDbHelper.init(this)
        val dbHelper=BookDbHelper.getInstance()

        dataBinding.btAdd.setOnClickListener {
            val book=Book(dataBinding.edtitle.text.toString(),dataBinding.edauthor.text.toString(),
                dataBinding.edpublisher.text.toString())
            dbHelper?.addBook(book)
        }
        dataBinding.btlist.setOnClickListener {
            val books=dbHelper?.getAllBooks()
            books?.let {
                for (b in books){
                    Toast.makeText(this,"Book:${b.title},${b.author},${b.publisher}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}