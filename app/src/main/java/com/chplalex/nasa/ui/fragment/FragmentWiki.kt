package com.chplalex.nasa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.presenter.PresenterWiki
import com.chplalex.nasa.mvp.view.IViewWiki
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class FragmentWiki : MvpAppCompatFragment(), IViewWiki {

    private val presenter by moxyPresenter {
        PresenterWiki()
    }

    private lateinit var inputEditText: TextInputEditText
    private lateinit var inputLayout: TextInputLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_wiki, container, false).also {
        inputEditText = it.findViewById(R.id.wiki_input_edit_text)
        inputLayout = it.findViewById(R.id.wiki_input_layout)
        inputLayout.setEndIconOnClickListener {
            presenter.onEndIconPressed(inputEditText.text.toString())
        }
    }
}