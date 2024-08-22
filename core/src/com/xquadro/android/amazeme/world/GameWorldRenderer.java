package com.xquadro.android.amazeme.world;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.utils.Array;
import com.xquadro.android.amazeme.OrthographicCameraAccessor;
import com.xquadro.android.amazeme.world.GameWorld.State;

public class GameWorldRenderer {
	static final float ZOOM = 0.06f;
	
	static final float PEAR_WIDTH = 4f; //(half)
	static final float PEAR_HEIGHT = 1.29f;
	static final float SUN_WIDTH = 2f;

	float aspect;
	
	GameWorld world;
	public OrthographicCamera camera;
	SpriteBatch batch;
	
	AssetManager assetManager;
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tileRenderer;
	TextureRegion background;
	TextureAtlas atlas;
	AtlasRegion bg;
	
	private Sprite pigSprite;	
	private Sprite stoneSprite;
	private Sprite pearSprite;
	private Sprite sunSprite;

	//Box2DDebugRenderer debugRenderer;
	
	TweenManager tm;
	
	public GameWorldRenderer (SpriteBatch batch, GameWorld world, AssetManager assetManager) {
		this.world = world;
		//this.atlas = atlas;
		this.assetManager = assetManager;
		Level lvl = world.level;
		this.atlas = assetManager.get("data/atlases/amazeme.atlas",
				TextureAtlas.class);
		
		switch (lvl.maze) {
		case NIGHT:
			bg = atlas.findRegion("bgnight");
			break;
		case DAY:
		default:
			bg = atlas.findRegion("bg");
			break;
		}
		
		
		aspect = (float) Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
		camera = new OrthographicCamera(GameWorld.WORLD_WIDTH,  GameWorld.WORLD_WIDTH * aspect);
		camera.position.set(GameWorld.WORLD_WIDTH / 2, GameWorld.WORLD_WIDTH / 2, 0);
		
		this.batch = batch;

		tiledMap = new TmxMapLoader().load(lvl.tileFile);
		tileRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 64f);
		//debugRenderer = new Box2DDebugRenderer();
		
		tm = new TweenManager();
		Tween.registerAccessor(OrthographicCamera.class, new OrthographicCameraAccessor());
		
		createSprites(lvl);
	}
	
	private void createSprites(Level lvl) {
		switch (lvl.pig) {
		case NINJA:
			pigSprite = new Sprite(atlas.findRegion("pigninja"));
			break;
		case PIRATE:
		default:
			pigSprite = new Sprite(atlas.findRegion("pig"));
			break;
		}
		//pigSprite = new Sprite(atlas.findRegion("pig"));
		pigSprite.setSize(GameWorld.PIG_RADIUS * 2.2f, GameWorld.PIG_RADIUS*2.2f);
		pigSprite.setOrigin(GameWorld.PIG_RADIUS * 1.1f, GameWorld.PIG_RADIUS*1.1f);

		stoneSprite = new Sprite(atlas.findRegion("cstones"));
		stoneSprite.setSize(
				GameWorld.PIG_RADIUS*2.3f, GameWorld.PIG_RADIUS*2.3f);
		stoneSprite.setOrigin(GameWorld.PIG_RADIUS-5, GameWorld.PIG_RADIUS-5);
		
		sunSprite = new Sprite(atlas.findRegion("sun"));
		sunSprite.setSize(SUN_WIDTH * 2f, SUN_WIDTH * 2f);
		sunSprite.setOrigin(SUN_WIDTH, SUN_WIDTH);
		sunSprite.setPosition(world.sunPos.x - sunSprite.getWidth() / 2,
				world.sunPos.y - sunSprite.getHeight() / 2);

		pearSprite = new Sprite(atlas.findRegion("pears"));
		pearSprite.setSize(PEAR_WIDTH * 2, PEAR_HEIGHT * 2);
		pearSprite.setOrigin(PEAR_WIDTH, PEAR_HEIGHT);
		pearSprite.setPosition(world.pearsPos.x - pearSprite.getWidth() / 2,
				world.pearsPos.y - pearSprite.getHeight() / 2);

	}

	public void render () {
		
		if(world.state == State.GAME_FAIL || world.state == State.GAME_OVER) {
			return;
		}
		
		if (world.state == State.ZOOMME) {
			Tween.to(camera, OrthographicCameraAccessor.POS_XY_ZOOM, 3f)
					.target(world.pigPos.x, world.pigPos.y, ZOOM)
					.setCallback(new TweenCallback() {
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							setWorldState(GameWorld.State.RUNNING);
							removeSolution();
						}
					}).start(tm);
			world.state = State.ZOOMING;
		}
		if (world.state == State.ZOOMING) {
			tm.update(Gdx.graphics.getDeltaTime());
		}
		
		if (world.state == State.RUNNING) {
			camera.position.set(world.pigPos.x, world.pigPos.y, 0);
		}
			
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderBackground();
		tileRenderer.setView(camera);
		tileRenderer.render();
		batch.setProjectionMatrix(camera.combined);
		renderObjects();
		//debugRenderer.render(world.box2dWorld, camera.combined);
	}
	
	private void removeSolution(){
		MapLayer solution = (MapLayer) tiledMap.getLayers().get("Solution");
		tiledMap.getLayers().remove(solution);
	}
	private void setWorldState(GameWorld.State s){
		world.state = s;
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		batch.draw(bg, 0, (GameWorld.WORLD_WIDTH - GameWorld.WORLD_WIDTH * aspect)/2
				, GameWorld.WORLD_WIDTH,
				GameWorld.WORLD_WIDTH * aspect);
		batch.end();
		batch.enableBlending();
	}

	public void renderObjects () {

		batch.enableBlending();
		batch.begin();
		sunSprite.draw(batch);
		pearSprite.draw(batch);
		renderStones();
		renderPig();		
		batch.end();
	}

	private void renderPig () {
		if (world.state == State.GAME_FAIL || world.state == State.GAME_OVER){
			
		} else {
			pigSprite.setPosition(world.pigPos.x - pigSprite.getWidth() / 2,
					world.pigPos.y - pigSprite.getHeight() / 2);
	
			pigSprite.setRotation(world.pigModel.getAngle()
					* MathUtils.radiansToDegrees); 
	
			pigSprite.draw(batch);
		}
	}

	private void renderStones () {
		if(world.state != State.RUNNING) {
			return;
		}

		Array<Fixture> fixtures = world.stones.getFixtureList();
		int len = fixtures.size;
		
		for (int i = 0; i < len; i++) {
			Fixture fixture = fixtures.get(i);
			
		    if (fixture.getType() == Type.Circle) {
		    	CircleShape shape = (CircleShape)fixture.getShape();
		    	float radius = shape.getRadius();
		    	Vector2 pos = shape.getPosition();
		    	stoneSprite.setPosition(pos.x - radius, pos.y - radius);
		    	stoneSprite.setSize(radius*2f, radius*2f);
		    	stoneSprite.draw(batch);
		    } 		      
		}

	}
}
