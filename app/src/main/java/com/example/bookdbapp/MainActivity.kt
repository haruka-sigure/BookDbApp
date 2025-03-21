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

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val dbHelper=BookDbHelper(this)

        dataBinding.btAdd.setOnClickListener {
            Toast.makeText(this,
                "${dataBinding.edtitle.text.toString()},${dataBinding.edauthor.text.toString()},${dataBinding.edpublisher.text.toString()}",
                Toast.LENGTH_LONG).show()
            dbHelper.addBook(dataBinding.edtitle.text.toString(),dataBinding.edauthor.text.toString(),
                dataBinding.edpublisher.text.toString())
        }
        dataBinding.btlist.setOnClickListener {
            dbHelper.printAllBooks()
        }
    }
}