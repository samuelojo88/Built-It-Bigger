package com.udacity.gradle.builditbigger.free;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.async.BackendAsyncTask;

public class MainActivityFree extends AppCompatActivity implements BackendAsyncTask.ApiResponse {

    private final String TAG = "MainActivityFree_TAG";

    private ProgressBar mProgressBar;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_free);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544/1033173712");


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("TAG", "Interstitial Ad Lodaed!!");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mProgressBar.setVisibility(View.VISIBLE);
                new BackendAsyncTask(MainActivityFree.this).execute();

                // Re-load add in case button is clicked more than once.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }

    public void tellJoke(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "Interstitial hasn't fully loaded yet...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseReceived(String response) {
        mProgressBar.setVisibility(View.GONE);
        if(response != null) {
            Intent intent = new Intent(getApplicationContext(), javalibrary.four.gradle.udacity.com.androidjokelib.MainActivity.class);
            intent.putExtra(javalibrary.four.gradle.udacity.com.androidjokelib.MainActivity.JOKE_PARAM, response);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.generic_api_error_message), Toast.LENGTH_LONG).show();
        }
    }
}
