package com.project.arch_repo.base.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description databinding的adaptet
 * please use {@link BaseBindableAdapter}
 */
@Deprecated
public abstract class BaseBindAdapter<T> extends BaseRecyclerAdapter<T> {

    @Override
    public BaseViewHolder onCreateHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), bindView(viewType), viewGroup, false);
        BaseViewHolder viewHolder = new BaseViewHolder(this, inflate.getRoot(), true);
        viewHolder.setBinding(inflate);
        return viewHolder;
    }
}
