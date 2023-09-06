package com.example.sunopodcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.example.sunopodcast.Viewmodel.MyViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MyViewModel by viewModels()
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.custom_toolbar)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_main)
        val nav = findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val navigation = findViewById<NavigationView>(R.id.navigate)
        val drawerLayout = findViewById<DrawerLayout>(R.id.draw)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        val drawer = findViewById<ImageButton>(R.id.drwaer)
        drawer.setOnClickListener {


            if (currenbtuser.login) {
                drawerLayout.openDrawer(GravityCompat.END)
            }else{
                Toast.makeText(applicationContext, "Please Login First", Toast.LENGTH_SHORT).show()
            }

        }
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
//        viewModel.getallpodcastlist()
//        lifecycleScope.launchWhenStarted {
////            delay(3000)
//            viewModel.sharedtoast.collect{
//                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
//            }
//        }


//        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipetorefresh)
//        swipe.setOnRefreshListener {
//            viewModel.getallpodcastlist()
//        }
//        val currentFragment:MutableList<Fragment?> = mutableListOf(supportFragmentManager.findFragmentById(R.id.createPodcast)
//                                                                   ,supportFragmentManager.findFragmentById(R.id.listOfPodcast)
//                                                                    ,supportFragmentManager.findFragmentById(R.id.listenpodcast)
//                                                                   ,supportFragmentManager.findFragmentById(R.id.livePodCast))

        navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.signout -> {
                    val curId = findNavController(R.id.fragmentContainerView).currentDestination?.id
//                    for(i in currentFragment){
                        if (curId == R.id.createPodcast){
                                findNavController(R.id.fragmentContainerView).navigate(R.id.action_createPodcast_to_loginfragment)
                                currenbtuser.login = false
                            }
                        if (curId == R.id.listOfPodcast){
                            findNavController(R.id.fragmentContainerView).navigate(R.id.action_listOfPodcast_to_loginfragment)
                            currenbtuser.login = false
                        }
                        if (curId == R.id.listenpodcast){
                            findNavController(R.id.fragmentContainerView).navigate(R.id.action_listenpodcast_to_loginfragment)
                            currenbtuser.login = false
                        }
                        if (curId == R.id.livePodCast){
                            findNavController(R.id.fragmentContainerView).navigate(R.id.action_livePodCast_to_loginfragment)
                            currenbtuser.login = false
                        }
                    if (curId == R.id.karokelistfrag){
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_karokelistfrag_to_loginfragment)
                        currenbtuser.login = false
                    }
                    if (curId == R.id.listenKaroke){
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_listenKaroke_to_loginfragment)
                        currenbtuser.login = false
                    }
//                    }
                    pref.edit().clear().apply()
                    drawerLayout.closeDrawer(GravityCompat.END)
              true  }
                R.id.createpodcast ->{
                    val curId = findNavController(R.id.fragmentContainerView).currentDestination?.id

                    if (curId == R.id.listOfPodcast){
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_listOfPodcast_to_createPodcast)

                    }
                    if (curId == R.id.listenpodcast){
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_listenpodcast_to_createPodcast)

                    }
                    if (curId == R.id.livePodCast){
                        Toast.makeText(applicationContext, "Already in a live podcast", Toast.LENGTH_SHORT).show()

                    }
                    if (curId == R.id.karokelistfrag){
                        Toast.makeText(applicationContext, "PLEASE END THIS SESSION FIRTST", Toast.LENGTH_SHORT).show()
                    }
                    if (curId == R.id.listenKaroke){
                        Toast.makeText(applicationContext, "PLEASE END THIS SESSION FIRTST", Toast.LENGTH_SHORT).show()
                    }
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.karoke -> {
                    val curId = findNavController(R.id.fragmentContainerView).currentDestination?.id

                    if (curId == R.id.listOfPodcast){
                        findNavController(R.id.fragmentContainerView).navigate(R.id.action_listOfPodcast_to_listenKaroke)

                    }
                    if (curId == R.id.listenpodcast){
                       Toast.makeText(applicationContext,"Already listening a karoke please make sure to end the session first",
                        Toast.LENGTH_SHORT).show()

                    }
                    if (curId == R.id.livePodCast){
                        Toast.makeText(applicationContext, "Already in a live podcast", Toast.LENGTH_SHORT).show()

                    }
                    if (curId == R.id.karokelistfrag){
                        Toast.makeText(applicationContext, "Already in karoke section", Toast.LENGTH_SHORT).show()
                    }
                    if (curId == R.id.listenKaroke){
                        Toast.makeText(applicationContext, "Already Listening a live karoke session", Toast.LENGTH_SHORT).show()
                    }
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true}

                else -> {false}
            }
        }
        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            onBackPressed()
        }
    }




}
object currenbtuser{
    var login = false
}