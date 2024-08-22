/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.AnimatedImage;
import com.xquadro.android.amazeme.utils.Settings;
import com.xquadro.android.amazeme.utils.SoundUtils;
import com.xquadro.android.amazeme.world.GameWorld;
import com.xquadro.android.amazeme.world.GameWorldRenderer;
import com.xquadro.android.amazeme.world.Level;
import com.xquadro.android.amazeme.world.GameWorld.WorldListener;

public class GameScreen implements Screen {
	public enum State {LOAD, RUNNING, PAUSED, FAIL, WIN};

	
	final AMazeMeGame game;
	Array<Level> levels;
	Level level;
	int levelCount;
	int levelIndex;
	final int btnPauseSize = 50;
	final int btnSize = 70;

	int virtualWigth = 480;
	int virtualHeight = 800;
	float virtualAspect = (float) virtualWigth / virtualHeight;
	int width, height;
	float aspect;

	State state;
	float stateTime;

	TextureAtlas atlas;
	
	OrthographicCamera guiCam;

	Vector3 touchPoint = new Vector3();

	SpriteBatch batch;

	Stage stage;

	GameWorld gameWorld;
	WorldListener worldListener;
	GameWorldRenderer renderer;
	FPSLogger fps = new FPSLogger();
	
	Table pauseTable;
	ImageButton btnPlay, btnPlayNext;
	ImageButton btnPause;
	AnimatedImage sadPig;
	AnimatedImage happyPig;

	Image winBg;
	Image looseBg;
	
	TextureRegionDrawable play, playNext; 

	public GameScreen(AMazeMeGame aMazeMeGame, int levelIndex) {
		this.game = aMazeMeGame;
		this.levelIndex = levelIndex;
		this.levels = game.levels;
		this.level = levels.get(levelIndex);
		this.levelCount = levels.size;

		atlas = game.assetManager.get("data/atlases/amazeme.atlas",
				TextureAtlas.class);

		state = State.LOAD;
		stateTime = 0;

		aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		if (aspect < virtualAspect) {
			width = virtualWigth;
			height = (int) (virtualWigth / aspect);
		} else {
			width = (int) (virtualHeight * aspect);
			height = virtualHeight;
		}

		guiCam = new OrthographicCamera(width, height);
		guiCam.position.set(width / 2, height / 2, 0);
		guiCam.update();

		batch = new SpriteBatch();
		stage = new Stage(new StretchViewport(width, height));
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage,
			new InputAdapter() {
				@Override
				public boolean keyUp(int keycode) {
					if (keycode == Keys.BACK){
						game.setScreen(new SelectLevelScreen(game));
					}
					return true;
				}
				@Override
				public boolean touchDragged(int x, int y, int pointer) {
					clickWorld(x, y);
					return true;
				}
				@Override
				public boolean touchDown(int x, int y, int pointer, int button) {
					clickWorld(x, y);
					return true;
				}
			}));

		setupStage();

		batch = new SpriteBatch();
		worldListener = new WorldListener() {

			@Override
			public void touchWall() {
				SoundUtils.vibrate();				
			}

			@Override
			public void win() {
				SoundUtils.playSound(game.assetManager, "gruh.ogg");
			}

			@Override
			public void fail() {
				SoundUtils.playSound(game.assetManager, "kvik.ogg");
			}

			@Override
			public void destroyStone() {
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}

		};
		gameWorld = new GameWorld(worldListener, level);

		renderer = new GameWorldRenderer(batch, gameWorld, game.assetManager);
		
		game.getAdsController().prepareInterstitialAd();
	}

	private void setupStage() {
		play = new TextureRegionDrawable(atlas.findRegion("btnplay"));
		playNext = new TextureRegionDrawable(atlas.findRegion("btnplaynext"));
		
		winBg = new Image(new TextureRegionDrawable(atlas.findRegion("win")));
		winBg.setPosition(width/2 - 397/2, height/2 - 700/2 - 20);
		winBg.setSize(397, 700);
		winBg.setVisible(false);
		stage.addActor(winBg);
		
		looseBg = new Image(new TextureRegionDrawable(atlas.findRegion("loose")));
		looseBg.setPosition(width/2 - 339/2, height/2 - 700/2 - 20);
		looseBg.setSize(339, 700);
		looseBg.setVisible(false);
		stage.addActor(looseBg);
		
		sadPig = new AnimatedImage(new Animation(0.1f, atlas.findRegions("spt"), Animation.PlayMode.LOOP_PINGPONG));
		sadPig.setVisible(false);
		sadPig.setPosition(width/2-130/2 + 40, height/2 - 130/2 + 30);
		sadPig.setSize(130, 130);		
		stage.addActor(sadPig);
		
		happyPig = new AnimatedImage(new Animation(0.1f, atlas.findRegions("HPT"), Animation.PlayMode.LOOP_PINGPONG));
		happyPig.setVisible(false);
		happyPig.setPosition(width/2 - 130/2 + 55, height/2-130/2 + 15);
		happyPig.setSize(130, 130);		
		happyPig.addAction(Actions.forever(
				Actions.parallel(
					Actions.sequence(
						Actions.moveTo(width/2 - 130/2 + 55, height/2-130/2 + 50 + 15, 0.8f, Interpolation.circleOut),
						Actions.moveTo(width/2 - 130/2 + 55, height/2-130/2 + 0 + 15, 0.8f, Interpolation.circleIn)),
					Actions.sequence(
						Actions.scaleTo(0.99f, 1.01f, 0.8f, Interpolation.circleOut),
						Actions.scaleTo(1.05f, 0.95f, 0.8f, Interpolation.circleIn))
				)));
		stage.addActor(happyPig);
		
		btnPause = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("btnpause")));
		//btnPause.setPosition(width - btnPauseSize - 20, height - btnPauseSize - 20);
		btnPause.setPosition(width - btnPauseSize - 20, 20);
		btnPause.setSize(btnPauseSize, btnPauseSize);
		btnPause.setVisible(false);
		btnPause.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = State.PAUSED;
				pauseTable.setPosition(width/2-400/2, height/2-100/2);
				pauseTable.setVisible(true);
				btnPause.setVisible(false);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}

		});
		stage.addActor(btnPause);
		
		pauseTable = new Table();
		pauseTable.setWidth(400);
		pauseTable.setHeight(100);
		pauseTable.setPosition(width/2-400/2, height/2-100/2);
		pauseTable.setBackground(new TextureRegionDrawable(atlas.findRegion("bgmenu")));
		pauseTable.setVisible(false);
		ImageButton btnMenu = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnmenu")));
		btnMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new SelectLevelScreen(game));
					}
				});
			}      
		});
		pauseTable.add(btnMenu).width(btnSize).height(btnSize).expandX();
		
		ImageButton btnRestart = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnrestart")));
		btnRestart.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.setScreen(new GameScreen(game, levelIndex));				
			}      
		});
		pauseTable.add(btnRestart).width(btnSize).height(btnSize).expandX();
		
		btnPlay = new ImageButton(play);
		btnPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				if(state==State.WIN){
					if (levelIndex < levelCount){
						levelIndex++;
					}
					game.getAdsController().showInterstitialAd(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new GameScreen(game, levelIndex));
						}
					});
					
				} else {
					state = State.RUNNING;
					pauseTable.setVisible(false);
					btnPause.setVisible(true);
				}

			}      
		});
		pauseTable.add(btnPlay).width(btnSize).height(btnSize).expandX();
		stage.addActor(pauseTable);
	}

	public void update(float deltaTime) {

		stage.act(deltaTime);

		switch (state) {
		case LOAD:
			updateLoad(deltaTime);
			break;
		case RUNNING:
			updateRunning(deltaTime);
			break;
		case PAUSED:
			break;
		case FAIL:
			break;
		case WIN:
			break;
		}
	}

	private void updateLoad(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime > 1) {
			state = State.RUNNING;
			stateTime = 0;
		}
	}
	
	public void clickWorld(int x, int y) {
		touchPoint.set(x, y, 0);
		renderer.camera.unproject(touchPoint);
		gameWorld.touch(touchPoint);
	}

	private void updateRunning(float deltaTime) {

		gameWorld.update(deltaTime);
		
		if(gameWorld.state == GameWorld.State.RUNNING){
			btnPause.setVisible(true);	
		}

		if (gameWorld.state == GameWorld.State.GAME_FAIL) {
			state = State.FAIL;
			stateTime = 0;
			btnPlay.setVisible(false);
			btnPause.setVisible(false);
			pauseTable.setPosition(width/2-400/2, height/2-150);
			pauseTable.setVisible(true);
			sadPig.setVisible(true);
			looseBg.setVisible(true);
		}
		if (gameWorld.state == GameWorld.State.GAME_OVER) {
			state = State.WIN;
			stateTime = 0;

			if(levelIndex == Settings.levelCompleted) {
				Settings.levelCompleted++;
				Settings.save();
			}
			
			if(levelIndex == levelCount - 1){
				//btnPlay.setVisible(false);
				game.setScreen(new FarewellScreen(game));
				return;
			} 

			btnPause.setVisible(false);
			btnPlay.getStyle().imageUp = playNext;
			pauseTable.setVisible(true);			
			pauseTable.setPosition(width/2-400/2, height/2-150);
			happyPig.setVisible(true);
			winBg.setVisible(true);		
		}
	}

	public void draw(float deltaTime) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0f, 0.89f, 0.99f, 1f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		stage.draw();

	}


	@Override
	public void render(float delta) {
		fps.log();
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		//game.myRequestHandler.showAds(false);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {		
		stage.dispose();
		batch.dispose();		
		gameWorld.dispose();	
	}
}
