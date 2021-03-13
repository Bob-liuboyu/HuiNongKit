package com.project.huinongkit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.databinding.MainActivityMainBinding;
import com.project.huinongkit.fragment.MainFragment;
import com.project.huinongkit.fragment.MineFragment;
import com.xxf.arch.widget.BaseFragmentAdapter;

import java.util.Arrays;

@Route(path = ArouterConfig.Main.MAIN)
public class MainActivity extends BaseActivity {
    private MainActivityMainBinding mBinding;
    private BaseFragmentAdapter mBaseFragmentAdapter;
    private MainFragment mainFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initFragment();
        initView();
    }

    private void initFragment() {
        mainFragment = MainFragment.newInstance();
        mineFragment = MineFragment.newInstance();
    }

    private void initView() {
        mBaseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        mBaseFragmentAdapter.bindData(true, Arrays.asList(mainFragment, mineFragment));
        mBaseFragmentAdapter.bindTitle(true, Arrays.asList("首页", "我的"));
        mBinding.mViewPager.setAdapter(mBaseFragmentAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.mViewPager);
    }
}