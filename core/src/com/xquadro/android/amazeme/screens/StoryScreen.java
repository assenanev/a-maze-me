package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xquadro.android.amazeme.AMazeMeGame;
import com.xquadro.android.amazeme.utils.SoundUtils;

public class StoryScreen extends AbstractScreen {
	public static TextureAtlas atlas;
	
	Image story;
	
	TextureRegionDrawable story1, story2, story3;
	
	int imageIndex = 0;
	float stateTime = 0;
	boolean autoUpdateImage = true;

	public StoryScreen(AMazeMeGame aMazeMeGame) {
		super(aMazeMeGame, "data/bgmain.png");

		atlas = game.assetManager.get("data/atlases/amazeme.atlas", TextureAtlas.class);
		
		story1 = new TextureRegionDrawable(atlas.findRegion("story1"));
		story2 = new TextureRegionDrawable(atlas.findRegion("story2"));
		story3 = new TextureRegionDrawable(atlas.findRegion("story3"));
		
		story = new Image(story1);
		story.setPosition(width/2-460/2, height/2 - 714/2 + 20);
		story.setSize(460, 714);
		stage.addActor(story);
		
        ImageButton btnPrev = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnprev")));
        btnPrev.setPosition(20, 20);
        btnPrev.setSize(60, 60);
        btnPrev.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				imageIndex--;
				updateImage();	
				autoUpdateImage = false;
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});
        stage.addActor(btnPrev);
        
        ImageButton btnMenu = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnmenu")));
        btnMenu.setPosition(width/2 - 60/2, 20);
        btnMenu.setSize(60, 60);
        btnMenu.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new MainScreen(game));
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});
        stage.addActor(btnMenu);
        
        ImageButton btnNext = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnnext")));
        btnNext.setPosition(width - 80, 20);
        btnNext.setSize(60, 60);
        btnNext.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				imageIndex++;
				updateImage();	
				autoUpdateImage = false;
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		stage.addActor(btnNext);
	}

	protected void updateImage() {
		if (imageIndex > 2) {
			imageIndex = 0;
		}
		if (imageIndex < 0) {
			imageIndex = 2;
		}
		
		switch (imageIndex) {
		case 2:
			story.setDrawable(story3);
			break;
		case 1:
			story.setDrawable(story2);
			break;
		case 0:
		default:			
			story.setDrawable(story1);
			break;
		}
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0.89f, 0.99f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        
        update(delta);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		stage.draw();
	}

	private void update(float delta) {
		stateTime += delta;
		if(stateTime > 2 && autoUpdateImage){
			imageIndex++;
			updateImage();
			stateTime = 0;
			
			if (imageIndex >= 2){
				autoUpdateImage = false;
			}
		}
		
	}

	@Override
	void goToPrevScreen() {
		game.setScreen(new MainScreen(game));		
	}


}
