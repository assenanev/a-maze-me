
package com.xquadro.android.amazeme.world;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
	public enum State {MEMORIZE, ZOOMME, ZOOMING, RUNNING, ENDING, GAME_FAIL, GAME_OVER};
	
	public interface WorldListener {
		public void touchWall ();

		public void win ();
		
		public void fail ();
		
		public void destroyStone ();		
	}

	public static final float WORLD_WIDTH = 100;
	public static final float WORLD_HEIGHT = 100;
	public static final float PIG_RADIUS = 0.8f;
	
	public static final float MEMO_TIME = 3f;
	
	private float accumulator = 0;
	private final static float TICK = 1 / 60f;
	
	public State state;
	public float stateTime;

	//models
	
	BodyEditorLoader loader;
	
	public static final Vector2 gravity = new Vector2(0, -10);
	
	public World box2dWorld;
	
	Vector3 touchPoint;
	Vector2 pigPos = new Vector2();
	Vector2 prevPigPos = new Vector2();
	Vector2 sunPos, pearsPos;
	
	private boolean goDown = true;
	private float touchWallTimer = 0;
	
	Body mazeModel = null;
	Body pigModel = null;
	Body stones = null;
	
	private Body hitBody = null;
	private Fixture hitFixture = null;
	
	public final WorldListener listener;
	Level level;

	
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
			if (fixture.testPoint(touchPoint.x, touchPoint.y)) {
				hitBody = fixture.getBody();
				hitFixture = fixture;
				return false;
			} else
				return true;
		}
	};
	

	public GameWorld (WorldListener listener, Level l) {
		this.level = l;
		this.listener = listener;
		loadLevel(level);

		state = State.MEMORIZE;
	}

	private void loadLevel (Level l) {
		box2dWorld = new World(gravity, true);
		loader = new BodyEditorLoader(Gdx.files.internal(l.mazeFile));
		createGround();
		createMaze();
		createStones();
		createPig();
		pearsPos = getShapePosition("pears");
		sunPos = getShapePosition("sun");
	}
	
	private void createGround() {
		BodyDef bd = new BodyDef();
		bd.position.set(0, -0.1f);
		bd.type = BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(WORLD_WIDTH, 0.1f);

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0f;
		fd.shape = shape;

		box2dWorld.createBody(bd).createFixture(fd);

		shape.dispose();
	}

	private void createMaze() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.2f;

		mazeModel = box2dWorld.createBody(bd);

		loader.attachFixture(mazeModel, "maze", fd, WORLD_WIDTH);
		//mazeModelOrigin = loader.getOrigin("maze", WORLD_WIDTH).cpy();
		
		mazeModel.setTransform(0f, 0f, 0f);
		mazeModel.setLinearVelocity(0, 0);
		mazeModel.setAngularVelocity(0);
	}
	
	private void createStones() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.2f;

		stones = box2dWorld.createBody(bd);

		loader.attachFixture(stones, "stones", fd, WORLD_WIDTH);
		//mazeModelOrigin = loader.getOrigin("stones", WORLD_WIDTH).cpy();
		
		stones.setTransform(0f, 0f, 0f);
		stones.setLinearVelocity(0, 0);
		stones.setAngularVelocity(0);
	}

	Vector2 getShapePosition(String fixtureName){
		BodyDef tmpBd = new BodyDef();
		tmpBd.type = BodyType.StaticBody;
		FixtureDef tmpFd = new FixtureDef();
		Body tmp = box2dWorld.createBody(tmpBd);
		loader.attachFixture(tmp, fixtureName, tmpFd, WORLD_WIDTH);	
    	CircleShape sh = (CircleShape) tmp.getFixtureList().get(0).getShape();
    	Vector2 pos = sh.getPosition();
    	box2dWorld.destroyBody(tmp);
		return pos;
	}
	private void createPig() {		
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.type = BodyType.DynamicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(PIG_RADIUS);

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.6f;
		fd.shape = shape;

		pigModel = box2dWorld.createBody(ballBodyDef);
		pigModel.createFixture(fd);
		shape.dispose();
		pigPos = getShapePosition("pig");
		prevPigPos.set(pigPos);
		pigModel.setTransform(pigPos, 0);

		pigModel.setAwake(true);
		pigModel.setActive(true);
		
	}
	
	public void touch (Vector3 touchPoint) {
		this.touchPoint = touchPoint;
		hitBody = null;
		hitFixture = null;
		box2dWorld.QueryAABB(callback, touchPoint.x - 0.01f, touchPoint.y - 0.01f, touchPoint.x + 0.01f, touchPoint.y + 0.01f);

		if (hitBody == stones ) {
			hitBody.destroyFixture(hitFixture);
			listener.destroyStone();
			pigModel.setAwake(true);
		}
	}

	public void updatePhysics (float deltaTime) {
	      float dt = MathUtils.clamp(deltaTime, 0, 0.030f);
	      accumulator += dt;
	 
	      while(accumulator > TICK) {
	         accumulator -= TICK;
	         box2dWorld.step(TICK, 10, 10);
	      }
	   }
	
	public void update (float deltaTime) {		
		switch (state) {
		case MEMORIZE:
			updateMemorize(deltaTime);
			break;
		case ZOOMME:
			break;
		case ZOOMING:
			break;
		case RUNNING:
			updateRunning(deltaTime);
			break;
		case ENDING:
			updateEnding(deltaTime);
			break;
		case GAME_FAIL:
			break;
		case GAME_OVER:
			break;
		default:
			break;
		}
		
	}
	
	private void updateEnding(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime > 0.5f){
			state = State.GAME_OVER;
		}		
	}

	public void updateRunning (float deltaTime) {
		touchWallTimer += deltaTime;
		updatePhysics(deltaTime);
		pigPos = pigModel.getPosition();
		checkGameOver();
		checkPigDirection(deltaTime);
	}
	
	private void checkPigDirection(float deltaTime) {
		touchWallTimer += deltaTime;
		if (pigPos.y > prevPigPos.y && goDown) {
			goDown = false;
			if (touchWallTimer > 0.5f){
				listener.touchWall();
				touchWallTimer = 0;
			}
		}
		
		if (pigPos.y < prevPigPos.y && !goDown) {
			goDown = true;
		}
		prevPigPos.set(pigPos);
	}

	private void updateMemorize(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime > MEMO_TIME) {
			state = State.ZOOMME;
			stateTime = 0;
		}
	}


	private void checkGameOver () {

		if(pigPos.y < 2f) {
			state = State.ENDING;
			stateTime = 0;
			listener.win();
		}
		
		Array<Contact> contacts = box2dWorld.getContactList();
		int len = contacts.size;
		int b2ContactCount = 0;
		for(int i = 0; i < len; i++){
			Contact c = contacts.get(i);
			Body a = c.getFixtureA().getBody();
			Body b = c.getFixtureB().getBody();
			if (a == pigModel && b == stones)
				b2ContactCount++;
			if (b == pigModel && a == stones)
				b2ContactCount++;
		}
		
		if(pigPos.y > 2f && b2ContactCount < 1 && !pigModel.isAwake()){
			state = State.GAME_FAIL;
			listener.fail();
		}
	}
	
	public void dispose () {
		box2dWorld.dispose();
	}
}
