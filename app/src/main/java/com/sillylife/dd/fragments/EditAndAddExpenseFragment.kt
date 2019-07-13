package com.sillylife.dd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.sillylife.dd.DDApplication
import com.sillylife.dd.database.entities.MixedItemDataEntity
import com.sillylife.dd.database.repo.MixedItemDataRepo
import kotlinx.android.synthetic.main.fragment_calendar.*

class EditAndAddExpenseFragment : BaseFragment() {

    companion object {
        val TAG = EditAndAddExpenseFragment::class.java.simpleName
        fun newInstance() = EditAndAddExpenseFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.sillylife.dd.R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deepLinkRepo = MixedItemDataRepo(DDApplication.getInstance())

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            showToast(view.date.toString(), Toast.LENGTH_SHORT)
            Snackbar.make(view, "$dayOfMonth/$month/$year", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            deepLinkRepo.insert(MixedItemDataEntity(0, "SHub", "$dayOfMonth/$month/$year", false, view.date.toString()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}