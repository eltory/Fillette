package com.lsh.fillette.AppManaging;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lsh.fillette.R;

/**
 * Created by jjw on 2018-06-05.
 * App information
 */

public class AppInfo extends AppCompatActivity {

    public String cVersion;
    public String lVersion;
    TextView currVersion;
    TextView latestVersion;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // 버젼 가져오기
        lVersion = getMarketVersion();
        cVersion = getCurrAppVersion();
        currVersion = findViewById(R.id.current_version);
        latestVersion = findViewById(R.id.lately_version);
        update = findViewById(R.id.update);
        currVersion.setText("현재 버전 : " + cVersion);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lVersion != null)
                    latestVersion.setText("최신 버전 : " + lVersion);
                else
                    latestVersion.setText("최신 버전을 받아올 수 없습니다.");
            }
        });

        // 최신버전으로 업데이트 하기
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lVersion != null && !lVersion.equals(cVersion)) {
                    Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                    marketLaunch.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.lsh.kkotest"));
                    startActivity(marketLaunch);
                } else {
                    Toast.makeText(AppInfo.this, "이미 최신버전 입니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String getMarketVersion() {
        return CheckUpdate.getMarketVersion("com.lsh.kkotest");
    }

    public String getCurrAppVersion() {
        String device_version = null;

        try {
            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return device_version;
    }
}