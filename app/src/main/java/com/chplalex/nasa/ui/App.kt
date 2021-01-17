package com.chplalex.nasa.ui

import android.app.Application
import com.chplalex.nasa.ui.activity.MainActivity
import com.chplalex.nasa.di.component.ActivityComponent
import com.chplalex.nasa.di.component.AppComponent
import com.chplalex.nasa.di.component.DaggerAppComponent
import com.chplalex.nasa.di.module.ActivityModule

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    var activityComponent: ActivityComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.create()
    }

    fun createActivityComponent(activity: MainActivity) {
        activityComponent = appComponent.createActivityComponent(ActivityModule(activity))
    }

    fun destroyActivityComponent() {
        activityComponent = null
    }

    companion object {

        lateinit var instance: App
            private set
    }
}