package com.lsh.fillette.AppManaging;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by lsh on 2018. 3. 27..
 * <p>
 * Set up kakao login.
 */

public class KakaoLoginApplication extends Application {
    private static KakaoLoginApplication self;
    public static final String TAG = KakaoLoginApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        Log.d(TAG, "Entered kakao login");
        FirebaseApp.initializeApp(this);
        KakaoSDK.init(new KakaoAdapter() {
            @Override
            public IApplicationConfig getApplicationConfig() {
                return new IApplicationConfig() {
                    @Override
                    public Context getApplicationContext() {
                        return self;
                    }
                };
            }
        });
    }
}
