package com.project.arch_repo.widget.share;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project.arch_repo.R;
import com.project.arch_repo.base.recyclerview.BaseBindAdapter;
import com.project.arch_repo.databinding.ArchAdapterShareItemBinding;
import com.project.arch_repo.databinding.ArchDialogShareBinding;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 分享对话框
 */
public class ShareDialog extends AppCompatDialog implements OnItemClickListener {
    ArchDialogShareBinding binding;
    ShareItemAdapter shareItemAdapter;
    List<? extends IPlatformItem> platformItems;

    public ShareDialog(@NonNull Context context,
                       List<? extends IPlatformItem> platformItems) {
        super(context);
        this.platformItems = platformItems;
    }

    private ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private ShareDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ArchDialogShareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //去除系统自带的margin
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog在界面中的属性
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        binding.dialogRecyclerView.setAdapter(shareItemAdapter = new ShareItemAdapter());
        shareItemAdapter.setOnItemClickListener(this);
        shareItemAdapter.bindData(true, this.platformItems);
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
        dismiss();
        IPlatformItem item = (IPlatformItem) shareItemAdapter.getItem(index);
        if (item != null) {
            item.doShare();
        }
    }


    /**
     * 分享 适配器
     */
    private static class ShareItemAdapter<T extends IPlatformItem>
            extends BaseBindAdapter<T> {

        @Override
        public int bindView(int viewType) {
            return R.layout.arch_adapter_share_item;
        }

        @Override
        public void onBindHolder(BaseViewHolder holder, @Nullable T t, int index) {
            if (t == null) {
                return;
            }
            if (holder.getBinding() instanceof ArchAdapterShareItemBinding) {
                ArchAdapterShareItemBinding itemBinding = holder.getBinding();
                itemBinding.platformIv.setImageResource(t.getPlatformIcon());
                itemBinding.platformTv.setText(t.getPlatformName());
            }
        }
    }
}
