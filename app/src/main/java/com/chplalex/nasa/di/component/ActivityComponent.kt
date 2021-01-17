package com.chplalex.nasa.di.component

import com.chplalex.nasa.ui.activity.MainActivity
import com.chplalex.nasa.di.module.ActivityModule
import com.chplalex.nasa.di.scope.ActivityScope
import com.chplalex.nasa.mvp.presenter.PresenterNasaApod
import com.chplalex.nasa.mvp.presenter.PresenterWiki
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class
    ]
)

interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(presenter: PresenterNasaApod)
    fun inject(presenter: PresenterWiki)
}