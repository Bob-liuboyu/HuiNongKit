package com.project.module_order.ui;

import android.media.Image;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;

import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingFailureReason;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.NotYetAvailableException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.project.module_order.R;
import com.project.module_order.helpers.CameraPermissionHelper;
import com.project.module_order.helpers.TrackingStateHelper;
import com.project.module_order.samplerender.SampleRender;

import org.jetbrains.annotations.NotNull;

/**
 * @fileName: TestAR2
 * @author: liuboyu
 * @date: 2021/3/12 3:43 PM
 * @description:
 */
public class TestAR2 extends AppCompatActivity {
    private Session arSession;
    private Button btn_ar;
    private GLSurfaceView surfaceView;
    private boolean userRequestedInstall = true;
    private SampleRender render;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("findMe", "123");
        this.setContentView(R.layout.activity_test);
        this.btn_ar = (Button) this.findViewById(R.id.btn_ar);
        this.surfaceView = (GLSurfaceView) this.findViewById(R.id.surfaceView);
        Log.d("findMe", "Good");
        // Set up renderer.
        render = new SampleRender(surfaceView, renderer, getAssets());
        maybeEnableArButton();
    }

    private SampleRender.Renderer renderer = new SampleRender.Renderer() {

        @Override
        public void onSurfaceCreated(SampleRender render) {

        }

        @Override
        public void onSurfaceChanged(SampleRender render, int width, int height) {

        }

        @Override
        public void onDrawFrame(SampleRender render) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Log.d("findMe", "hey");
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

        try {
            if (arSession == null) {
                switch (ArCoreApk.getInstance().requestInstall(this, userRequestedInstall)) {
                    case INSTALL_REQUESTED:
                        Log.d("findMe", "INSTALL_REQUESTED");
                        userRequestedInstall = false;
                        return;
                    case INSTALLED:
                        Log.d("findMe", "INSTALLED");
                        arSession = new Session(this);
                        break;
                }
            }
        } catch (UnavailableUserDeclinedInstallationException e) {
            Log.d("findMe", "TODO: handle UnavailableUserDeclinedInstallationException " + e);
            return;
        } catch (Exception e) {  // Current catch statements.
            Log.d("findMe", "TODO: handle exception " + e);
            return;  // mSession is still null.
        }
    }

    public final void setClickListeners() {
        btn_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfDeviceSupportsDepthApi(arSession);
            }
        });
    }

    public final void maybeEnableArButton() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    maybeEnableArButton();
                }
            }, 200);
        }
        if (availability.isSupported()) {
            btn_ar.setVisibility(View.VISIBLE);
            btn_ar.setEnabled(true);
            setClickListeners();

            // indicator on the button.
        } else { // Unsupported or unknown.
            btn_ar.setVisibility(View.INVISIBLE);
            btn_ar.setEnabled(false);
        }
    }

    public final void checkIfDeviceSupportsDepthApi(@NotNull Session session) {
        Config config = session.getConfig();
        // Check whether the user's device supports the Depth API.
        boolean isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC);
        if (isDepthSupported) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
        try {
            session.configure(config);
            session.resume();
            surfaceView.onResume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
            Log.d("findMe", "CameraNotAvailableException = " + e.getMessage());
        }
        Log.d("findMe", "session.configure(config)");
        //retrieveDepthMaps(session.update());
    }

    public final void retrieveDepthMaps(@NotNull Frame frame) {
        try {
            Image depthImage = frame.acquireDepthImage();
        } catch (NotYetAvailableException e) {
            e.printStackTrace();
        }
    }
}