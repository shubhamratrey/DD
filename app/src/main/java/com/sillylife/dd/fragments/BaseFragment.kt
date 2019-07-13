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

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    fun showToast(message: String, length: Int) {
        if (activity != null && !activity?.isFinishing!!) {
            Toast.makeText(activity, message, length).show()
        }

    }

    fun getBaseActivity(): BaseActivity {
        var fragmentActivity: FragmentActivity = mContext as FragmentActivity
        return fragmentActivity as BaseActivity
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
