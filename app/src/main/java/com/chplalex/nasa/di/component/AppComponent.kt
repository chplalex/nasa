package com.chplalex.nasa.di.component

import com.chplalex.nasa.di.module.ActivityModule
import com.chplalex.nasa.di.module.ApiModule
import com.chplalex.nasa.di.module.AppModule
import com.chplalex.nasa.di.scope.AppScope
import dagger.Component

@AppScope
@Component(
    modules = [
        ApiModule::class,
        AppModule::class
    ]
)

interface AppComponent {
    fun createActivityComponent(activityModule : ActivityModule) : ActivityComponent
}