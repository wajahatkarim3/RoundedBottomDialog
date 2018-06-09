package com.wajahatkarim3.roundedbottomdialog.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomDialog = ListOptionsDialog()
        bottomDialog.show(supportFragmentManager, "dialog")
    }
}
