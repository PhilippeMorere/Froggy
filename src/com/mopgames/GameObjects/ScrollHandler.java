package com.mopgames.GameObjects;

public class ScrollHandler {
	private Frog frog;
	private Lilypad lilypad1, lilypad2, lilypad3;
	private Background frontBackground, backBackground;
	private MenuItem logo, help;
	public static final int SCROLL_SPEED = -120; // -135
	private float frogStartX, frogStartY;

	public ScrollHandler(float width, float yPos) {
		frogStartX = 71; // (int) (width / 2f - 8.5f);
		frogStartY = yPos - 124; // yPos - 71;
		frontBackground = new Background(0, yPos - 20, 136, 43, SCROLL_SPEED);
		backBackground = new Background(frontBackground.getTailX(), yPos - 20,
				136, 43, SCROLL_SPEED);
		frog = new Frog(frogStartX, frogStartY, 17, 12, SCROLL_SPEED);

		lilypad1 = new Lilypad(210, yPos, SCROLL_SPEED, yPos);
		lilypad2 = new Lilypad(lilypad1.getTailX() + Lilypad.LILYPAD_GAP, yPos,
				SCROLL_SPEED, yPos);
		lilypad3 = new Lilypad(lilypad2.getTailX() + Lilypad.LILYPAD_GAP, yPos,
				SCROLL_SPEED, yPos);
		logo = new MenuItem(136 / 2 - 42, (int) yPos - 128, 91 /* / 1.2f */
		, 42 /* / 1.2f */, SCROLL_SPEED);
		help = new MenuItem(136 / 2, (int) yPos, 91, 42, SCROLL_SPEED);
		onRestart();
	}

	public void updateMenu(float delta) {
		frontBackground.update(delta);
		backBackground.update(delta);

		// Same with background
		if (frontBackground.isScrolledLeft()) {
			frontBackground.reset(backBackground.getTailX());

		} else if (backBackground.isScrolledLeft()) {
			backBackground.reset(frontBackground.getTailX());

		}
	}

	public void update(float delta) {
		// Update our objects
		frontBackground.update(delta);
		backBackground.update(delta);
		frog.update(delta);
		lilypad1.update(delta);
		lilypad2.update(delta);
		lilypad3.update(delta);
		logo.update(delta);
		help.update(delta);

		// Check if any of the lilypads are scrolled left,
		// and reset accordingly
		if (lilypad1.isScrolledLeft()) {
			lilypad1.reset(lilypad3.getTailX());
		} else if (lilypad2.isScrolledLeft()) {
			lilypad2.reset(lilypad1.getTailX());
		} else if (lilypad3.isScrolledLeft()) {
			lilypad3.reset(lilypad2.getTailX());
		}

		// Same with background
		if (frontBackground.isScrolledLeft()) {
			frontBackground.reset(backBackground.getTailX());

		} else if (backBackground.isScrolledLeft()) {
			backBackground.reset(frontBackground.getTailX());

		}
	}

	public void stop() {
		frontBackground.stop();
		backBackground.stop();
		lilypad1.stop();
		lilypad2.stop();
		lilypad3.stop();
		frog.stop();
	}

	public boolean collidesLand(Frog frog) {
		// if (!lilypad1.isScored()
		// && lilypad1.getX() + (lilypad1.getWidth() / 2) < frog.getX()
		// + frog.getWidth()) {
		// addScore(1);
		// lilypad1.setScored(true);
		// // AssetLoader.coin.play();
		// } else if (!lilypad2.isScored()
		// && lilypad2.getX() + (lilypad2.getWidth() / 2) < frog.getX()
		// + frog.getWidth()) {
		// addScore(1);
		// lilypad2.setScored(true);
		// // AssetLoader.coin.play();
		//
		// } else if (!lilypad3.isScored()
		// && lilypad3.getX() + (lilypad3.getWidth() / 2) < frog.getX()
		// + frog.getWidth()) {
		// addScore(1);
		// lilypad3.setScored(true);
		// // AssetLoader.coin.play();
		//
		// }

		return (lilypad1.collidesLand(frog) || lilypad2.collidesLand(frog) || lilypad3
				.collidesLand(frog));
	}

	public float getFloor(Frog frog) {
		if (lilypad1.collidesLand(frog))
			return lilypad1.getHitBox().getY();
		else if (lilypad2.collidesLand(frog))
			return lilypad2.getHitBox().getY();
		else if (lilypad3.collidesLand(frog))
			return lilypad3.getHitBox().getY();
		return lilypad1.getHitBox().getY();
	}

	public Background getFrontBackground() {
		return frontBackground;
	}

	public Background getBackBackground() {
		return backBackground;
	}

	public Lilypad getLilypad1() {
		return lilypad1;
	}

	public Lilypad getLilypad2() {
		return lilypad2;
	}

	public Lilypad getLilypad3() {
		return lilypad3;
	}

	public Frog getFrog() {
		return frog;
	}

	public MenuItem getLogo() {
		return logo;
	}
	
	public MenuItem getHelp() {
		return help;
	}


	public void onRestart() {
		frontBackground.onRestart(0, SCROLL_SPEED);
		backBackground.onRestart(frontBackground.getTailX(), SCROLL_SPEED);
		lilypad1.onRestart(170, SCROLL_SPEED);
		lilypad2.onRestart(lilypad1.getTailX() + Lilypad.LILYPAD_GAP,
				SCROLL_SPEED);
		lilypad3.onRestart(lilypad2.getTailX() + Lilypad.LILYPAD_GAP,
				SCROLL_SPEED);
		frog.onRestart(frogStartX, frogStartY);
		logo.onRestart(136 / 2 - 42);
		help.onRestart(136 / 2);
	}
}