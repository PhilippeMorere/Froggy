package com.mopgames.GameWorld;

import com.mopgames.Froggy.FroggyGame;
import com.mopgames.GameObjects.Frog;
import com.mopgames.GameObjects.ScrollHandler;
import com.mopgames.Helpers.AssetLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {

	private Frog frog;
	private ScrollHandler scroller;
	private Rectangle water;
	private int score = 0, oldHighScore = 0;
	private float runTime = 0;
	private int midPointY;
	private int width;
	private boolean startingFrogPhase;

	private GameState currentState;
	private GameRenderer renderer;

	public enum GameState {
		MENU, STARTING, RUNNING, GAMEOVER, HIGHSCORE
	}

	public GameWorld(int width, int midPointY) {
		currentState = GameState.MENU;
		this.midPointY = midPointY;
		this.width = width;
		// The grass should start 66 pixels below the midPointY
		scroller = new ScrollHandler(width, midPointY + 66);
		frog = scroller.getFrog();
		water = new Rectangle(0, midPointY + 66, 137, 11);
		if (AssetLoader.getGooglePlayLoginPreference())
			loginGooglePlay();
	}

	public void update(float delta) {
		runTime += delta;

		switch (currentState) {
		case MENU:
			updateReady(delta);
			break;
		case STARTING:
			updateStarting(delta);
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		default:
			break;
		}

	}

	private void updateReady(float delta) {
		frog.updateReady(runTime);
		scroller.updateMenu(delta);
	}

	public void updateStarting(float delta) {
		if (startingFrogPhase && frog.isOutOfScreen(width, 2 * midPointY)) {
			frog.setStarting(scroller.getLilypad1());
			startingFrogPhase = false;
		}
		// frog.update(delta);
		scroller.update(delta);
		if (!startingFrogPhase && frog.getX() < 2 * width / 3)
			currentState = GameState.RUNNING;
	}

	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}

		scroller.update(delta);

		if (frog.isAlive() && frog.isJumping() && scroller.collidesLand(frog)) {
			frog.setOnGround(scroller.getFloor(frog));
			addScore(1);
		}

		if (Intersector.overlaps(frog.getBoundingCircle(), water)) {
			scroller.stop();
			frog.sink();
			achievementSink();
		}

		if (frog.isOutOfScreen(width, 2 * midPointY + 5)) {
			achievementOutOfScreen();
			scroller.stop();
			AssetLoader.dead.play();
			frog.die();
			frog.decelerate();
			renderer.prepareAlphaTransition(255, 255, 255, .3f);
			renderer.preparePositionTransition(0, midPointY, .5f);
			renderer.prepareScoreTransition(getScore(), 1f);
			renderer.initRunTimeSinceDead();
			currentState = GameState.GAMEOVER;
			AssetLoader.addGamePlayed();
			submitNumberGamesPlayed(AssetLoader.getNumberGamesPlayed());
			if (score > AssetLoader.getHighScore()) {
				oldHighScore = AssetLoader.getHighScore();
				AssetLoader.setHighScore(score);
				currentState = GameState.HIGHSCORE;
				submitScore();
			}
		}
	}

	public Frog getFrog() {
		return frog;
	}

	public int getMidPointY() {
		return midPointY;
	}

	public ScrollHandler getScroller() {
		return scroller;
	}

	public int getScore() {
		return score;
	}

	public int getOldHighScore() {
		return oldHighScore;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public void start() {
		oldHighScore = AssetLoader.getHighScore();
		FroggyGame.actionResolver.showAd(true);
		frog.doBigJump();
		scroller.getLogo().onRun(1.5f * ScrollHandler.SCROLL_SPEED);
		scroller.getHelp().onRun(1.5f * ScrollHandler.SCROLL_SPEED);
		startingFrogPhase = true;
		currentState = GameState.STARTING;
	}

	public void restart() {
		FroggyGame.actionResolver.showAd(false);
		renderer.prepareAlphaTransition(0, 0, 0, 1f);
		score = 0;
		scroller.onRestart();
		currentState = GameState.MENU;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}

	public boolean isStarting() {
		return currentState == GameState.STARTING;
	}

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}

	private void loginGooglePlay() {
		if (FroggyGame.actionResolver.getSignedInGPGS())
			;// FroggyGame.actionResolver.getLeaderboardGPGS();
		else
			FroggyGame.actionResolver.loginGPGS();
	}

	private void achievementSink() {
		if (FroggyGame.actionResolver.getSignedInGPGS())
			FroggyGame.actionResolver
					.unlockAchievementGPGS("CgkImu2VmcgNEAIQEg");
	}

	private void achievementOutOfScreen() {
		if (frog.getX() > width)
			if (FroggyGame.actionResolver.getSignedInGPGS())
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQEQ");
	}

	private void submitScore() {
		if (FroggyGame.actionResolver.getSignedInGPGS()) {
			FroggyGame.actionResolver.submitScoreGPGS(score);
			if (score >= 2)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQCg");
			if (score >= 15)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQCw");
			if (score >= 30)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQDA");
			if (score >= 60)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQDQ");
			if (score >= 120)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQDg");
			if (score >= 250)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQDw");
			if (score >= 500)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQEA");
		}
	}

	private void submitNumberGamesPlayed(int numberGamesPlayed) {
		if (FroggyGame.actionResolver.getSignedInGPGS()) {
			if (numberGamesPlayed >= 5)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQAw");
			if (numberGamesPlayed >= 20)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQBA");
			if (numberGamesPlayed >= 50)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQBQ");
			if (numberGamesPlayed >= 100)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQBg");
			if (numberGamesPlayed >= 200)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQBw");
			if (numberGamesPlayed >= 500)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQCA");
			if (numberGamesPlayed >= 500)
				FroggyGame.actionResolver
						.unlockAchievementGPGS("CgkImu2VmcgNEAIQCQ");
		}
	}

	public void tweetScore() {
		FroggyGame.actionResolver.share(
				"I died again in Froggy... but I scored " + getScore()
						+ "! Try to beat me, download #Froggy",
				"https://play.google.com/store/apps/details?id=com.mopgames");
		// "http://bit.ly/1gdyCr3");
	}
}
