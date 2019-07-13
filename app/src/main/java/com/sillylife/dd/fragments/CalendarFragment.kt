package com.sillylife.dd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.sillylife.dd.R
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : BaseFragment() {

    companion object {
        val TAG = CalendarFragment::class.java.simpleName
        fun newInstance() = CalendarFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Snackbar.make(view, "$dayOfMonth/$month/$year", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}