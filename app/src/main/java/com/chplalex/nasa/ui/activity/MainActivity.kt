package com.chplalex.nasa.ui.activity

import android.animation.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
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

    private val transparentBackground: FrameLayout by lazy { findViewById(R.id.transparent_background) }
    private val optionApodContainer: LinearLayout by lazy { findViewById(R.id.option_apod_container) }
    private val optionEpicContainer: LinearLayout by lazy { findViewById(R.id.option_epic_container) }
    private val optionWikiContainer: LinearLayout by lazy { findViewById(R.id.option_wiki_container) }
    private val optionSettingsContainer: LinearLayout by lazy { findViewById(R.id.option_settings_container) }
    private val fabImageView: ImageView by lazy { findViewById(R.id.fab_image_view) }
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }

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

        val apodListener = AnimatorListener(optionApodContainer, true, R.id.nav_action_apod)
        val epicListener = AnimatorListener(optionEpicContainer, true, R.id.nav_action_epic)
        val wikiListener = AnimatorListener(optionWikiContainer, true, R.id.nav_action_wiki)
        val settingsListener = AnimatorListener(optionSettingsContainer, true, R.id.nav_action_settings)
        val backgroundListener = AnimatorListener(transparentBackground, true, null)

        animateFab(
            apodListener,
            epicListener,
            wikiListener,
            settingsListener,
            backgroundListener,
            R.animator.animator_expand_fab,
            R.animator.animator_expand_apod_option,
            R.animator.animator_expand_epic_option,
            R.animator.animator_expand_wiki_option,
            R.animator.animator_expand_settings_option,
            R.animator.animator_expand_background
        )
    }

    private fun collapseFab() {
        isExpanded = false

        val apodListener = AnimatorListener(optionApodContainer, false, null)
        val epicListener = AnimatorListener(optionEpicContainer, false, null)
        val wikiListener = AnimatorListener(optionWikiContainer, false, null)
        val settingsListener = AnimatorListener(optionSettingsContainer, false, null)
        val backgroundListener = AnimatorListener(transparentBackground, false, null)

        animateFab(
            apodListener,
            epicListener,
            wikiListener,
            settingsListener,
            backgroundListener,
            R.animator.animator_collaps_fab,
            R.animator.animator_collaps_apod_option,
            R.animator.animator_collaps_epic_option,
            R.animator.animator_collaps_wiki_option,
            R.animator.animator_collaps_settings_option,
            R.animator.animator_collaps_background
        )
    }

    private fun animateFab(
        apodListener: AnimatorListenerAdapter,
        epicListener: AnimatorListenerAdapter,
        wikiListener: AnimatorListenerAdapter,
        settingsListener: AnimatorListenerAdapter,
        backgroundListener: AnimatorListenerAdapter,
        fabAnimatorResId: Int,
        apodAnimatorResId: Int,
        epicAnimatorResId: Int,
        wikiAnimatorResId: Int,
        settingsAnimatorResId: Int,
        backgroundAnimatorResId: Int
    ) {
        AnimatorInflater.loadAnimator(this, fabAnimatorResId).apply {
            setTarget(fabImageView)
            start()
        }

        AnimatorInflater.loadAnimator(this, apodAnimatorResId).apply {
            setTarget(optionApodContainer)
            addListener(apodListener)
            start()
        }

        AnimatorInflater.loadAnimator(this, epicAnimatorResId).apply {
            setTarget(optionEpicContainer)
            addListener(epicListener)
            start()
        }

        AnimatorInflater.loadAnimator(this, wikiAnimatorResId).apply {
            setTarget(optionWikiContainer)
            addListener(wikiListener)
            start()
        }

        AnimatorInflater.loadAnimator(this, settingsAnimatorResId).apply {
            setTarget(optionSettingsContainer)
            addListener(settingsListener)
            start()
        }

        AnimatorInflater.loadAnimator(this, backgroundAnimatorResId).apply {
            setTarget(transparentBackground)
            addListener(backgroundListener)
            start()
        }
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
        optionWikiContainer.apply {
            alpha = 0f
            isClickable = false
        }
        optionSettingsContainer.apply {
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
        setSupportActionBar(findViewById(R.id.nasa_toolbar))
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

    inner class AnimatorListener(
        private val target: View,
        private val isClickable: Boolean,
        private val actionResId: Int? = null
    ) : AnimatorListenerAdapter() {

        override fun onAnimationEnd(animation: Animator?) {
            target.isClickable = isClickable
            if (isClickable) {
                if (actionResId == null)
                   target.setOnClickListener { collapseFab() }
                else
                    target.setOnClickListener {
                        navController.navigate(actionResId)
                        collapseFab()
                    }
            } else
                target.setOnClickListener(null)
        }
    }
}