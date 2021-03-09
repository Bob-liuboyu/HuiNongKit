package com.project.arch_repo.widget.bottomationdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.project.arch_repo.R;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.util.List;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * 模仿ios 下部菜单
 */

public class BottomActionDialog extends AppCompatDialog {

    private BottomActionDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private BottomActionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public BottomActionDialog(@NonNull Context context,
                              @NonNull List<? extends ItemMenu> actionItems) {
        super(context, R.style.arch_AnimBottomDialog);
        this.actionItems = actionItems;
    }

    RecyclerView dialogRecyclerView;
    View tv_item_cancel;
    ActionItemAdapter actionItemAdapter;
    List<? extends ItemMenu> actionItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arch_dialog_bottom_action);

        Window win = getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setWindowAnimations(R.style.arch_AnimBottomDialog);
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.BOTTOM);
        win.getAttributes().dimAmount = 0.5f;
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setCanceledOnTouchOutside(true);
        win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        dialogRecyclerView = findViewById(R.id.dialog_recyclerView);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogRecyclerView.setAdapter(actionItemAdapter = new ActionItemAdapter());
        actionItemAdapter.bindData(true, actionItems);

        actionItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View view, int position) {
                dismiss();
                ItemMenu itemMenu = (ItemMenu) actionItemAdapter.getItem(position);
                itemMenu.doAction();
            }
        });

        tv_item_cancel = findViewById(R.id.tv_item_cancel);
        tv_item_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        closeKeyboard();

        super.show();
    }

    private void closeKeyboard() {
        try {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ActionItemAdapter<T extends ItemMenu> extends BaseRecyclerAdapter<T> {

        @Override
        public int bindView(int viewtype) {
            return R.layout.arch_adapter_item_bottom_action;
        }

        @Override
        public void onBindHolder(BaseViewHolder holder, @Nullable T t, int index) {
            TextView tvActionBottomDialog = holder.obtainView(R.id.tv_item_title);
            tvActionBottomDialog.setText(t.getItemTitle());
            tvActionBottomDialog.setTextColor(t.getItemColor());
            tvActionBottomDialog.setEnabled(!t.isItemDisable());
        }
    }
}
