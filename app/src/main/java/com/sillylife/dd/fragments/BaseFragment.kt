package com.sillylife.dd.fragments

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sillylife.dd.activities.BaseActivity

open class BaseFragment : Fragment() {

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initEveryThing()
        mContext = context
    }

    fun showToast(message: String, length: Int) {
        getBaseActivity().showToast(message, length)
    }

    fun getBaseActivity(): BaseActivity {
        return (mContext as FragmentActivity) as BaseActivity
    }

    fun addFragment(fragment: Fragment, tag: String?=null) {
        getBaseActivity().addFragment(fragment, tag)
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        getBaseActivity().replaceFragment(fragment, tag)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
