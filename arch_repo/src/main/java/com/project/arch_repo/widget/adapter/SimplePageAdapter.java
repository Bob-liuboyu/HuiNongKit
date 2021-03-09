

package com.project.arch_repo.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SimplePageAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mItems;

    public SimplePageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setItems(List<T> items) {
        this.mItems = items;
    }

    @Override
    public T getItem(int position) {
        return (T) this.mItems.get(position);
    }

    @Override
    public int getCount() {
        return this.mItems == null ? 0 : this.mItems.size();
    }
}
