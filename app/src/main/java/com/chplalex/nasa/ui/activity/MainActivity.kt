package com.chplalex.nasa.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
    private var isExpanded = false

    private lateinit var transparentBackground: FrameLayout
    private lateinit var optionApodContainer: LinearLayout
    private lateinit var optionEpicContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var fabImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        instance.createActivityComponent(this)
        super.onCreate(savedInstanceState)
        initTheme()
        setContentView(R.layout.activity_main)
        instance.activityComponent?.inject(this)
        initToolbar()
        initFAB()
        initNavigation()
    }

    private fun initFAB() {
        transparentBackground = findViewById(R.id.transparent_background)
        optionApodContainer = findViewById(R.id.option_apod_container)
        optionEpicContainer = findViewById(R.id.option_epic_container)
        fab = findViewById(R.id.fab)
        fabImageView = findViewById(R.id.fab_image_view)

        fab.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }

        setInitialState()
    }

    private fun expandFAB() {
        isExpanded = true

        ObjectAnimator.ofFloat(fabImageView, "rotation", 0f, 225f).start()
        ObjectAnimator.ofFloat(optionEpicContainer, "translationY", -130f).start()
        ObjectAnimator.ofFloat(optionApodContainer, "translationY", -250f).start()

        optionEpicContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    optionEpicContainer.isClickable = true
                    optionApodContainer.setOnClickListener {
                        Toast.makeText(this@MainActivity, "Option 2", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        optionApodContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    optionApodContainer.isClickable = true
                    optionApodContainer.setOnClickListener {
                        Toast.makeText(this@MainActivity, "Option 1", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        transparentBackground.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparentBackground.isClickable = true
                }
            })
    }

    private fun collapseFab() {
        isExpanded = false

        ObjectAnimator.ofFloat(fabImageView, "rotation", 0f, -180f).start()
        ObjectAnimator.ofFloat(optionEpicContainer, "translationY", 0f).start()
        ObjectAnimator.ofFloat(optionApodContainer, "translationY", 0f).start()

        optionEpicContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    optionEpicContainer.isClickable = false
                    optionApodContainer.setOnClickListener(null)
                }
            })

        optionApodContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    optionApodContainer.isClickable = false
                }
            })

        transparentBackground.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparentBackground.isClickable = false
                }
            })
    }

    private fun setInitialState() {
        transparentBackground.apply {
            alpha = 0f
        }
        optionApodContainer.apply {
            alpha = 0f
            isClickable = false
        }
        optionEpicContainer.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun initNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_nasa_apod, R.id.nav_wiki),
            drawerLayout
        )
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

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
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