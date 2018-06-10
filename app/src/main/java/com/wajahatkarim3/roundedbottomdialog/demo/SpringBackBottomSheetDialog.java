package com.wajahatkarim3.roundedbottomdialog.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by lgh on 18-4-9.
 *
 * Added controllable rebound distance
 *
 */

public class SpringBackBottomSheetDialog extends AppCompatDialog {

    private BottomSheetBehavior<FrameLayout> mBehavior;

    boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mCanceledOnTouchOutsideSet;

    private CoordinatorLayout coordinator;
    private FrameLayout bottomSheet;
    private Rect r = new Rect();
    private NestedScrollView nestedScrollView;

    public SpringBackBottomSheetDialog(@NonNull Context context) {
        this(context, R.style.BottomSheetDialogTheme);
    }

    public SpringBackBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, getThemeResId(context, R.style.BottomSheetDialogTheme));
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected SpringBackBottomSheetDialog(@NonNull Context context, boolean cancelable,
                                          OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mCancelable = cancelable;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(wrapInBottomSheet(layoutResId, null, null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(wrapInBottomSheet(0, view, params));
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (mCancelable != cancelable) {
            mCancelable = cancelable;
            if (mBehavior != null) {
                mBehavior.setHideable(cancelable);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }

    /**
     * How much the top is added to the top triggers the shrink effect
     * @param targetLimitH int Height restrictions
     */
    @SuppressWarnings("all")
    public void addSpringBackDisLimit(final int targetLimitH){
        if(coordinator == null)
            return;
        final int totalHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        final int currentH = (int) ((float)totalHeight*0.618);
        final int leftH    = totalHeight - currentH;
        /*
        coordinator.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_MOVE:
                                // Calculate the coordinates relative to the screen
                                bottomSheet.getGlobalVisibleRect(r);
                                break;
                            case MotionEvent.ACTION_UP:
                                int limitH;
                                if(targetLimitH < 0)
                                    limitH = (leftH + currentH/3);
                                else
                                    limitH = targetLimitH;
                                if(r.top <= limitH)
                                    if (mBehavior != null)
                                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                break;
                        }
                        return false;
                    }
                }
        );
        */
    }

    private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
        final FrameLayout container = (FrameLayout) View.inflate(getContext(),
                R.layout.custom_sheet_layout, null);
        coordinator =
                (CoordinatorLayout) container.findViewById(R.id.c);
        if (layoutResId != 0 && view == null) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        bottomSheet = (FrameLayout) coordinator.findViewById(R.id.sub);
        nestedScrollView = (NestedScrollView) coordinator.findViewById(R.id.nestedScrollView);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mBehavior.setHideable(mCancelable);
        if (params == null) {
            //bottomSheet.addView(view);
            nestedScrollView.addView(view);
        } else {
            //bottomSheet.addView(view, params);
            nestedScrollView.addView(view, params);
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        coordinator.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
                    cancel();
                }
            }
        });
        /*
        // Handle accessibility events
        ViewCompat.setAccessibilityDelegate(bottomSheet, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(View host,
                                                          AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                if (mCancelable) {
                    info.addAction(AccessibilityNodeInfoCompat.ACTION_DISMISS);
                    info.setDismissable(true);
                } else {
                    info.setDismissable(false);
                }
            }

            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfoCompat.ACTION_DISMISS && mCancelable) {
                    cancel();
                    return true;
                }
                return super.performAccessibilityAction(host, action, args);
            }
        });
        */
        /*
        bottomSheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // coordinator intecept, 这没用
                return true;
            }
        });
        */
        return container;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                TypedArray a = getContext().obtainStyledAttributes(
                        new int[]{android.R.attr.windowCloseOnTouchOutside});
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    private static int getThemeResId(Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            TypedValue outValue = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.support.design.R.attr.bottomSheetDialogTheme, outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                // bottomSheetDialogTheme is not provided; we default to our light theme
                themeId = android.support.design.R.style.Theme_Design_Light_BottomSheetDialog;
            }
        }
        return themeId;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                cancel();
            }
        }

        /**
         *
         * @param bottomSheet
         * @param slideOffset float: The new offset of this bottom sheet within [-1,1] range.
         *                    Offset increases as this bottom sheet is moving upward.
         *                    From 0 to 1 the sheet is between collapsed and expanded states and
         *                    from -1 to 0 it is between hidden and collapsed states.
         */
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (slideOffset > 0)
            {
            //    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            Log.w("SLIDE", slideOffset + "");

            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && slideOffset < 1.0)
            {
                // Collapse first
                //mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

}