package com.mopgames.GameObjects;

public class Background extends Scrollable {

	public Background(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y , width * 2, height * 2, scrollSpeed / 3);
	}

	public void onRestart(float x, float scrollSpeed) {
		position.x = x;
		velocity.x = scrollSpeed / 2;
	}
}