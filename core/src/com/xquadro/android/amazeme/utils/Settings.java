package com.xquadro.android.amazeme.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	public static final Preferences prefs = Gdx.app.getPreferences("preferences");
	public static boolean soundEnabled = true;
	public static boolean musicEnabled = true;
	public static boolean vibrateEnabled = true;
	public static int levelCompleted = 0;

	public static void load() {
		if (!prefs.contains("soundEnabled")
				|| !prefs.contains("musicEnabled")
				|| !prefs.contains("vibrateEnabled")
				|| !prefs.contains("levelCompleted")
				) {
			save();
		}

		soundEnabled = prefs.getBoolean("soundEnabled", true);
		musicEnabled = prefs.getBoolean("musicEnabled", true);
		vibrateEnabled = prefs.getBoolean("vibrateEnabled", true);
		levelCompleted = prefs.getInteger("levelCompleted", 0);
		
	}

	public static void save() {
		prefs.putBoolean("soundEnabled", soundEnabled);
		prefs.putBoolean("musicEnabled", musicEnabled);
		prefs.putBoolean("vibrateEnabled", vibrateEnabled);
		prefs.putInteger("levelCompleted", levelCompleted);
		prefs.flush();
	}
}
