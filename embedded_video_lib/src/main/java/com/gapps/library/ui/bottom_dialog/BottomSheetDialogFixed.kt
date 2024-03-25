package com.gapps.library.ui.bottom_dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.gapps.library.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetDialogFixed : BottomSheetDialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.run {
            findViewById<View>(com.google.android.material.R.id.container)?.fitsSystemWindows = false
            findViewById<View>(com.google.android.material.R.id.coordinator)?.fitsSystemWindows = false
        }

        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            val dialogWidth = context.resources.getDimensionPixelSize(R.dimen.vna_bv_dialog_width)
            it.layoutParams = it.layoutParams.apply {
                if (dialogWidth > 0) {
                    width = context.resources.getDimensionPixelSize(R.dimen.vna_bv_dialog_width)
                }
            }
        }
    }

    override fun show() {
        super.show()
        // androidx should use: com.google.android.material.R.id.design_bottom_sheet
        val view = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        view?.post {
            val behavior = BottomSheetBehavior.from(view)
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }
}
