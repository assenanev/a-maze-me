package com.xquadro.android.amazeme.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xquadro.android.amazeme.AMazeMeGame;

public class FarewellScreen extends AbstractScreen {
	
	public static TextureAtlas atlas;
	Image bg;
	
	Image sun;
	
	Table t;
	
	private float stateTime = 0;

	public FarewellScreen(AMazeMeGame aMazeMeGame) {
		super(aMazeMeGame, "data/bgmain.png");
		atlas = game.assetManager.get("data/atlases/amazeme.atlas", TextureAtlas.class);
		
		bg = new Image(new TextureRegionDrawable(atlas.findRegion("farewellbg")));
		bg.setSize(width, height);
		stage.addActor(bg);
		
		sun = new Image(new TextureRegionDrawable(atlas.findRegion("farewellsun")));
		sun.setSize(650, 516);
		sun.setPosition(width/2 - 650/2, height/2-516/2 - 40);
		stage.addActor(sun);
		
		t = new Table();
		t.setBackground(new TextureRegionDrawable(atlas.findRegion("farewell")));
		t.setSize(160, 303);
		t.setPosition(width/2 - 160/2 - 30, height/2 - 303/2 - 135);
		
		t.addAction(Actions.parallel(
						Actions.moveTo(width/2 - 160/2 - 10 + 40, height/2-303/2 - 135 + 110, 4.5f),
						Actions.sizeTo(80, 150, 4.5f),
						Actions.fadeOut(4.5f)));
		stage.addActor(t);
	}

	@Override
	void goToPrevScreen() {
		game.setScreen(new MainScreen(game));		
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		 
		Gdx.gl.glClearColor(0f, 0.89f, 0.99f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		stage.draw();
	}

	private void update(float delta) {
		stateTime += delta;
		if (stateTime > 6.0F){
			goToPrevScreen();
		}
		
	}

}
