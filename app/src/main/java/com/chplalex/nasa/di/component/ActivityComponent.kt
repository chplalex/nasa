package com.chplalex.nasa.di.component

import com.chplalex.nasa.ui.activity.MainActivity
import com.chplalex.nasa.di.module.ActivityModule
import com.chplalex.nasa.di.scope.ActivityScope
import com.chplalex.nasa.ui.activity.NoteActivity
import com.chplalex.nasa.ui.activity.NotesActivity
import com.chplalex.nasa.ui.fragment.*
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class
    ]
)

interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: FragmentNasaApod)
    fun inject(fragment: FragmentWiki)
    fun inject(fragment: FragmentNasaEpicContainer)
    fun inject(fragment: FragmentNasaEpicPage)
    fun inject(activity: NotesActivity)
    fun inject(activity: NoteActivity)
}