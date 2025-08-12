package com.sun.weatherapp.utils

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sun.weatherapp.R

fun Fragment.showProgressDialog(): AlertDialog? {
    return if (context != null) {
        AlertDialog.Builder(context!!, R.style.AFUtilProgressBarStyle)
            .setCancelable(false)
            .setView(R.layout.af_util_layout_dialog_progress)
            .show()
    } else null
}
