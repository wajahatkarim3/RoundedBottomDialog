package com.wajahatkarim3.roundedbottomdialog.demo

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.annotation.NonNull
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.support.design.widget.CoordinatorLayout




class BottomSheetFragment : BottomSheetDialogFragment()
{
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)

        //Get the content View
        val contentView = View.inflate(context, R.layout.fragment_bottom_sheet_layout, null)
        dialog?.setContentView(contentView)

        //Set the coordinator layout behavior
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)

            contentView.viewTreeObserver.addOnGlobalLayoutListener {
                //contentView.viewTreeObserver.removeOnGlobalLayoutListener(it)
                val height = contentView.getMeasuredHeight()
                behavior.setPeekHeight(height)
            }
        }
    }

    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }

        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}