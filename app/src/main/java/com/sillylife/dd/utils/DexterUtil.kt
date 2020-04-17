package com.sillylife.dd.utils

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

object DexterUtil {

    interface DexterUtilListener {
        fun permissionGranted()
        // some time token can be null
        fun permissionDenied(token: PermissionToken?)
    }

    fun with(activity: Activity, vararg permissions: String): Builder {
        return Builder(activity, *permissions)
    }

    class Builder(private val mActivity: Activity, vararg permissions: String) {

        private val mPermissions: Collection<String>?
        private var mListener: DexterUtilListener? = null

        init {
            mPermissions = Arrays.asList(*permissions)
        }

        fun setListener(listener: DexterUtilListener): Builder {
            mListener = listener
            return this
        }

        fun check() {
            if (mPermissions != null && mPermissions.isNotEmpty() && mListener != null) {
                if (mPermissions.size == 1) {
                    Dexter.withActivity(mActivity).withPermission(mPermissions.iterator().next())
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                mListener!!.permissionGranted()
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                mListener!!.permissionDenied(null)
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permission: PermissionRequest,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest()
                            }
                        }).check()
                } else {
                    Dexter.withActivity(mActivity).withPermissions(mPermissions)
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                if (report.areAllPermissionsGranted()) {
                                    mListener!!.permissionGranted()
                                } else {
                                    mListener!!.permissionDenied(null)
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: List<PermissionRequest>,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest()
                            }
                        }).check()
                }
            }
        }

    }

}
