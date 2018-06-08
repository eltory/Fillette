package com.lsh.fillette.Filtering;


/**
 * Created by lsh on 2018. 4. 9..
 * Real graphical rendering data for filtering.
 */

public class FilterData {
    // Preview view aspect ration.
    public final float mAspectRatioPreview[] = new float[2];
    // Filter values.
    public float mBrightness, mContrast, mSaturation, mCornerRadius, mRed, mGreen, mBlue, mAlpha, mX;
    // Device orientation degree.
    public int mOrientationDevice;
    // Camera orientation matrix.
    public final float mOrientationM[] = new float[16];
}
