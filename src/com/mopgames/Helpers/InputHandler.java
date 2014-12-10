package com.mopgames.Helpers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.mopgames.Froggy.FroggyGame;
import com.mopgames.GameObjects.Frog;
import com.mopgames.GameWorld.GameWorld;
import com.mopgames.ui.SimpleButton;

public class InputHandler implements InputProcessor {
	private Frog myFrog;
	private GameWorld myWorld;

	private List<SimpleButton> menuButtons;
	private List<SimpleButton> scoreButtons;

	private SimpleButton playButton;
	private SimpleButton scoreButton;
	public Rectangle tweetButton;
	private SimpleButton rateButton;
	private SimpleButton achievementButton;

	private float scaleFactorX;
	private float scaleFactorY;

	public InputHandler(GameWorld myWorld, float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = myWorld;
		myFrog = myWorld.getFrog();

		int midPointY = myWorld.getMidPointY();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		menuButtons = new ArrayList<SimpleButton>();
		scoreButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(
				136 / 2 - AssetLoader.buttonUp.getRegionWidth(),
				midPointY + 27, 37, 27, AssetLoader.buttonUp,
				AssetLoader.buttonDown, AssetLoader.playButton);
		scoreButton = new SimpleButton(136 / 2, midPointY + 27, 37, 27,
				AssetLoader.buttonUp, AssetLoader.buttonDown,
				AssetLoader.scoreButton);
		rateButton = new SimpleButton(
				136 / 2 - AssetLoader.buttonUp.getRegionWidth(),
				midPointY + 80, 37, 27, AssetLoader.buttonUp,
				AssetLoader.buttonDown, AssetLoader.rateButton);
		tweetButton = new Rectangle(
				136 / 2 - AssetLoader.buttonUp.getRegionWidth() + 36,
				midPointY + 10, 37, 27);
		achievementButton = new SimpleButton(136 / 2, midPointY + 80, 37, 27,
				AssetLoader.buttonUp, AssetLoader.buttonDown,
				AssetLoader.achievementButton);
		scoreButtons.add(playButton);
		menuButtons.add(rateButton);
		scoreButtons.add(scoreButton);
		menuButtons.add(achievementButton);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		if (myWorld.isMenu()) {
			boolean buttonRateDown = false;
			boolean buttonAchievementDown = false;
			buttonRateDown = rateButton.isTouchDown(screenX, screenY);
			buttonAchievementDown = achievementButton.isTouchDown(screenX,
					screenY);
			if (!buttonAchievementDown && !buttonRateDown)
				myWorld.start();
		}

		if (!myWorld.isStarting())
			myFrog.onClick();

		if (myWorld.isGameOver() || myWorld.isHighScore()) {
			playButton.isTouchDown(screenX, screenY);
			scoreButton.isTouchDown(screenX, screenY);
			if (myWorld.isHighScore() && tweetButton.contains(screenX, screenY))
				myWorld.tweetScore();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (myWorld.isGameOver() || myWorld.isHighScore()) {
			if (playButton.isTouchUp(screenX, screenY))
				myWorld.restart();
			if (scoreButton.isTouchUp(screenX, screenY))
				FroggyGame.actionResolver.getLeaderboardGPGS();

		}
		if (myWorld.isMenu()) {
			if (rateButton.isTouchUp(screenX, screenY))
				FroggyGame.actionResolver.rateGame();
			if (achievementButton.isTouchUp(screenX, screenY))
				FroggyGame.actionResolver.getAchievementsGPGS();
		}
		myFrog.onClickRelease();
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {

		// Can now use Space Bar to play the game
		if (keycode == Keys.SPACE) {

			if (myWorld.isMenu()) {
				myWorld.start();
			}

			if (!myWorld.isStarting())
				myFrog.onClick();

			if (myWorld.isGameOver() || myWorld.isHighScore()) {
				myWorld.restart();
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.SPACE) {
			if (myWorld.isMenu()) {
				myWorld.start();
			}
			myFrog.onClickRelease();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

	public List<SimpleButton> getMenuButtons() {
		return menuButtons;
	}

	public List<SimpleButton> getScoreButtons() {
		return scoreButtons;
	}
}