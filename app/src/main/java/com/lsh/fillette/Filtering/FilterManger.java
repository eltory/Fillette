package com.lsh.fillette.Filtering;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by lsw on 2018. 5. 26..
 * Manager of filter data.
 */

public class FilterManger {
    private static final FilterManger ourInstance = new FilterManger();
    public static final String APP_SAVE_NAME = "FILLETTE";
    private String path = null;
    private final Gson gson = new Gson();

    public static FilterManger getInstance() {
        return ourInstance;
    }

    private FilterManger() {
    }

    public void save(Context context, String key, String name, String data) {
        HashMap<String, String> filter = load(context, key);
        filter.put(name, data);

        String hashMapString = gson.toJson(filter);
        SharedPreferences pref = context.getSharedPreferences(APP_SAVE_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(key, hashMapString).commit();
    }

    public HashMap load(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(APP_SAVE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        if(gson.fromJson(pref.getString(key, ""), type) == null)
            return new HashMap<String, String>();
        return gson.fromJson(pref.getString(key, ""), type);
    }

    public String requestProfile() {
        KakaoTalkService.getInstance().requestProfile(new TalkResponseCallback<KakaoTalkProfile>() {
            @Override
            public void onNotKakaoTalkUser() {

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(KakaoTalkProfile result) {
                path = result.getProfileImageUrl();
            }
        });
        return path;
    }
}
