package com.wajahatkarim3.roundedbottomdialog.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun openDialog(view: View)
    {
        val bottomDialog = FullScreenScrollableDialog()
        bottomDialog.show(supportFragmentManager, "dialog")
    }
}
