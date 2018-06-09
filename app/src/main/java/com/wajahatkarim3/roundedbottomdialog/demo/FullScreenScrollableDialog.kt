package com.wajahatkarim3.roundedbottomdialog.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FullScreenScrollableDialog : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fullscreen_scrollable_dialog_layout, container, false)
        return v
    }
}