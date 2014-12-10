package com.mopgames.client;

import com.mopgames.Froggy.FroggyGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		cfg.width = 1080 / 3;
		cfg.height = 1920 / 3;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new FroggyGame(new ActionResolverDesktop());
	}
}