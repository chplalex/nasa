package com.chplalex.nasa.di.module

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.chplalex.nasa.ui.activity.MainActivity
import com.chplalex.nasa.R
import com.chplalex.nasa.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: MainActivity) {

//    @ActivityScope
//    @Provides
//    fun sharedPreferences() = activity.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
//
    @ActivityScope
    @Provides
    fun context() : Context = activity

//    @ActivityScope
//    @Provides
//    fun resourses() : Resources = activity.resources

    @ActivityScope
    @Provides
    fun navController() : NavController = Navigation.findNavController(activity, R.id.nav_host_fragment)

}