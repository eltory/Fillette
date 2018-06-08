package com.lsh.fillette.Filtering;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLException;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.lsh.fillette.View.FilterUploadByCamera;
import com.lsh.fillette.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Created by lsh
 * @version 1.0.0
 * @date 2018. 4. 19..
 * @details Filter rendering for preview camera on mobile
 */

public class CameraRenderer extends GLSurfaceView implements
        GLSurfaceView.Renderer,
        SurfaceTexture.OnFrameAvailableListener {
    private Context mContext;
    public static final String TAG = CameraRenderer.class.getName();

    /**
     * @brief Camera and SurfaceTexture
     */
    private Camera mCamera;
    private SurfaceTexture mSurfaceTexture;
    private int mCameraId;

    private final OESTexture mCameraTexture = new OESTexture();
    private final Shader mOffscreenShader = new Shader();
    private final OESTexture mOffscreen = new OESTexture();
    private int mWidth, mHeight;
    private boolean updateTexture = false;
    public boolean snap = false, shot = false;
    private Bitmap picture;
    private FilterData mSharedData;
    private final Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    /**
     * @brief OpenGL params
     */
    private ByteBuffer mFullQuadVertices;
    private float[] mTransformM = new float[16];
    private float[] mOrientationM = new float[16];
    private float[] mRatio = new float[2];
    private int mFrameWidth, mFrameHeight;
    private final float mAspectRatio[] = new float[2];
    protected AudioManager mAudioManager;

    /**
     * @param context current application context
     * @brief constructor
     */
    public CameraRenderer(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * @param context current application context
     * @param attrs   for rendering attributes
     * @brief constructor
     */
    public CameraRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FilterData getmSharedData() {
        return this.mSharedData;
    }

    public void setUpdateTexture(boolean t) {
        this.updateTexture = t;
    }

    public void setCameraFront(boolean frontFacing) {
        int facing = frontFacing ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK;
        mCameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, mCameraInfo);
            if (mCameraInfo.facing == facing) {
                mCameraId = i;
                break;
            }
        }
        openCamera();
    }

    private void openCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        if (mCameraId >= 0) {
            Camera.getCameraInfo(mCameraId, mCameraInfo);
            mCamera = Camera.open(mCameraId);
            Camera.Parameters params = mCamera.getParameters();
            params.setRotation(0);
            if (!isCameraFront())
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
            try {
                if (mSurfaceTexture != null) {
                    mCamera.setPreviewTexture(mSurfaceTexture);
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void flashLightOn() {
        try {
            Camera.Parameters p = mCamera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flashLightOff() {
        try {
            Camera.Parameters p = mCamera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief initializing scene of camera
     */
    private void init() {

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //Create full scene quad buffer
        final byte FULL_QUAD_COORDS[] = {-1, 1, -1, -1, 1, 1, 1, -1};
        mFullQuadVertices = ByteBuffer.allocateDirect(4 * 2);
        mFullQuadVertices.put(FULL_QUAD_COORDS).position(0);

        setPreserveEGLContextOnPause(true);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public boolean isCameraFront() {
        return mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    /**
     * @param surfaceTexture frame texture
     */
    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateTexture = true;
        requestRender();
    }

    /**
     * @param gl     GL10
     * @param config EGLConfig
     * @brief filter surface is created
     */
    @Override
    public synchronized void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //load and compile shader

        try {
            mOffscreenShader.setProgram(R.raw.vshader, R.raw.fshader, mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    /**
     * @param gl     GL10
     * @param width  camera preview width
     * @param height camera preview height
     * @brief filter surface is changed
     */
    @SuppressLint("NewApi")
    @Override
    public synchronized void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;

        mAspectRatio[0] = (float) Math.min(mWidth, mHeight) / mWidth;
        mAspectRatio[1] = (float) Math.min(mWidth, mHeight) / mHeight;

        //generate camera texture------------------------
        mCameraTexture.init();

        //set up surface texture------------------
        SurfaceTexture oldSurfaceTexture = mSurfaceTexture;
        mSurfaceTexture = new SurfaceTexture(mCameraTexture.getTextureId());
        mSurfaceTexture.setOnFrameAvailableListener(this);
        if (oldSurfaceTexture != null) {
            oldSurfaceTexture.release();
        }

        //set camera para-----------------------------------
        int camera_width = 0;
        int camera_height = 0;

        openCamera();
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Camera.Parameters param = mCamera.getParameters();
        mFrameHeight = height;
        mFrameWidth = width;
        List<Camera.Size> psize = param.getSupportedPreviewSizes();

        // TODO : 카메라 프리뷰 사이즈 해상도별로 업데이트 해서 보여주기
        double minDiff = Double.MAX_VALUE;
        for (Camera.Size size : psize) {
            if (Math.abs(size.height - height) < minDiff) {
                mFrameWidth = size.width;
                mFrameHeight = size.height;
                minDiff = Math.abs(size.height - height);
            }
        }
        //param.setPreviewSize(getFrameWidth(), getFrameHeight());
        param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        if (psize.size() > 0) {
            int i;
            for (i = 0; i < psize.size(); i++) {
                if (psize.get(i).width < width || psize.get(i).height < height)
                    break;
            }
            if (i > 0)
                i--;
            param.setPreviewSize(psize.get(i).width, psize.get(i).height);

            camera_width = psize.get(i).width;
            camera_height = psize.get(i).height;
        }

        //TODO: 단말기별 테스트
        //get the camera orientation and display dimension------------
        if (mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            Matrix.setRotateM(mOrientationM, 0, 90.0f, 0f, 0f, 1f);
            mRatio[1] = (float) Math.max(mWidth, mHeight) / mHeight;//camera_height * 1.0f / height;
            mRatio[0] = (float) Math.min(mWidth, mHeight) / mWidth;
            //camera_height * 1.0f / width;
        } else {
            Matrix.setRotateM(mOrientationM, 0, 0.0f, 0f, 0f, 1f);
            mRatio[1] = (float) Math.max(mWidth, mHeight) / mWidth;//camera_width * 1.0f / height;
            mRatio[0] = (float) Math.min(mWidth, mHeight) / mHeight;
        }

        //start camera-----------------------------------------
        mCamera.setParameters(param);
        mCamera.startPreview();

        //start render---------------------
        requestRender();
    }

    public void rand() {

        try {
            mOffscreenShader.setProgram(R.raw.vshader, R.raw.fshader, mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        requestRender();
    }

    /**
     * @param gl GL10
     * @details in real rendering of camera filter
     */
    @Override
    public synchronized void onDrawFrame(GL10 gl) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //render the texture to FBO if new frame is available
        if (updateTexture) {
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(mTransformM);
            updateTexture = false;

            GLES20.glViewport(0, 0, mWidth, mHeight);

            mOffscreenShader.useProgram();

            int uTransformM = mOffscreenShader.getHandle("uTransformM");
            int uOrientationM = mOffscreenShader.getHandle("uOrientationM");
            int uRatioV = mOffscreenShader.getHandle("ratios");

            GLES20.glUniformMatrix4fv(uTransformM, 1, false, mTransformM, 0);
            GLES20.glUniformMatrix4fv(uOrientationM, 1, false, mOrientationM, 0);
            GLES20.glUniform2fv(uRatioV, 1, mRatio, 0);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mCameraTexture.getTextureId());
            renderQuad(mOffscreenShader.getHandle("aPosition"));

            if (snap) {
                snap = false;
                picture = createBitmapFromGLSurface(0, 0, mWidth, mHeight, gl);
                setFolder();
            }
        }

        // Uniform variables.
        int uBrightness = mOffscreenShader.getHandle("uBrightness");
        int uContrast = mOffscreenShader.getHandle("uContrast");
        int uSaturation = mOffscreenShader.getHandle("uSaturation");
        int uCornerRadius = mOffscreenShader.getHandle("uCornerRadius");

        int uAspectRatio = mOffscreenShader.getHandle("uAspectRatio");
        int uAspectRatioPreview = mOffscreenShader.getHandle("uAspectRatioPreview");

        int uRed = mOffscreenShader.getHandle("uRed");
        int uGreen = mOffscreenShader.getHandle("uGreen");
        int uBlue = mOffscreenShader.getHandle("uBlue");
        int uAlpha = mOffscreenShader.getHandle("uAlpha");

        int dX = mOffscreenShader.getHandle("dX");

        GLES20.glUniform1f(uBrightness, mSharedData.mBrightness);
        GLES20.glUniform1f(uContrast, mSharedData.mContrast);
        GLES20.glUniform1f(uSaturation, mSharedData.mSaturation);
        GLES20.glUniform1f(uCornerRadius, mSharedData.mCornerRadius);

        GLES20.glUniform2fv(uAspectRatio, 1, mAspectRatio, 0);
        GLES20.glUniform2fv(uAspectRatioPreview, 1,
                mSharedData.mAspectRatioPreview, 0);

        GLES20.glUniform1f(uRed, mSharedData.mRed);
        GLES20.glUniform1f(uGreen, mSharedData.mGreen);
        GLES20.glUniform1f(uBlue, mSharedData.mBlue);
        GLES20.glUniform1f(uAlpha, mSharedData.mAlpha);

        GLES20.glUniform1f(dX, mSharedData.mX);

        // Use offscreen texture as source.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOffscreen.getTextureId());

        // Trigger actual rendering.
        renderQuad(mOffscreenShader.getHandle("aPosition"));
        if (shot) {
            shot = false;
            FilterUploadByCamera filterUploadByCamera = (FilterUploadByCamera) getContext();
            filterUploadByCamera.setBitmap(picture = createBitmapFromGLSurface(0, 0, mWidth, mHeight, gl));
            //onPause();
        }
    }

    public void setFolder() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        File dir = new File(path + "/Fillette");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String img_name = Calendar.getInstance().getTime().getTime() + ".jpg";
        File file = new File(dir, img_name);
        int vol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.shutter);
        if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT ||
                mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE ||
                mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_PLAY_SOUND);
            mp.start();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fOutputStream = new FileOutputStream(file);
            picture.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);
            fOutputStream.flush();
            fOutputStream.close();
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + path + "/Fillette/" + img_name)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return;
        }finally {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void setSharedData(FilterData sharedData) {
        mSharedData = sharedData;
        requestRender();
    }

    /**
     * @return taken picture
     */
    public Bitmap getPicture() {
        return picture;
    }

    /**
     * @param x  start x point
     * @param y  start y point
     * @param w  width
     * @param h  height
     * @param gl GL10
     * @return bitmap image
     * @throws OutOfMemoryError
     * @details convert YUV format into RGBA format
     */
    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    private void renderQuad(int aPosition) {
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0, mFullQuadVertices);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestRender();
    }

    public void onDestroy() {
        updateTexture = false;
        mSurfaceTexture.release();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }
        mCamera = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }
}