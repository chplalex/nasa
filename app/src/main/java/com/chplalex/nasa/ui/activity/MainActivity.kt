package com.chplalex.nasa.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.chplalex.nasa.R
import com.chplalex.nasa.ui.App.Companion.instance
import com.chplalex.nasa.utils.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        instance.createActivityComponent(this)
        super.onCreate(savedInstanceState)
        initTheme()
        setContentView(R.layout.activity_main)
        instance.activityComponent?.inject(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_nasa_apod, R.id.nav_wiki
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initTheme() {
        val sp = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)
        val themeStyle = when (sp.getInt(PREFS_THEME_ID, THEME_ID_SYSTEM)) {
            THEME_ID_SYSTEM -> R.style.Theme_Nasa_System
            THEME_ID_LIGHT -> R.style.Theme_Nasa_Light
            THEME_ID_DARK -> R.style.Theme_Nasa_Dark
            THEME_ID_CUSTOM -> R.style.Theme_Nasa_Custom
            else -> R.style.Theme_Nasa_System
        }
        setTheme(themeStyle)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            navController.navigate(R.id.nav_action_settings)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        instance.destroyActivityComponent()
    }
}