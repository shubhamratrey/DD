package com.sillylife.dd.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sillylife.dd.R
import com.sillylife.dd.utils.FragmentHelper


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
            }
        }
    }

    fun showToast(message: String?, length: Int) {
        if (message != null && !isFinishing) {
            Toast.makeText(this, message, length).show()
        }
    }

    fun addFragment(fragment: Fragment, tag: String?=null) {
        FragmentHelper.add(R.id.container, supportFragmentManager, fragment, tag)
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        FragmentHelper.replace(R.id.container, supportFragmentManager, fragment, tag)
    }
}