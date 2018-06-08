package com.lsh.fillette.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.util.exception.KakaoException;
import com.lsh.fillette.AppManaging.BlankFragment;
import com.lsh.fillette.Filtering.FilterCamera;
import com.lsh.fillette.R;
import com.lsh.fillette.databinding.ActivityMyPageBinding;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by lsh on 2018-03-05.
 * Main Activity.
 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private ActivityMyPageBinding binding;

    private FirebaseUser currentUser;
    private LoginButton loginButton;
    private ProgressBar loginProgress;
    private RelativeLayout bg;
    private ViewPager vp;
    private pagerAdapter mPagerAdapter;
    public int curr = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, getKeyHash(this));

        // Kakao session callback method
        Session.getCurrentSession().addCallback(new KakaoSessionCallback());

        // Check firebase auth
        Firebase.setAndroidContext(this);

        // Binding data
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page);

        bg = findViewById(R.id.activity_main);
        vp = findViewById(R.id.vp);
        loginButton = findViewById(R.id.login_button);
        loginProgress = findViewById(R.id.loginProgressBar);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            mPagerAdapter = new pagerAdapter(getSupportFragmentManager());
            vp.setAdapter(mPagerAdapter);
            vp.setCurrentItem(curr);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            vp.setAdapter(new pagerAdapter2(getSupportFragmentManager()));
            vp.setCurrentItem(curr);
        }
    };

    /**
     * View pager adapter
     */
    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FilterMarketFragment();
                case 1:
                    return new FilterCamera();
                default:
                    return new FilterCamera();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class pagerAdapter2 extends FragmentStatePagerAdapter {
        public pagerAdapter2(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FilterMarketFragment();
                case 1:
                    return new BlankFragment();
                default:
                    return new FilterMarketFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    /**
     * Update user's UI from kakao information. Show Login Button if not logged in.
     */
    private void updateUI() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("해당 권한을 설정하지 않을시 본 어플리케이션을 이용하실 수 없습니다.")
                    .setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check();
            bg.setBackgroundColor(Color.WHITE);
            findViewById(R.id.frame_progress).setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        } else {
            vp.setAdapter(null);
            findViewById(R.id.frame_progress).setVisibility(View.VISIBLE);
            bg.setBackgroundResource(R.drawable.login_1);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * OnActivityResult() should be overridden for Kakao Login because Kakao Login uses startActivityForResult().
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param kakaoAccessToken Access token retrieved after successful Kakao Login
     * @return Task object that will call validation server and retrieve firebase token
     */
    private Task<String> getFirebaseJwt(final String kakaoAccessToken) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.server_url) + "/verifyToken";
        HashMap<String, String> validationObject = new HashMap<>();
        validationObject.put("token", kakaoAccessToken);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(validationObject), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String firebaseToken = response.getString("firebase_token");
                    source.setResult(firebaseToken);
                } catch (Exception e) {
                    source.setException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                source.setException(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", kakaoAccessToken);
                return params;
            }
        };
        queue.add(request);
        return source.getTask();
    }

    /**
     * Session callback class for Kakao Login. OnSessionOpened() is called after successful login.
     */
    private class KakaoSessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            loginButton.setVisibility(View.INVISIBLE);
            showProgress(true);
            //t = Toast.makeText(getApplicationContext(), "서버에 로그인 요청 중입니다...", Toast.LENGTH_SHORT);

            String accessToken = Session.getCurrentSession().getAccessToken();
            getFirebaseJwt(accessToken).continueWithTask(new Continuation<String, Task<AuthResult>>() {
                @Override
                public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                    String firebaseToken = task.getResult();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    return auth.signInWithCustomToken(firebaseToken);
                }
            }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        updateUI();
                    } else {
                        if (task.getException() != null) {
                            Log.e(TAG, task.getException().toString());
                        }
                        loginButton.setVisibility(View.VISIBLE);
                    }
                    showProgress(false);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e(TAG, "카카오 인증 에러 " + exception.toString());
            }
        }
    }

    /**
     * Progressing view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_page, menu);
        return true;
    }

    public void setUI() {
        updateUI();
    }

    public ActivityMyPageBinding getBinding() {
        return this.binding;
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}