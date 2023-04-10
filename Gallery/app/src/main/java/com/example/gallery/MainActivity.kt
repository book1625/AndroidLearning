package com.example.gallery

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private val logTag = "mylog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationUI.setupActionBarWithNavController(
            this,
            getNaviController()
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || getNaviController().navigateUp()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy: main")
        VolleySingleton.cancelAll()
        super.onDestroy()
    }

    //共用的取得 navController 的方法
    //https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
    private fun getNaviController(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }
}