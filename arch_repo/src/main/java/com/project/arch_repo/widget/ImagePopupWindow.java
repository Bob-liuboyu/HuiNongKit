package com.project.arch_repo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import com.project.arch_repo.databinding.ArchImagePopupWindowBinding;

public class ImagePopupWindow extends PopupWindow {
    ArchImagePopupWindowBinding binding;

    public ImagePopupWindow(Context context) {
        super(context);
        binding = ArchImagePopupWindowBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);
    }

}