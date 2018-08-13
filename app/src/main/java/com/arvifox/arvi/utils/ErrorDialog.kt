package com.arvifox.arvi.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class ErrorDialog : DialogFragment() {

    private val ARG = "arg"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(arguments?.getString(ARG))
                    .setPositiveButton(android.R.string.ok) { _, _ -> activity?.finish() }
                    .create()

    companion object {

        @JvmStatic
        fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
            arguments = Bundle().apply { putString(ARG, message) }
        }
    }

}