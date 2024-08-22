package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.utils.Settings;
import com.xquadro.android.amazeme.utils.SoundUtils;

public class MainScreen extends AbstractScreen {

	public static TextureAtlas atlas;
	
	ImageButton btnSound;
	ImageButton btnMusic;
	ImageButton btnVibrate;
	ImageButton btnSettings;
	boolean showSettings = false;

	public MainScreen(AMazeMeGame aMazeMeGame) {
		super(aMazeMeGame, "data/bgmain.png");
		
		atlas = game.assetManager.get("data/atlases/amazeme.atlas", TextureAtlas.class);

        ImageButton btnPlay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnplaybig")));
        btnPlay.setPosition(width/2 - 175/2, height/2);
        btnPlay.setSize(175, 175);
        btnPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.setScreen(new SelectLevelScreen(game));				
			}
		        
		});
        stage.addActor(btnPlay);
        
        Image settingsBG = new Image(new TextureRegionDrawable(atlas.findRegion("btnbgyellow")));
        settingsBG.setPosition(90, 20);
        settingsBG.setSize(60, 60);
        stage.addActor(settingsBG);
        
        Image storyBG = new Image(new TextureRegionDrawable(atlas.findRegion("btnbgyellow")));
        storyBG.setPosition(15, 22);
        storyBG.setSize(80, 80);
        stage.addActor(storyBG);
        
        btnSound = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnsoundoff")),
        		null, new TextureRegionDrawable(atlas.findRegion("btnsoundon")));
        btnSound.setPosition(100, 30);
        btnSound.setSize(40, 40);
        btnSound.setChecked(Settings.soundEnabled);
        btnSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Settings.soundEnabled = btnSound.isChecked();		
				Settings.save();	
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnSound);  
        
        btnMusic = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnmusicoff")),
        		null, new TextureRegionDrawable(atlas.findRegion("btnmusicon")));
        btnMusic.setPosition(100, 30);
        btnMusic.setSize(40, 40);
        btnMusic.setChecked(Settings.musicEnabled);
        btnMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Settings.musicEnabled = btnMusic.isChecked();		
				Settings.save();
				SoundUtils.toggleMusic(game.assetManager);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnMusic);

        btnVibrate = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnvibrateoff")),
        		null, new TextureRegionDrawable(atlas.findRegion("btnvibrateon")));
        btnVibrate.setPosition(100, 30);
        btnVibrate.setSize(40, 40);
        btnVibrate.setChecked(Settings.vibrateEnabled);
        btnVibrate.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Settings.vibrateEnabled = btnVibrate.isChecked();		
				Settings.save();		
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnVibrate); 
        
		btnSettings = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnsettings")));
		btnSettings.setPosition(100, 30);
		btnSettings.setSize(40, 40);
		btnSettings.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showSettings = !showSettings;
				if(showSettings){
					btnSound.addAction(Actions.moveTo(100, 180, 0.3f));
					btnMusic.addAction(Actions.moveTo(100, 130, 0.3f));
					btnVibrate.addAction(Actions.moveTo(100, 80, 0.3f));
				} else {
					btnSound.addAction(Actions.moveTo(100, 30, 0.3f));	
					btnMusic.addAction(Actions.moveTo(100, 30, 0.3f));
					btnVibrate.addAction(Actions.moveTo(100, 30, 0.3f));
				}
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnSettings);
        
		ImageButton btnStory = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnstory")));
		btnStory.setPosition(25, 32);
		btnStory.setSize(60, 60);
		btnStory.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new StoryScreen(game));	
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});
		stage.addActor(btnStory);

	}

	@Override
	void goToPrevScreen() {
		Gdx.app.exit();		
	}
}
