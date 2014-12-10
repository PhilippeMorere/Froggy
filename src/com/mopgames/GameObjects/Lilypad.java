package com.mopgames.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class Lilypad extends Scrollable {
	private Random r;

	private Rectangle hitBox;

	public static final int VERTICAL_GAP = 45;
	public static final int SKULL_WIDTH = 40;
	public static final int SKULL_HEIGHT = 11;
	public static final int LILYPAD_GAP = 49;
	private static final int minHeight = 8;
	private static final int minWidth = 40;

	private float groundY;
	private boolean isScored = false;

	// When Pipe's constructor is invoked, invoke the super (Scrollable)
	// constructor
	public Lilypad(float x, float y, float scrollSpeed, float groundY) {
		super(x, y - minHeight, minWidth, minHeight, scrollSpeed);
		// Initialize a Random object for Random number generation
		r = new Random();
		hitBox = new Rectangle();
		this.groundY = groundY;
		// reset(x);
	}

	@Override
	public void update(float delta) {
		// Call the update method in the superclass (Scrollable)
		super.update(delta);
		hitBox.set(position.x, position.y + height / 3, width, height / 3);
	}

	@Override
	public void reset(float newX) {
		newX += (0.5 + r.nextDouble() * 0.7) * LILYPAD_GAP;
		// Call the reset method in the superclass (Scrollable)
		super.reset(newX);
		// Change the height to a random number
		double seed = r.nextDouble() * 0.3;
		height = (int) (minHeight * (1 + seed));
		width = (int) (minWidth * (1 + seed));
		position.y = groundY - height;
		isScored = false;
	}

	public void onRestart(float x, float scrollSpeed) {
		velocity.x = scrollSpeed;
		reset(x);
	}

	public boolean collidesLand(Frog frog) {
		// if (position.x < frog.getX() + frog.getWidth()) {
		if (position.x < frog.getBoundingCircle().x
		// Bad corner collision (following)
				&& !(position.y < frog.getBoundingCircle().y && position.x + 5 > frog
						.getBoundingCircle().x))
			return (Intersector.overlaps(frog.getBoundingCircle(), hitBox));
		// }
		return false;
	}

	public boolean isScored() {
		return isScored;
	}

	public void setScored(boolean b) {
		isScored = b;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}
}
