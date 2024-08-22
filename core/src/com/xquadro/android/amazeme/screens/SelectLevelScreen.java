package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.utils.Settings;
import com.xquadro.android.amazeme.utils.SoundUtils;
import com.xquadro.android.amazeme.world.Level;

public class SelectLevelScreen extends AbstractScreen {
	public static TextureAtlas atlas;
	
	Array<Level> levels;
	
	int levelIndex;
	
	ImageButton btnSound;
	ImageButton btnMusic;
	ImageButton btnVibrate;
	ImageButton btnSettings;
	boolean showSettings = false;

	public SelectLevelScreen(AMazeMeGame aMazeMeGame) {
		super(aMazeMeGame, "data/bgmain.png");
		
		atlas = game.assetManager.get("data/atlases/amazeme.atlas", TextureAtlas.class);
		
		levels = game.levels;

		Table scrollTable = new Table();
		//scrollTable.debug();
		
		int count = levels.size;
		
		Table t = new Table();

		t.setBackground(new TextureRegionDrawable(atlas.findRegion("bgslidex")));
		t.setSize(400, 400);
//		t.setSize(340, 340);
//		t.setBackground(new NinePatchDrawable(atlas.createPatch("bgslide")));
		int tablePadding = width/2 - 400/2;
		
		if (count <= 16){
			scrollTable.add(t);
		} else {
			scrollTable.add(t).padLeft(tablePadding);
		}
		
		
		int accuPage = 0;
		int accuRow = 0;
		int countX = (count - 1)/16;
		countX = (countX + 1) * 16; 
		//countX = (countX + 1) * 16; 
		
		for (int i = 0; i < countX; i++){
			
			if (accuPage > 15) {
				t = new Table();
				t.setBackground(new TextureRegionDrawable(atlas.findRegion("bgslidex")));
				t.setSize(400, 400);
				//t.setBackground(new NinePatchDrawable(atlas.createPatch("bgslide")));
				if(i / 16 == (count - 1) / 16  ){
					scrollTable.add(t).padLeft(tablePadding).padRight(tablePadding);
				} else {
					scrollTable.add(t).padLeft(tablePadding);
				}
				accuPage = 0;
			}
			
			if (accuRow > 3) {
				t.row();
				accuRow = 0;
			}
			
			if (i < count) {

				AtlasRegion ar;
				boolean levelEnabled;
				if(i < Settings.levelCompleted){
					ar = atlas.findRegion("btnlevelcompleted");
					levelEnabled = true;
				} else if (i == Settings.levelCompleted){
					ar = atlas.findRegion("btnlevelunlocked");
					levelEnabled = true;
				} else {
					ar = atlas.findRegion("btnlevellocked");
					levelEnabled = false;
				}
				ImageButton btnLevel = new ImageButton(new TextureRegionDrawable(ar));
				//btnLevel.setSize(70, 70);
				btnLevel.setName(""+i);
				if (levelEnabled) {
					btnLevel.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							int l = Integer.parseInt(actor.getName());
							game.setScreen(new GameScreen(game, l));
							SoundUtils.playSound(game.assetManager, "click.ogg");
						}      
					});
				}
				t.add(btnLevel).width(92).height(92);
			} else {
				t.add().width(92).height(92);
			}

			accuPage++;
			accuRow++;
			
			
		}
		
		int scrollTo;
		scrollTo = ((Settings.levelCompleted -1 )/ 16) 
				* (400 + tablePadding + tablePadding) + tablePadding;


		ScrollPaneStyle style = new ScrollPaneStyle(null, null, null, null, null);
		ScrollPane pane = new ScrollPane(scrollTable, style);
		pane.setScrollingDisabled(false, true);
		pane.setWidth(width);
		pane.setHeight(400);//pane.pack();
		pane.setPosition(0, 315);
		pane.validate();
		pane.scrollTo(scrollTo, 0, 400, 400);
		stage.addActor(pane);

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
				SoundUtils.playSound(game.assetManager, "click.ogg");
				Settings.save();				
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
				SoundUtils.playSound(game.assetManager, "click.ogg");
				Settings.save();				
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
		game.setScreen(new MainScreen(game));			
	}
}
