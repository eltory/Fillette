package com.lsh.fillette.AppManaging;

import android.os.StrictMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jjw on 2018-06-05.
 * Auto getting application's latest market version.
 */

public class CheckUpdate {

    public static String getMarketVersion(String packageName) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Document document = Jsoup.connect(
                    "https://play.google.com/store/apps/details?id=" + packageName).get();
            Elements Version = document.select(".htlgb");
            return Version.get(5).text();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMarketVersionFast(String packageName) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String mData = "", mVer = null;

        try {
            URL mUrl = new URL("https://play.google.com/store/apps/details?id=" + packageName);
            HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();

            if (mConnection == null)
                return null;

            mConnection.setConnectTimeout(5000);
            mConnection.setUseCaches(false);
            mConnection.setDoOutput(true);

            if (mConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(mConnection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    mData += line;
                }

                reader.close();
            }
            mConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String startToken = "softwareVersion\">";
        String endToken = "<";
        int index = mData.indexOf(startToken);

        if (index == -1) {
            mVer = null;
        } else {
            mVer = mData.substring(index + startToken.length(), index + startToken.length() + 100);
            mVer = mVer.substring(0, mVer.indexOf(endToken)).trim();
        }
        return mVer;
    }
}