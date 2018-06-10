package com.wajahatkarim3.roundedbottomdialog

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Window

open class RoundedBottomSheetDialog : AppCompatDialog
{
    constructor(context: Context) : this(context, 0)

    constructor(context: Context, theme: Int) : super(context, theme) {
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }
    
}