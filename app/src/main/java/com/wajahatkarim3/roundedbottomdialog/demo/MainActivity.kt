package com.wajahatkarim3.roundedbottomdialog.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun openDialog(view: View)
    {
        //val bottomDialog = FullScreenScrollableDialog()
        //bottomDialog.show(supportFragmentManager, "dialog")

        val bottomDialog = SpringBackBottomSheetDialog(this)
        val v = LayoutInflater.from(this).inflate(R.layout.new_task_dialog_layout, null, false)
        bottomDialog.setContentView(v)
        //bottomDialog.addSpringBackDisLimit(500)
        bottomDialog.show()
    }
}
