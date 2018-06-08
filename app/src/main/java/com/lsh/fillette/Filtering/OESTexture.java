package com.lsh.fillette.Filtering;

import android.opengl.GLES20;

/**
 * Created by lsh on 2018. 3. 19..
 * Texture for camera input.
 */

public class OESTexture {
    private int mTextureHandle;

    public OESTexture() {
        // TODO Auto-generated constructor stub
    }

    public int getTextureId() {
        return mTextureHandle;
    }

    public void init() {
        int[] mTextureHandles = new int[1];
        GLES20.glGenTextures(1, mTextureHandles, 0);
        mTextureHandle = mTextureHandles[0];

        GLES20.glBindTexture(GLES20.GL_SAMPLER_2D, mTextureHandles[0]);
        GLES20.glTexParameteri(GLES20.GL_SAMPLER_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_SAMPLER_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_SAMPLER_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_SAMPLER_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    }
}

