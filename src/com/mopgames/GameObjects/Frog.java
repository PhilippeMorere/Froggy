package com.mopgames.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Circle;
import com.mopgames.Helpers.AssetLoader;

public class Frog extends Scrollable {

	private Vector2 acceleration;

	private float rotation;

	private boolean isAlive;
	private boolean isJumping;
	private boolean isHoldingJump;
	private boolean isSinking;
	private float holdingJumpTime;

	private Circle boundingCircle;
	private float scrollSpeed;

	private Vector2 jumpSpeed;

	public Frog(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		this.scrollSpeed = scrollSpeed;
		jumpSpeed = new Vector2(-scrollSpeed + 15, -150);
		acceleration = new Vector2(0, 460);
		boundingCircle = new Circle();
		onRestart(x, y);
	}

	public void update(float delta) {
		holdingJumpTime += delta;

		// Frog on the floor
		if (isHoldingJump && holdingJumpTime < 0.7f)
			velocity.add(acceleration.cpy().scl(delta * 0.3f));
		else
			velocity.add(acceleration.cpy().scl(delta));

		if (velocity.y > 200) {
			velocity.y = 200;
		}

		// CEILING CHECK
		if (position.y < -13) {
			position.y = -13;
			velocity.y = 0;
		}

		if (!isJumping)
			velocity.y = 0;

		position.add(velocity.cpy().scl(delta));

		// Set the circle's center to be (9, 6) with respect to the frog.
		// Set the circle's radius to be 6.5f;
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f);

		// Rotate counterclockwise
		if (velocity.y < 0) {
			rotation -= 600 * delta;

			if (rotation < -20) {
				rotation = -20;
			}
		}

		// Rotate clockwise
		if (isSinking) {
			rotation += 48 * delta;
			if (rotation > 180)
				rotation = 180;
		} else if (isFalling() || !isAlive) {
			rotation += 480 * delta;
			if (rotation > 90) {
				rotation = 90;
			}

		}

	}

	public void setStarting(Lilypad lilypad) {
		rotation = 0;
		isJumping = false;
		isHoldingJump = false;
		position.x = lilypad.getX() + lilypad.getWidth() / 2 - width / 2;
		position.y = lilypad.getY() - lilypad.getHeight() / 3 - 2 * height / 3;
		velocity.y = 0;
		velocity.x = scrollSpeed;
	}

	public void updateReady(float runTime) {
		// position.y = 2 * (float) Math.sin(7 * runTime) + originalY;
	}

	public boolean isFalling() {
		return velocity.y > 110;
	}

	public boolean shouldntFlap() {
		return velocity.y > 70 || !isAlive;
	}

	public void onClick() {
		if (isAlive) {
			if (!isJumping) {
				isHoldingJump = true;
				holdingJumpTime = 0;
				isJumping = true;
				AssetLoader.jump.play();
				velocity.add(jumpSpeed);
				rotation = 0;
			}
		}
	}

	public void onClickRelease() {
		if (isAlive && isJumping) {
			isHoldingJump = false;
		}
	}

	public void die() {
		isAlive = false;
		velocity.y = 0;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setOnGround(float floorY) {
		rotation = 0;
		position.y = floorY - getHeight();
		isJumping = false;
		velocity.y = 0;
		velocity.x = scrollSpeed;
		acceleration.x = 0;
	}

	public void decelerate() {
		acceleration.y = 0;
	}

	public boolean isOutOfScreen(int width, int height) {
		if (position.x + this.width < 0)
			return true;
		if (position.x > width)
			return true;
		if (position.y + this.height < 0)
			return true;
		if (position.y > height)
			return true;
		return false;
	}

	public void onRestart(float x, float y) {
		rotation = 0;
		position.x = x;
		position.y = y;
		velocity.x = scrollSpeed;
		velocity.y = 0;
		acceleration.x = 0;
		acceleration.y = 460;
		isAlive = true;
		isJumping = true;
		isHoldingJump = false;
		isSinking = false;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getRotation() {
		return rotation;
	}

	public Circle getBoundingCircle() {
		return boundingCircle;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void doBigJump() {
		isJumping = true;
		AssetLoader.jump.play();
		velocity.add(new Vector2(-scrollSpeed + 15, -50).scl(2.5f)); // scl 2.5f
		rotation = 0;
	}

	public void sink() {
		if (!isSinking) {
			AssetLoader.splash.play();
			rotation = 90;
			isSinking = true;
			velocity.x = 0;
			velocity.y = 55; // 55
			acceleration.y = 0;
			acceleration.x = 0;
		}
	}
}