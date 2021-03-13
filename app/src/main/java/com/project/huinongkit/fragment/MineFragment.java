package com.project.huinongkit.fragment;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.project.arch_repo.base.fragment.BaseFragment;
import com.project.common_resource.UserInfoModel;
import com.project.huinongkit.databinding.MainFragmentMineBinding;

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
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setCompany("北京神州慧达信息技术有限公司");
        userInfoModel.setId("1234");
        userInfoModel.setName("刘伯羽");
        mBinding.setModel(userInfoModel);
        mBinding.tvVersion.setText("版本号：" + getAppVersionCode(getActivity()));
        mBinding.tvSupport.setText("技术支持：400-5678-8980");
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData() {

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        String versionName;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
//             versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }
}
