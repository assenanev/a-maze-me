package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.utils.SoundUtils;

public class LoadScreen implements Screen {
	
	private AMazeMeGame aMazeMeGame;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private static Texture splashTexture;
	
	private int virtualWigth = 480;
	private int virtualHeight = 800;
	private float virtualAspect = (float) virtualWigth/virtualHeight;
	private int width, height;
	private float aspect, splashAspect;
	private int splashWidth, splashHeight;
	private float activeTime;
	private float minActiveTime = 3f;
	AssetManager manager;
	
	
	
	
	

	public LoadScreen(AMazeMeGame game) {
		super();
		this.aMazeMeGame = game;
		
		aspect = (float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		if(aspect < virtualAspect){
			width = virtualWigth;
	        height = (int) (virtualWigth / aspect);
		} else {
			width = (int) (virtualHeight * aspect);
	        height = virtualHeight;
		}
		
		manager = game.assetManager;
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.BACK){
					Gdx.app.exit();
				}
				return true;
			}
		});
		
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		param.magFilter = TextureFilter.Linear;

		manager.load("data/bgmain.png", Texture.class, param);
		manager.load("data/atlases/amazeme.atlas", TextureAtlas.class);
		manager.load("data/sounds/click.ogg", Sound.class);
		manager.load("data/sounds/gruh.ogg", Sound.class);
		manager.load("data/sounds/kvik.ogg", Sound.class);
		manager.load("data/sounds/pirates.ogg", Music.class);

		batch = new SpriteBatch();
		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		
		splashTexture = new Texture(Gdx.files.internal("data/splash.png"));
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		splashAspect = (float) splashTexture.getWidth()/splashTexture.getHeight();
		splashWidth = (int) (width * 0.9f);
		splashHeight = (int) (splashWidth / splashAspect);
		activeTime = 0;
		
		
	}

	@Override
	public void render(float delta) {
		activeTime += delta;
		
		if(manager.update() && activeTime > minActiveTime){
			aMazeMeGame.loadLevels();
			SoundUtils.playMusic(manager);
			aMazeMeGame.setScreen(new MainScreen(aMazeMeGame));
		}
		
		GL20 gl = Gdx.gl;
		gl.glClearColor(0f, 0.89f, 0.99f, 1f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(splashTexture, width/2 - splashWidth/2, height/2 - splashHeight/2 + height/30, splashWidth, splashHeight);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();		
	}

}
