package com.mopgames.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SimpleButton {

	private float x, y, width, height;

	private TextureRegion buttonUp;
	private TextureRegion buttonDown;
	private TextureRegion icon;

	private Rectangle bounds;

	private boolean isPressed = false;

	public SimpleButton(float x, float y, float width, float height,
			TextureRegion buttonUp, TextureRegion buttonDown, TextureRegion icon) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.buttonUp = buttonUp;
		this.buttonDown = buttonDown;
		this.icon = icon;

		bounds = new Rectangle(x, y, width, height);

	}

	public boolean isClicked(int screenX, int screenY) {
		return bounds.contains(screenX, screenY);
	}

	public void draw(SpriteBatch batcher) {
		draw(batcher, 0, 0);
	}

	public void draw(SpriteBatch batcher, float xOffset, float yOffset) {
		if (isPressed) {
			batcher.draw(buttonDown, x + xOffset, y + yOffset, width, height);
		} else {
			batcher.draw(buttonUp, x + xOffset, y + yOffset, width, height);
		}
		batcher.draw(icon, width / 2 + x + xOffset - icon.getRegionWidth() / 2,
				height / 2 + y + yOffset - icon.getRegionHeight() / 2 - 1,
				icon.getRegionWidth(), icon.getRegionHeight());
	}

	public boolean isTouchDown(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY)) {
			isPressed = true;
			return true;
		}

		return false;
	}

	public boolean isTouchUp(int screenX, int screenY) {
		// It only counts as a touchUp if the button is in a pressed state.
		if (bounds.contains(screenX, screenY) && isPressed) {
			isPressed = false;
			return true;
		}

		// Whenever a finger is released, we will cancel any presses.
		isPressed = false;
		return false;
	}

}
