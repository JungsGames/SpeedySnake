package com.jung.framework.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jung.framework.intf.Audio;
import com.jung.framework.intf.BannerListener;
import com.jung.framework.intf.FileIO;
import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Input;
import com.jung.framework.intf.InterstitialListener;
import com.jung.framework.intf.Screen;
import com.jung.game.main.Assets;

@SuppressLint("Registered") 
public class AndroidGame extends Activity implements Game, InterstitialListener, BannerListener{
	RelativeLayout layout;
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	DisplayMetrics displayMetrics;
	AdView adView;
	InterstitialAd interstitialAd;
	private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    boolean isVisible = false;
    
    @SuppressLint("HandlerLeak") 
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                	adView.setVisibility(View.VISIBLE);
                    adView.resume();
                    isVisible = true;
                    break;
                }
                case HIDE_ADS:
                {
                	adView.setVisibility(View.GONE);
                    adView.pause();
                    isVisible = false;
                    break;
                }
            }
        }
    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 1280 : 800;
		int frameBufferHeight = isLandscape ? 800 : 1280;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);
		
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		float scaleX = (float) frameBufferWidth
				/ displayMetrics.widthPixels;
		float scaleY = (float) frameBufferHeight
				/ displayMetrics.heightPixels;
		
		layout = new RelativeLayout(this);
		renderView = new AndroidFastRenderView(this, frameBuffer);
		
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-4022879704675666/3437857231");
		
		interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-4022879704675666/4914590437");
        
        
        AdRequest adRequest = new AdRequest.Builder().build();
        
        interstitialAd.loadAd(adRequest);
		adView.loadAd(adRequest);
		
		interstitialAd.setAdListener(new AdListener() {
			
            @Override
            public void onAdClosed() {
            	AdRequest adRequest = new AdRequest.Builder().build();
            	interstitialAd.loadAd(adRequest);
            	getCurrentScreen();
            }
        });
		
		screen = getStartScreen();
		layout.addView(renderView);
		
		RelativeLayout.LayoutParams adParams = 
	            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
	            		RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    
	    layout.addView(adView, adParams);
		setContentView(layout);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		screen.resume();
		renderView.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		renderView.pause();
		screen.pause();
		if (Assets.bgm.isPlaying())
			Assets.bgm.pause();
		
		if (isFinishing()) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}

	@Override
	public Screen getStartScreen() {
		return null;
	}
	
	@Override
    public void onBackPressed() {
        if(screen.toString().equals("MainMenuScreen")){
        	//THIS BLOCK WILL BE CALLED IF ABOVE COND IS TRUE, AND WOULD ENABLE BACK BUTTON
        	//finish(); finishes one Activity
        	//android.os.Process.killProcess(android.os.Process.myPid()); //finishes all Activities from the stack
        	super.onBackPressed();
            

        }else{
        	//THIS BLOCK WILL NOT DO ANYTHING AND WOULD DISABLE BACK BUTTON
        }
    }
	
	public void displayInterstitial(){
        if(interstitialAd.isLoaded())
            interstitialAd.show();
    }

	@Override
	public void showInterstitial() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				displayInterstitial();
			}
		});
	}

	@Override
	public void showBanner(boolean show) {
		if (show && !isVisible)
			handler.sendEmptyMessage(SHOW_ADS);
		if (!show)
			handler.sendEmptyMessage(HIDE_ADS);
	}
}
