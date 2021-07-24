package com.sainivik.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sainivik.adapter.SelectLanguageAdapter
import com.sainivik.exoplayersample.R
import com.sainivik.exoplayersample.databinding.SelectLanguageDialogBinding


class Alerts {

    companion object {


        fun changeLanguageDialog(context: Context, adapter: SelectLanguageAdapter): Dialog {
            var dialog = BottomSheetDialog(context, R.style.SheetDialog)
            var binding = DataBindingUtil.inflate<SelectLanguageDialogBinding>(
                LayoutInflater.from(context),
                R.layout.select_language_dialog,
                null,
                false
            )
            dialog.setContentView(binding.root)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.setGravity(Gravity.BOTTOM)
            // dialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            binding.adapter = adapter
            dialog.show()

            return dialog

        }


    }
}

