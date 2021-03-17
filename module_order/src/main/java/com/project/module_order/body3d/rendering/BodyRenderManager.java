/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.module_order.body3d.rendering;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.huawei.hiar.ARBody;
import com.huawei.hiar.ARCamera;
import com.huawei.hiar.ARFrame;
import com.huawei.hiar.ARImage;
import com.huawei.hiar.ARPointCloud;
import com.huawei.hiar.ARSceneMesh;
import com.huawei.hiar.ARSession;
import com.huawei.hiar.ARTrackable;
import com.project.module_order.common.ArDemoRuntimeException;
import com.project.module_order.common.DisplayRotationManager;
import com.project.module_order.common.TextDisplay;
import com.project.module_order.common.TextureDisplay;
import com.project.module_order.utils.LogToFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES10.GL_NEAREST;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexImage2D;
import static android.opengl.GLES11.glTexParameteri;
import static android.opengl.GLES30.GL_R16UI;
import static android.opengl.GLES30.GL_RED_INTEGER;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_SHORT;

/**
 * This class renders personal data obtained by the AR Engine.
 *
 * @author HW
 * @since 2020-03-21
 */
public class BodyRenderManager implements GLSurfaceView.Renderer {
    private static final String TAG = BodyRenderManager.class.getSimpleName();

    private static final int PROJECTION_MATRIX_OFFSET = 0;

    private static final float PROJECTION_MATRIX_NEAR = 0.1f;

    private static final float PROJECTION_MATRIX_FAR = 100.0f;

    private static final float UPDATE_INTERVAL = 0.5f;

    private int frames = 0;

    private long lastInterval;

    private ARSession mSession;

    private float fps;

    private Activity mActivity;

    private TextView mTextView;

    private TextureDisplay mTextureDisplay = new TextureDisplay();

    private TextDisplay mTextDisplay = new TextDisplay();

    private ArrayList<BodyRelatedDisplay> mBodyRelatedDisplays = new ArrayList<>();

    private DisplayRotationManager mDisplayRotationManager;

    private boolean dealDepth;
    public static final float NEAR = 0.1f, FAR = 3f;

    /**
     * The constructor passes activity.
     *
     * @param activity Activity
     */
    public BodyRenderManager(Activity activity) {
        mActivity = activity;
        BodyRelatedDisplay bodySkeletonDisplay = new BodySkeletonDisplay();
        BodyRelatedDisplay bodySkeletonLineDisplay = new BodySkeletonLineDisplay();
        mBodyRelatedDisplays.add(bodySkeletonDisplay);
        mBodyRelatedDisplays.add(bodySkeletonLineDisplay);
    }

    /**
     * Set the AR session to be updated in onDrawFrame to obtain the latest data.
     *
     * @param arSession ARSession.
     */
    public void setArSession(ARSession arSession) {
        if (arSession == null) {
            Log.e(TAG, "Set session error, arSession is null!");
            return;
        }
        mSession = arSession;
    }

    /**
     * Set displayRotationManage, which is used in onSurfaceChanged and onDrawFrame.
     *
     * @param displayRotationManager DisplayRotationManage.
     */
    public void setDisplayRotationManage(DisplayRotationManager displayRotationManager) {
        if (displayRotationManager == null) {
            Log.e(TAG, "Set display rotation manage error, display rotation manage is null!");
            return;
        }
        mDisplayRotationManager = displayRotationManager;
    }

    /**
     * Set TextView, which is called in the UI thread to display data correctly.
     *
     * @param textView TextView.
     */
    public void setTextView(TextView textView) {
        if (textView == null) {
            Log.e(TAG, "Set textView error, text view is null!");
            return;
        }
        mTextView = textView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Clear the color and set the window color.
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        for (BodyRelatedDisplay bodyRelatedDisplay : mBodyRelatedDisplays) {
            bodyRelatedDisplay.init();
        }
        mTextureDisplay.init();
        mTextDisplay.setListener(new TextDisplay.OnTextInfoChangeListener() {
            @Override
            public void textInfoChanged(String text, float positionX, float positionY) {
                showBodyTypeTextView(text, positionX, positionY);
            }
        });
    }

    /**
     * The OpenGL thread calls back the UI thread to display text.
     *
     * @param text      Gesture information displayed on the screen
     * @param positionX The left padding in pixels.
     * @param positionY The right padding in pixels.
     */
    private void showBodyTypeTextView(final String text, final float positionX, final float positionY) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setTextColor(Color.WHITE);

                // Set the font size.
                mTextView.setTextSize(10f);
                if (text != null) {
                    mTextView.setText(text);
                    mTextView.setPadding((int) positionX, (int) positionY, 0, 0);
                } else {
                    mTextView.setText("");
                }
            }
        });
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mTextureDisplay.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        mDisplayRotationManager.updateViewportRotation(width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Clear the screen to notify the driver not to load pixels of the previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mSession == null) {
            return;
        }
        if (mDisplayRotationManager.getDeviceRotation()) {
            mDisplayRotationManager.updateArSessionDisplayGeometry(mSession);
        }

        try {
            mSession.setCameraTextureName(mTextureDisplay.getExternalTextureId());
            ARFrame frame = mSession.update();

            // The size of the projection matrix is 4 * 4.
            float[] projectionMatrix = new float[16];
            ARCamera camera = frame.getCamera();

            // Obtain the projection matrix of ARCamera.
            camera.getProjectionMatrix(projectionMatrix, PROJECTION_MATRIX_OFFSET, PROJECTION_MATRIX_NEAR,
                    PROJECTION_MATRIX_FAR);
            mTextureDisplay.onDrawFrame(frame);
            Collection<ARBody> bodies = mSession.getAllTrackables(ARBody.class);
            if (bodies.size() == 0) {
                mTextDisplay.onDrawFrame(null);
                return;
            }

            // 获取深度图像]
            getBgDepthMillimeters(frame);

            for (ARBody body : bodies) {
                if (body.getTrackingState() != ARTrackable.TrackingState.TRACKING) {
                    continue;
                }

                // Update the body recognition information to be displayed on the screen.
                StringBuilder sb = new StringBuilder();
                updateMessageData(sb, body);

                // Display the updated body information on the screen.
                mTextDisplay.onDrawFrame(sb);
            }
            for (BodyRelatedDisplay bodyRelatedDisplay : mBodyRelatedDisplays) {
                bodyRelatedDisplay.onDrawFrame(bodies, projectionMatrix);
            }
        } catch (ArDemoRuntimeException e) {
            Log.e(TAG, "Exception on the ArDemoRuntimeException!");
        } catch (Throwable t) {
            // This prevents the app from crashing due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread");
        }
    }

    /**
     * Update gesture-related data for display.
     *
     * @param sb   String buffer.
     * @param body ARBody
     */
    private void updateMessageData(StringBuilder sb, ARBody body) {
        float fpsResult = doFpsCalculate();
        sb.append("FPS=").append(fpsResult).append(System.lineSeparator());
        int bodyAction = body.getBodyAction();
        sb.append("bodyAction=").append(bodyAction).append(System.lineSeparator());
    }

    private float doFpsCalculate() {
        ++frames;
        long timeNow = System.currentTimeMillis();

        // Convert millisecond to second.
        if (((timeNow - lastInterval) / 1000.0f) > UPDATE_INTERVAL) {
            fps = frames / ((timeNow - lastInterval) / 1000.0f);
            frames = 0;
            lastInterval = timeNow;
        }
        return fps;
    }

    public void getDepthImage() {
        dealDepth = true;
    }

    private void getDepthImage(ARFrame frame) {
        if (!dealDepth) {
            return;
        }
        dealDepth = false;
        try {
            Image depthImage = frame.acquireDepthImage();

            ShortBuffer shortDepthBuffer = depthImage.getPlanes()[0].getBuffer().asShortBuffer();
            short depthSample = shortDepthBuffer.get();
            short depthRange = (short) (depthSample & 0x1FFF);
            short depthConfidence = (short) ((depthSample >> 13) & 0x7);
            float depthPercentage = depthConfidence == 0 ? 1.f : (depthConfidence - 1) / 7.f;

            Log.e("xxxxxxxxx", depthRange + " , " + depthConfidence + " , " + depthPercentage);

        } catch (Exception e) {
            Log.e("xxxxxxxxx", "NotYetAvailableException = " + e);
        }
    }


    public void createOnGlThread() {
        int[] textureId = new int[1];
        glGenTextures(1, textureId, 0);
        int depthTextureId = textureId[0];
        glBindTexture(GL_TEXTURE_2D, depthTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    }

    public void update(Image depthImage, int depthTextureId) {
        try {
            int depthTextureWidth = depthImage.getWidth();
            int depthTextureHeight = depthImage.getHeight();
//            String msg = String.format("width: %s, height: %s", depthTextureWidth, depthTextureHeight);
//            Log.d("render", msg);
            glBindTexture(GL_TEXTURE_2D, depthTextureId);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_R16UI,
                    depthTextureWidth,
                    depthTextureHeight,
                    0,
                    GL_RED_INTEGER,
                    GL_UNSIGNED_SHORT,
                    depthImage.getPlanes()[0].getBuffer().asShortBuffer());
            depthImage.close();
        } catch (Exception e) {
            Log.e("xxxxxxxxx", "Exception = " + e);
        }
    }


    public void getBgDepthMillimeters(ARFrame frame) {
        if (!dealDepth) {
            return;
        }
        dealDepth = false;
        try {
            StringBuffer sb = new StringBuffer();
            ARImage depthImage = (ARImage) frame.acquireDepthImage();
            int imwidth = depthImage.getWidth();
            int imheight = depthImage.getHeight();
//            ARPointCloud arPointCloud = frame.acquirePointCloud();
            ARSceneMesh arSceneMesh = frame.acquireSceneMesh();
            ShortBuffer shortDepthBuffer = arSceneMesh.getSceneDepth();
            Bitmap disBimap = Bitmap.createBitmap(imwidth, imheight, Bitmap.Config.RGB_565);
            for (int i = 0; i < imheight; i++) {
                for (int j = 0; j < imwidth; j++) {
                    int index = (i * imwidth + j);
                    shortDepthBuffer.position(index);
                    short depthSample = shortDepthBuffer.get();
                    short depthRange = (short) (depthSample & 0x1FFF);
                    byte value = (byte) (depthRange / 8000f * 255);
                    disBimap.setPixel(j, i, Color.rgb(value, value, value));
                }
            }
//            saveBitmapToDisk(disBimap, generateFilename());
        } catch (Exception e) {
            Log.e("xxxxxxxxx", "NotYetAvailableException = " + e);
        }
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {
        Log.e("xxxxxxxxx", "filename = " + filename);
        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private String generateFilename() {
        return Environment.getExternalStorageDirectory() + "/DCIM/HuiNongKit/"
                + System.currentTimeMillis() + ".jpg";
    }
}