package com.chplalex.nasa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.presenter.PresenterWiki
import com.chplalex.nasa.mvp.view.IViewWiki
import com.chplalex.nasa.ui.App
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class FragmentWiki : MvpAppCompatFragment(R.layout.fragment_wiki), IViewWiki {

    @Inject
    lateinit var injectPresenter: Provider<PresenterWiki>

    private val presenter by moxyPresenter {
        injectPresenter.get()
    }

    private lateinit var inputEditText: TextInputEditText
    private lateinit var inputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputEditText = view.findViewById(R.id.wiki_input_edit_text)
        inputLayout = view.findViewById(R.id.wiki_input_layout)
        inputLayout.setEndIconOnClickListener {
            presenter.onEndIconPressed(inputEditText.text.toString())
        }
    }
}