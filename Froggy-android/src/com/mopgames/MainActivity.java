package com.mopgames;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.plus.PlusShare;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import com.mopgames.Froggy.FroggyGame;
import com.mopgames.Helpers.ActionResolver;
import com.mopgames.Helpers.AssetLoader;

public class MainActivity extends AndroidApplication implements
		GameHelperListener, ActionResolver {
	protected AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-9606491772050851/9529550921";
	private GameHelper gameHelper;
	private String onFailLoginMessage = null;

	public MainActivity() {
		gameHelper = new GameHelper(this);
		gameHelper.enableDebugLog(true, "GPGS");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the libgdx View
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		View gameView = initializeForView(new FroggyGame(this), cfg);

		// Create and setup the AdMob view
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);

		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("D667D1A2F3C7801FF0E205F635D9DE30") // Nexus 5
				// .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
				.build();

		// Set an Ad Listener to know when the user clicks
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdOpened() {
				if (getSignedInGPGS())
					unlockAchievementGPGS("CgkImu2VmcgNEAIQEw");
			}
		});

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
		adView.setVisibility(View.GONE);

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		// adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(adView, adParams);

		// Hook it all up
		setContentView(layout);
		gameHelper.setup(this);
	}

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_ADS: {
				adView.setVisibility(View.VISIBLE);
				break;
			}
			case HIDE_ADS: {
				adView.setVisibility(View.GONE);
				break;
			}
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		gameHelper.getGamesClient().submitScore("CgkImu2VmcgNEAIQAQ", score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		gameHelper.getGamesClient().unlockAchievement(achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (getSignedInGPGS())
			startActivityForResult(gameHelper.getGamesClient()
					.getLeaderboardIntent("CgkImu2VmcgNEAIQAQ"), 100);
		else {
			loginGPGS();
			onFailLoginMessage = "Could not open the leaderborad, please connect to Google Play Games.";
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (getSignedInGPGS())
			startActivityForResult(gameHelper.getGamesClient()
					.getAchievementsIntent(), 101);
		else {
			loginGPGS();
			onFailLoginMessage = "Could not open the achievements, please connect to Google Play Games.";
		}
	}

	@Override
	public void onSignInFailed() {
		AssetLoader.setGooglePlayLoginPreference(false);
		if (onFailLoginMessage != null) {
			final Context context = this;
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, onFailLoginMessage,
							Toast.LENGTH_SHORT).show();
				}
			});
			onFailLoginMessage = null;
		}
	}

	@Override
	public void onSignInSucceeded() {
		AssetLoader.setGooglePlayLoginPreference(true);
	}

	@Override
	public void showAd(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void rateGame() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// Try Google play
		intent.setData(Uri.parse("market://details?id=com.mopgames"));
		if (!MyStartActivity(intent)) {
			// Market (Google play) app seems not installed, let's try to open a
			// webbrowser
			intent.setData(Uri
					.parse("https://play.google.com/store/apps/details?id=com.mopgames"));
			if (!MyStartActivity(intent)) {
				// Well if this also fails, we have run out of options, inform
				// the user.
				final Context context = this;
				this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								context,
								"Could not open Android market, please install the market app.",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}

	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void share(String text, String appLink) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");

		PackageManager pm = getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		for (final ResolveInfo app : activityList) {
			String packageName = app.activityInfo.packageName;
			Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);

			if (packageName.contains("twitter")) {
				targetedShareIntent.putExtra(Intent.EXTRA_TEXT, text + " "
						+ appLink);
				targetedShareIntent.setPackage(packageName);
			} else if (packageName.contains("facebook")
					&& !packageName.contains("orca")) {
				targetedShareIntent.putExtra(Intent.EXTRA_TEXT, appLink + " "
						+ text);
				targetedShareIntent.setPackage(packageName);
			} else if (packageName.contains("google.android.apps.plus")) {
				targetedShareIntent = new PlusShare.Builder(this)
						.setType("text/plain").setText(text + " " + appLink)
						.getIntent();
			} else
				continue;
			targetedShareIntent.setType("text/plain");
			targetedShareIntents.add(targetedShareIntent);
		}

		Intent chooserIntent = Intent.createChooser(
				targetedShareIntents.remove(0), "Share score with..");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				targetedShareIntents.toArray(new Parcelable[] {}));
		startActivity(chooserIntent);
	}

}
