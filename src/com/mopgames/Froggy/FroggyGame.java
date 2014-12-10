package com.mopgames.Froggy;

import com.badlogic.gdx.Game;
import com.mopgames.Screens.SplashScreen;
import com.mopgames.Helpers.ActionResolver;
import com.mopgames.Helpers.AssetLoader;

public class FroggyGame extends Game {

	public static ActionResolver actionResolver;

	public FroggyGame(ActionResolver actionResolver) {
		super();
		FroggyGame.actionResolver = actionResolver;
	}

	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}