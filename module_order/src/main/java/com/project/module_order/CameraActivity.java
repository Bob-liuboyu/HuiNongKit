package com.project.module_order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.arch_repo.base.activity.BaseActivity;
import com.project.module_order.databinding.ActivityCameraBinding;

import java.io.File;
import java.io.IOException;

/**
 * @fileName: PhotoActivity
 * @author: liuboyu
 * @date: 2021/3/11 11:07 AM
 * @description: 照片采样
 */
public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private CameraUtil cameraInstance;
    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;
    /**
     * 图片宽高
     */
    private int picWidth;

    /**
     * 是否有界面
     */
    private boolean isView = true;
    /**
     * 拍照id  1： 前摄像头  0：后摄像头
     */
    private int mCameraId = 0;
    /**
     * 闪光灯类型 0 ：关闭 1： 打开 2：自动
     */
    private int light_type = 0;

    /**
     * 图片高度
     */
    private int picHeight;

    ActivityCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
        setListener();
    }

    public void setListener(){
        if(binding == null){
            return;
        }
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (light_type) {
                    case 0:
                        //关闭
                        cameraInstance.turnLightOff(mCamera);
                        break;
                    case 1:
                        cameraInstance.turnLightOn(mCamera);
                        break;
                    case 2:
                        //自动
                        cameraInstance.turnLightAuto(mCamera);
                        break;
                    default:
                        break;
                }
                takePhoto();
            }
        });
        binding.cameraFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraId == 1) {
                    Toast.makeText(CameraActivity.this, "请切换到后置摄像头", Toast.LENGTH_LONG).show();
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_type) {
                    case 0:
                        //打开
                        light_type = 1;
                        binding.cameraFlash.setImageResource(R.mipmap.icon_camera_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //自动
                        light_type = 2;
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        mCamera.setParameters(parameters);
                        binding.cameraFlash.setImageResource(R.mipmap.icon_camera_a);
                        break;
                    case 2:
                        //关闭
                        light_type = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        binding.cameraFlash.setImageResource(R.mipmap.icon_camera_off);
                        break;
                    default:
                        break;
                }
            }
        });

        binding.cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initView() {
        mHolder = binding.surfaceView.getHolder();
        mHolder.addCallback(this);
    }

    protected void initData() {
        cameraInstance = CameraUtil.getInstance();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    /**
     * 切换前后摄像头
     */
    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % Camera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isView = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = cameraInstance.setTakePicktrueOrientation(mCameraId, bitmap);
                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, screenHeight, true);
                String imgpath = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +
                        File.separator + System.currentTimeMillis() + ".jpeg";
                Log.e(TAG, "imgpath: ---  " + imgpath);
                BitmapUtils.saveJPGE_After(getApplicationContext(), saveBitmap, imgpath, 100);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }
                Intent intent = new Intent();
                intent.putExtra(AppConstant.KEY.IMG_PATH, imgpath);
                intent.putExtra(AppConstant.KEY.PIC_WIDTH, picWidth);
                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
                setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            cameraInstance.setCameraDisplayOrientation(this, mCameraId, camera);
            camera.startPreview();
            isView = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
     * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
     * previewSize.width才是surfaceView的高度
     * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        //根据屏幕尺寸获取最佳 大小
        Camera.Size previewSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPreviewSizes(),
                screenHeight, screenWidth);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPictureSizes(),
                screenHeight,screenWidth);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
        camera.setParameters(parameters);
//        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
        picWidth = pictrueSize.width;
        picHeight = pictrueSize.height;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,
                (screenWidth * pictrueSize.width) / pictrueSize.height);
        binding.surfaceView.setLayoutParams(params);
    }

    /**
     * 获取Camera实例
     *
     * @return Camera
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            Log.e(TAG, "getCamera: " + e);
        }
        return camera;
    }

}
