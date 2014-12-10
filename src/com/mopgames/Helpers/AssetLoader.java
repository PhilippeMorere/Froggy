package com.mopgames.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture texture, logoTexture, numbersTexture;

	public static TextureRegion logo, froggyLogo, bg, frog, frogJump, arrow,
			frogJumpUp, lilypad, buttonUp, buttonDown, playButton, rateButton,
			gameOver, achievementButton, highScore, scoreboard, star, noStar,
			help, scoreButton, tweetButton, numbers[];

	public static Animation frogAnimation;

	public static Preferences prefs;

	public static Sound splash, jump, dead;

	public static void load() {
		numbersTexture = new Texture(Gdx.files.internal("data/numbers.png"));
		numbersTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		numbers = new TextureRegion[10];
		for (int i = 0; i < 10; i++) {
			numbers[i] = new TextureRegion(numbersTexture, i * 6, 0, 6, 8);
			numbers[i].flip(false, true);
		}

		logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
		logoTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		logo = new TextureRegion(logoTexture, 0, 9, 110, 54);

		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		buttonUp = new TextureRegion(texture, 219, 38, 37, 27);
		buttonDown = new TextureRegion(texture, 219, 65, 37, 27);
		buttonUp.flip(false, true);
		buttonDown.flip(false, true);

		playButton = new TextureRegion(texture, 231, 92, 9, 13);
		playButton.flip(false, true);

		tweetButton = new TextureRegion(texture, 144, 46, 30, 12);
		tweetButton.flip(false, true);

		scoreButton = new TextureRegion(texture, 237, 106, 17, 13);
		scoreButton.flip(false, true);

		rateButton = new TextureRegion(texture, 219, 106, 17, 7);
		rateButton.flip(false, true);

		achievementButton = new TextureRegion(texture, 219, 92, 11, 13);
		achievementButton.flip(false, true);

		gameOver = new TextureRegion(texture, 0, 86, 58, 21);
		gameOver.flip(false, true);

		highScore = new TextureRegion(texture, 0, 107, 58, 21);
		highScore.flip(false, true);

		scoreboard = new TextureRegion(texture, 98, 63, 114, 61);
		scoreboard.flip(false, true);

		star = new TextureRegion(texture, 69, 116, 10, 10);
		noStar = new TextureRegion(texture, 82, 116, 10, 10);

		star.flip(false, true);
		noStar.flip(false, true);

		froggyLogo = new TextureRegion(texture, 0, 43, 91, 42);
		froggyLogo.flip(false, true);

		bg = new TextureRegion(texture, 0, 0, 136, 43);
		bg.flip(false, true);

		help = new TextureRegion(texture, 219, 0, 37, 38);
		help.flip(false, true);

		arrow = new TextureRegion(texture, 93, 48, 11, 11);
		arrow.flip(false, true);

		frog = new TextureRegion(texture, 136, 0, 18, 12);
		frog.flip(false, true);

		frogJump = new TextureRegion(texture, 154, 0, 22, 13);
		frogJump.flip(false, true);

		frogJumpUp = new TextureRegion(texture, 176, 0, 23, 11);
		frogJumpUp.flip(false, true);

		TextureRegion[] frogs = { frogJump, frogJumpUp };
		frogAnimation = new Animation(0.2f, frogs);
		frogAnimation.setPlayMode(Animation.NORMAL);

		lilypad = new TextureRegion(texture, 136, 14, 42, 17);
		lilypad.flip(false, true);

		splash = Gdx.audio.newSound(Gdx.files.internal("data/splash.wav"));
		jump = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));

		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("Froggy");

		// Provide default high score of 0
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}

		// Provide default games played of 0
		if (!prefs.contains("numberGamesPlayed")) {
			prefs.putInteger("numberGamesPlayed", 0);
		}

		// Provide default login google play preference
		if (!prefs.contains("shouldLoginToGooglePlay")) {
			prefs.putBoolean("shouldLoginToGooglePlay", true);
		}
		prefs.flush();
	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
		logoTexture.dispose();

		// Dispose sounds
		splash.dispose();
		jump.dispose();

		// Dispose font
		numbersTexture.dispose();
	}

	// Receives an integer and maps it to the String highScore in prefs
	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}

	// Retrieves the current high score
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}

	// Retrieves the current high score
	public static int getNumberGamesPlayed() {
		return prefs.getInteger("numberGamesPlayed");
	}

	public static void addGamePlayed() {
		int number = prefs.getInteger("numberGamesPlayed");
		prefs.putInteger("numberGamesPlayed", number + 1);
		prefs.flush();
	}

	public static boolean getGooglePlayLoginPreference() {
		return prefs.getBoolean("shouldLoginToGooglePlay");
	}

	public static void setGooglePlayLoginPreference(boolean value) {
		prefs.putBoolean("shouldLoginToGooglePlay", value);
		prefs.flush();
	}
}