package com.chplalex.nasa.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.NasaEpicItem
import com.chplalex.nasa.mvp.presenter.PresenterNasaEpicContainer
import com.chplalex.nasa.mvp.view.IViewNasaEpicContainer
import com.chplalex.nasa.ui.App
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.relex.circleindicator.CircleIndicator3
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class FragmentNasaEpicContainer : MvpAppCompatFragment(R.layout.fragment_nasa_epic_container), IViewNasaEpicContainer {

    @Inject
    lateinit var injectProvider: Provider<PresenterNasaEpicContainer>

    private val presenter by moxyPresenter {
        injectProvider.get()
    }

    private lateinit var chipDate: Chip
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerIndicator: CircleIndicator3

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chipDate = view.findViewById(R.id.nasa_epic_chip_date)
        chipDate.setOnClickListener { presenter.onChipDateClick() }
        viewPager = view.findViewById(R.id.nasa_epic_view_pager)
        viewPagerIndicator = view.findViewById(R.id.nasa_epic_view_pager_indicator)
    }

    override fun setChipDate(text: CharSequence) {
        chipDate.text = text
    }

    override fun requestDate(chipDate: Date) {
        val picker = MaterialDatePicker.Builder.datePicker().build()
        picker.addOnPositiveButtonClickListener { presenter.onDateSelected(Date(it)) }
        picker.show(childFragmentManager, null)
    }

    override fun showError(title: String, error: Throwable) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setMessage(error.message)
            .create()
            .show()
    }

    override fun setViewPager(list: List<NasaEpicItem>) {
        viewPager.adapter = ViewPagerAdapter(list, this)
        viewPagerIndicator.setViewPager(viewPager)
    }

    inner class ViewPagerAdapter(
        private val list: List<NasaEpicItem>,
        fragment: Fragment
    ) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount() = list.size
        override fun createFragment(position: Int) = FragmentNasaEpicPage.newInstance(list[position])
    }
}