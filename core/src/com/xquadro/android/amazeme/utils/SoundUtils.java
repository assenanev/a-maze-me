package com.xquadro.android.amazeme.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundUtils {

	public static void playSound(AssetManager manager, String soundName){
		if(Settings.soundEnabled) {
			Sound sound = manager.get("data/sounds/"+soundName, Sound.class);
			sound.play(0.6f);
		}
	}
	
	public static void vibrate(){
		if(Settings.vibrateEnabled) {
			Gdx.input.vibrate(10);		
		}
	}
	
	public static void playMusic(AssetManager manager){
		if(Settings.musicEnabled) {			
			Music music = manager.get("data/sounds/pirates.ogg", Music.class);
			music.setVolume(1f);
			music.setLooping(true); 
			music.play();
		}
		
	}

	public static void toggleMusic(AssetManager manager){
		Music music = manager.get("data/sounds/pirates.ogg", Music.class);
		
		if(Settings.musicEnabled && !music.isPlaying() ){
			playMusic(manager);
		}
		
		if(!Settings.musicEnabled && music.isPlaying() ){
			music.stop();
		}	
	}
}
