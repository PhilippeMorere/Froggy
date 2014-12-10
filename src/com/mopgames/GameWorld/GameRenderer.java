package com.mopgames.GameWorld;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.mopgames.GameObjects.Background;
import com.mopgames.GameObjects.Frog;
import com.mopgames.GameObjects.Lilypad;
import com.mopgames.GameObjects.MenuItem;
import com.mopgames.GameObjects.ScrollHandler;
import com.mopgames.TweenAccessors.Value;
import com.mopgames.TweenAccessors.ValueAccessor;
import com.mopgames.Helpers.AssetLoader;
import com.mopgames.Helpers.InputHandler;
import com.mopgames.ui.SimpleButton;

public class GameRenderer {

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private int midPointY;
	private float runTimeSinceDead = 0;

	// Game Objects
	private Frog frog;
	private ScrollHandler scroller;
	private Lilypad lilypad1, lilypad2, lilypad3;
	private Background frontBg, backBg;
	private MenuItem logo, help;

	// Game Assets
	private TextureRegion bg, frogMid, lilypad, froggyLogo, gameOver,
			highScore, scoreboard, star, noStar, helpTex, arrow, numbers[];
	private Animation frogAnimation;

	// Tween stuff
	private TweenManager managerAlpha, managerPos, managerScore;
	private Value valuePosition = new Value(), valueAlpha = new Value(),
			valueScore = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;
	private List<SimpleButton> scoreButtons;
	private Color transitionColor;

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
		myWorld = world;

		this.midPointY = midPointY;
		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getMenuButtons();
		this.scoreButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getScoreButtons();

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		initGameObjects();
		initAssets();

		transitionColor = new Color();
		prepareAlphaTransition(255, 255, 255, .5f);
		preparePositionTransition(0, midPointY, .5f);
		prepareScoreTransition(0, 1.5f);
	}

	private void initGameObjects() {
		frog = myWorld.getFrog();
		scroller = myWorld.getScroller();
		lilypad1 = scroller.getLilypad1();
		lilypad2 = scroller.getLilypad2();
		lilypad3 = scroller.getLilypad3();
		frontBg = scroller.getFrontBackground();
		backBg = scroller.getBackBackground();
		logo = scroller.getLogo();
		help = scroller.getHelp();
	}

	private void initAssets() {
		helpTex = AssetLoader.help;
		arrow = AssetLoader.arrow;
		bg = AssetLoader.bg;
		frogAnimation = AssetLoader.frogAnimation;
		frogMid = AssetLoader.frog;
		lilypad = AssetLoader.lilypad;
		froggyLogo = AssetLoader.froggyLogo;
		gameOver = AssetLoader.gameOver;
		highScore = AssetLoader.highScore;
		scoreboard = AssetLoader.scoreboard;
		star = AssetLoader.star;
		noStar = AssetLoader.noStar;
		numbers = AssetLoader.numbers;
	}

	// private void drawHitBoxes() {
	// shapeRenderer.begin(ShapeType.Filled);
	// shapeRenderer.setColor(Color.RED);
	// shapeRenderer.circle(frog.getBoundingCircle().x,
	// frog.getBoundingCircle().y, frog.getBoundingCircle().radius);
	// shapeRenderer.rect(lilypad1.getHitBox().x, lilypad1.getHitBox().y,
	// lilypad1.getHitBox().width, lilypad1.getHitBox().height);
	// shapeRenderer.rect(lilypad2.getHitBox().x, lilypad2.getHitBox().y,
	// lilypad2.getHitBox().width, lilypad2.getHitBox().height);
	// shapeRenderer.rect(lilypad3.getHitBox().x, lilypad3.getHitBox().y,
	// lilypad3.getHitBox().width, lilypad3.getHitBox().height);
	// shapeRenderer.end();
	// }

	private void drawLilypads() {
		batcher.draw(lilypad, lilypad1.getX(), lilypad1.getY(),
				lilypad1.getWidth(), lilypad1.getHeight());

		batcher.draw(lilypad, lilypad2.getX(), lilypad2.getY(),
				lilypad2.getWidth(), lilypad2.getHeight());

		batcher.draw(lilypad, lilypad3.getX(), lilypad3.getY(),
				lilypad3.getWidth(), lilypad3.getHeight());
	}

	private void drawFrogCentered(float runTime) {
		batcher.draw(frogMid, frog.getX(), frog.getY(), frog.getWidth() / 2.0f,
				frog.getHeight() / 2.0f, frog.getWidth(), frog.getHeight(), 1,
				1, frog.getRotation());
	}

	private void drawFrog(float runTime) {

		if (!frog.isJumping()) {
			batcher.draw(frogMid, frog.getX(), frog.getY(),
					frog.getWidth() / 2.0f, frog.getHeight() / 2.0f,
					frog.getWidth(), frog.getHeight(), 1, 1, frog.getRotation());
		} else if (frog.shouldntFlap()) {
			batcher.draw(frogMid, frog.getX(), frog.getY(),
					frog.getWidth() / 2.0f, frog.getHeight() / 2.0f,
					frog.getWidth(), frog.getHeight(), 1, 1, frog.getRotation());

		} else {
			batcher.draw(frogAnimation.getKeyFrame(runTime), frog.getX(),
					frog.getY(), frog.getWidth() / 2.0f,
					frog.getHeight() / 2.0f, frog.getWidth(), frog.getHeight(),
					1, 1, frog.getRotation());
		}

	}

	private void drawFroggyLogo() {
		batcher.draw(froggyLogo, logo.getX(), logo.getY(), logo.getWidth(),
				logo.getHeight());
	}

	private void drawHelp() {
		batcher.draw(helpTex, help.getX() - 37 / 2, midPointY + 38, 37, 38);
		batcher.draw(arrow, help.getX() - 25, midPointY + 30, 11, 11);
		batcher.draw(lilypad, help.getX() - 60, midPointY + 58, 42, 17);
		batcher.draw(frogMid, help.getX() - 60 + 42 / 2 - frog.getWidth() / 2,
				midPointY + 51, frog.getWidth(), frog.getHeight());

		batcher.draw(frogAnimation.getKeyFrame(250),
				help.getX() - frog.getWidth() / 2.0f, midPointY + 10,
				frog.getWidth() / 2.0f, frog.getHeight() / 2.0f,
				frog.getWidth(), frog.getHeight(), 1, 1, -10);
		batcher.draw(lilypad, help.getX() + 19, midPointY + 58, 42, 17);
	}

	private void drawMenuUI() {
		drawFroggyLogo();
		drawHelp();
		drawButtonsMenuScreen();
	}

	private void drawScoreboard(float delta) {
		if (valuePosition.getValue() < midPointY)
			managerPos.update(delta);

		batcher.draw(scoreboard, 136 / 2 - 114 / 2, midPointY
				+ (midPointY - valuePosition.getValue()) - 30, 114, 61);

		batcher.draw(noStar, 21,
				midPointY + (midPointY - valuePosition.getValue()) - 3, 10, 10);
		batcher.draw(noStar, 33,
				midPointY + (midPointY - valuePosition.getValue()) - 3, 10, 10);
		batcher.draw(noStar, 45,
				midPointY + (midPointY - valuePosition.getValue()) - 3, 10, 10);
		batcher.draw(noStar, 57,
				midPointY + (midPointY - valuePosition.getValue()) - 3, 10, 10);
		batcher.draw(noStar, 69,
				midPointY + (midPointY - valuePosition.getValue()) - 3, 10, 10);

		// Left medal
		if (myWorld.getScore() > 2 && runTimeSinceDead > 1)
			batcher.draw(star, 21, midPointY - 3, 10, 10);

		// 2nd
		if (myWorld.getScore() > 15 && runTimeSinceDead > 1.5)
			batcher.draw(star, 33, midPointY - 3, 10, 10);

		// 3rd
		if (myWorld.getScore() > 30 && runTimeSinceDead > 2)
			batcher.draw(star, 45, midPointY - 3, 10, 10);

		// 4th
		if (myWorld.getScore() > 60 && runTimeSinceDead > 2.5)
			batcher.draw(star, 57, midPointY - 3, 10, 10);

		// Last medal
		if (myWorld.getScore() > 120 && runTimeSinceDead > 3)
			batcher.draw(star, 69, midPointY - 3, 10, 10);

		if (runTimeSinceDead > .7)
			managerScore.update(delta);
		printNumber((int) valueScore.getValue(), 102, midPointY
				+ (midPointY - valuePosition.getValue()) - 10, 1f);

		// Highscore
		if (myWorld.getScore() > myWorld.getOldHighScore())
			printNumber(
					Math.max(myWorld.getOldHighScore(),
							(int) valueScore.getValue()), 102, midPointY
							+ (midPointY - valuePosition.getValue()) + 11, 1f);
		else
			printNumber(AssetLoader.getHighScore(), 102, midPointY
					+ (midPointY - valuePosition.getValue()) + 11, 1f);
	}

	private void printNumber(int number, float x, float y, float sizeFactor) {
		// Compute the number of digits
		String numberStr = String.valueOf(number);
		int count = numberStr.length();
		int xOffset = (int) (-sizeFactor * (count * 6 + (count - 1)) / 2);
		for (int i = 0; i < count; i++) {
			int digit = (int) numberStr.charAt(i) - 48;
			batcher.draw(numbers[digit], x + sizeFactor * 7 * i + xOffset, y,
					sizeFactor * 6, sizeFactor * 8);
		}
	}

	private void drawButtonsMenuScreen() {
		for (SimpleButton button : menuButtons) {
			button.draw(batcher, help.getX() - 136 / 2, 0);
		}
	}

	private void drawButtonsScoreScreen() {
		for (SimpleButton button : scoreButtons) {
			if (valuePosition.getValue() < midPointY)
				button.draw(batcher, 0, midPointY - valuePosition.getValue());
			else
				button.draw(batcher);
		}
		if (myWorld.isHighScore()) {
			Rectangle tweetButton = ((InputHandler) Gdx.input
					.getInputProcessor()).tweetButton;
			if (valuePosition.getValue() < midPointY)
				batcher.draw(AssetLoader.tweetButton, tweetButton.x,
						tweetButton.y + midPointY - valuePosition.getValue());
			else
				batcher.draw(AssetLoader.tweetButton, tweetButton.x,
						tweetButton.y);
		}
	}

	private void drawGameOver() {
		batcher.draw(gameOver, 136 / 2 - 58 / 2, valuePosition.getValue() - 60,
				58, 21);
	}

	private void drawScore() {
		printNumber(myWorld.getScore(), 63, midPointY - 82, 3);
	}

	private void drawHighScore() {
		batcher.draw(highScore, 136 / 2 - 58 / 2,
				valuePosition.getValue() - 60, 58, 21);
	}

	public void initRunTimeSinceDead() {
		runTimeSinceDead = 0;
	}

	public void render(float delta, float runTime) {
		runTimeSinceDead += delta;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		drawBackground();

		batcher.enableBlending();

		drawLilypads();
		// drawHitBoxes();

		if (myWorld.isRunning()) {
			drawFrog(runTime);
			// drawFroggyLogo();
			drawScore();
			batcher.end();
			drawCoveringWater();
		} else if (myWorld.isStarting()) {
			drawFrog(runTime);
			drawMenuUI();
			batcher.end();
		} else if (myWorld.isMenu()) {
			drawFrogCentered(runTime);
			drawMenuUI();
			batcher.end();
		} else if (myWorld.isGameOver()) {
			drawScoreboard(delta);
			drawButtonsScoreScreen();
			drawFrog(runTime);
			drawGameOver();
			batcher.end();
			// drawCoveringWater();
		} else if (myWorld.isHighScore()) {
			drawScoreboard(delta);
			drawButtonsScoreScreen();
			drawFrog(runTime);
			drawHighScore();
			batcher.end();
			// drawCoveringWater();
		}
		drawTransition(delta);
	}

	private void drawCoveringWater() {
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);
		// Draw Water
		shapeRenderer.setColor(39 / 255.0f, 121 / 255.0f, 142 / 255.0f, 0.5f);
		shapeRenderer.rect(0, midPointY + 66, 136, 83);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
	}

	private void drawBackground() {
		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(174 / 255.0f, 213 / 255.0f, 229 / 255.0f, 1);
		shapeRenderer.rect(0, 0, 136, midPointY + 66);

		// Draw Water
		shapeRenderer.setColor(39 / 255.0f, 121 / 255.0f, 142 / 255.0f, 1);
		shapeRenderer.rect(0, midPointY + 23, 136, 43);

		shapeRenderer.setColor(39 / 255.0f, 121 / 255.0f, 142 / 255.0f, 1.0f);
		shapeRenderer.rect(0, midPointY + 66, 136, 83);

		shapeRenderer.end();

		batcher.begin();
		batcher.disableBlending();

		// batcher.draw(, 0, midPointY, 136, 43);

		// Draw the background
		batcher.draw(bg, frontBg.getX(), frontBg.getY() - frontBg.getHeight(),
				frontBg.getWidth(), frontBg.getHeight());
		batcher.draw(bg, backBg.getX(), backBg.getY() - backBg.getHeight(),
				backBg.getWidth(), backBg.getHeight());
	}

	public void prepareScoreTransition(int score, float duration) {
		valueScore.setValue(0);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		managerScore = new TweenManager();
		Tween.to(valueScore, -1, duration).target(score)
				.ease(TweenEquations.easeInOutCubic).start(managerScore);
	}

	public void preparePositionTransition(int from, int to, float duration) {
		valuePosition.setValue(from);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		managerPos = new TweenManager();
		Tween.to(valuePosition, -1, duration).target(to)
				.ease(TweenEquations.easeInCubic).start(managerPos);
	}

	public void prepareAlphaTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		valueAlpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		managerAlpha = new TweenManager();
		Tween.to(valueAlpha, -1, duration).target(0)
				.ease(TweenEquations.easeOutQuad).start(managerAlpha);
	}

	private void drawTransition(float delta) {
		if (valueAlpha.getValue() > 0) {
			managerAlpha.update(delta);
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g,
					transitionColor.b, valueAlpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}
	}

}
