package com.mopgames.GameObjects;

public class MenuItem extends Scrollable {
	public MenuItem(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, 0);
	}

	public void onRestart(float x) {
		position.x = x;
		velocity.x = 0;
	}

	public void onRun(float scrollSpeed) {
		velocity.x = scrollSpeed;
	}
}
