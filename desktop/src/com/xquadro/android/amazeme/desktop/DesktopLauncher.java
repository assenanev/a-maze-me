package com.xquadro.android.amazeme.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.controllers.IAdsController;

public class DesktopLauncher  implements IAdsController {
	private static DesktopLauncher application;

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "a-maze-me";

		cfg.width = 480;
		cfg.height = 800;
		

		
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		settings.filterMin = TextureFilter.Linear;
		settings.filterMag = TextureFilter.Linear;
		settings.paddingX = 2;
		settings.paddingY = 2;
		settings.flattenPaths = true;
		TexturePacker.process(settings, "images", "atlases", "amazeme");
		
		new LwjglApplication(new AMazeMeGame(application), cfg);
	}
	
	@Override
	public void showBannerAd(boolean show) {
        // TODO Auto-generated method stub     
    }

	@Override
	public void prepareInterstitialAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showInterstitialAd(Runnable then) {
		// TODO Auto-generated method stub
		
	}
}
