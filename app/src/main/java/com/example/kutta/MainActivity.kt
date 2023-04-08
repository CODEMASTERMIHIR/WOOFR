package com.example.kutta

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.kutta.activity.RatingActivity
import com.example.kutta.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle : ActionBarDrawerToggle ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerlayout, R.string.open, R.string.close)
        binding.drawerlayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener(this)
        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.rateUs ->{

                val intent = Intent(this, RatingActivity::class.java)
                startActivity(intent)

            }R.id.termsCondition ->{
                Toast.makeText(this,"Terms And Condition",Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.privacypolicies.com/live/ed88c4fc-db2a-4b4c-b714-3e95281907f3")
                startActivity(intent)


        }R.id.privacy ->{
                Toast.makeText(this,"PrivacyPolicy",Toast.LENGTH_SHORT).show()
            }R.id.developer ->{
                Toast.makeText(this,"Developer",Toast.LENGTH_SHORT).show()
            }R.id.fav->{
                Toast.makeText(this,"Favourite",Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
           true
        }else
            super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.drawerlayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerlayout.close()
        }else
            super.onBackPressed()
    }
}