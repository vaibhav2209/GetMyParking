package com.example.getmyparking

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.getmyparking.utils.CustomProcessDialog


abstract class BaseActivity : AppCompatActivity() {

    private var dialogFragment: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)

    }

    protected open fun showBusyDialog(message: String?) {
        if (dialogFragment != null && dialogFragment?.isVisible == true) {
            dialogFragment?.dismiss()
        }
        dialogFragment = CustomProcessDialog()
        val bundle = Bundle()
        bundle.putString("message", message)
        dialogFragment?.arguments = bundle
        dialogFragment?.isCancelable = false
        dialogFragment?.show(supportFragmentManager, "busyDialog")

    }

    protected open fun dismissBusyDialog() {
        if (dialogFragment?.isVisible == true) {
            dialogFragment?.dismiss()
        }
    }

    protected open fun toastMessage(message: String?, toastTiming: Int) {
        Toast.makeText(this, message, toastTiming).show()
    }
}