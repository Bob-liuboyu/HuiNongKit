package com.project.huinongkit.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.fragment.BaseFragment;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.UserInfoModel;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.LoginResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.databinding.MainFragmentMineBinding;
import com.project.user.LoginActivity;

/**
 * @fileName: MainFragment
 * @author: liuboyu
 * @date: 2021/3/13 2:18 PM
 * @description: 我的
 */
public class MineFragment extends BaseFragment {
    private MainFragmentMineBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainFragmentMineBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        initData();
    }

    private void initView() {
        LoginResDTO.UserinfoBean userInfo = GlobalDataManager.getInstance().getUserInfo();
        LoginResDTO.SettingsBean settings = GlobalDataManager.getInstance().getSettings();
        mBinding.setModel(userInfo);

        if (settings != null) {
            mBinding.tvVersion.setText("版本号：" + settings.getVersion());
            mBinding.tvSupport.setText("技术支持：" + settings.getPhone_support());
        }
        mBinding.tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrDialogUtils.createCommonDialog(getActivity(), "退出登陆", "确认退出登陆吗？", "退出", "取消", new CommonDialog.OnDialogClickListener() {
                    @Override
                    public void onClickConfirm(View view) {
                        GlobalDataManager.getInstance().updateInfo(null);
                        SharedPreferencesUtils.setBooleanValue(getActivity(), "login", false);
                        ARouter.getInstance().build(ArouterConfig.User.LOGIN)
                                .navigation();
                        getActivity().finish();
                    }

                    @Override
                    public void onClickCancel(View view) {

                    }
                }).show();
            }
        });
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData() {

    }
}
