package com.sillylife.dd.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.sillylife.dd.R
import com.sillylife.dd.fragments.EditAndAddExpenseFragment
import com.sillylife.dd.fragments.HomeFragment
import com.sillylife.dd.utils.FragmentHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(HomeFragment.newInstance(), FragmentHelper.HOME)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            addFragment(EditAndAddExpenseFragment.newInstance(), FragmentHelper.EDIT_AND_ADD_EXPENSE_FRAGMENT)
        }
    }
}
