package com.project.huinongkit.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.fragment.BaseFragment;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.global.ConstantData;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.PgyerApkUpdateInfoModel;
import com.project.common_resource.response.LoginResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.BuildConfig;
import com.project.huinongkit.databinding.MainFragmentMineBinding;
import com.project.huinongkit.source.impl.PgyerlRepositoryImpl;
import com.project.user.LoginActivity;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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
    }

    private void initView() {
        LoginResDTO.UserInfoBean userInfo = GlobalDataManager.getInstance().getUserInfo();
        LoginResDTO.SettingsBean settings = GlobalDataManager.getInstance().getSettings();
        mBinding.setModel(userInfo);

        if (userInfo.getCompanyName() == null || "".equals(userInfo.getCompanyName())) {
            mBinding.tvCompany1.setVisibility(View.GONE);
        } else {
            mBinding.tvCompany1.setVisibility(View.VISIBLE);
        }

        if (settings != null) {
            mBinding.tvVersion.setText("版本号：" + BuildConfig.VERSION_NAME);
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

        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();
            }
        });
    }

    private void checkUpdate() {
        PgyerlRepositoryImpl.getInstance()
                .checkUpdate(ConstantData.API_KEY, ConstantData.APP_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<PgyerApkUpdateInfoModel>bindToLifecycle(this))
                .compose(XXF.<PgyerApkUpdateInfoModel>bindToErrorNotice())
                .compose(XXF.<PgyerApkUpdateInfoModel>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("检查更新...")))
                .subscribe(new Consumer<PgyerApkUpdateInfoModel>() {
                    @Override
                    public void accept(final PgyerApkUpdateInfoModel model) throws Exception {
                        if (model == null || model.getCode() != 0 || model.getData() == null) {
                            return;
                        }
                        Float webVersion = Float.valueOf(model.getData().getBuildVersion());
                        Float localVersion = Float.valueOf(BuildConfig.VERSION_NAME);
                        if (localVersion >= webVersion) {
                            ToastUtils.showToast("已经是最新版本");
                            return;
                        }

                        if (TextUtils.isEmpty(model.getData().getDownloadURL())) {
                            return;
                        }

                        GrDialogUtils.createCommonDialog(getActivity(), "检查", "检测到新版本，是否更新到最新？","更新","取消", new CommonDialog.OnDialogClickListener() {
                            @Override
                            public void onClickConfirm(View view) {
                                downloadApk(model.getData().getDownloadURL());
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

    private static void downloadApk(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            XXF.getActivityStackProvider().getRootActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("未找到相关App来打开");
        }
    }
}
