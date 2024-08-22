package com.xquadro.android.amazeme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.xquadro.android.amazeme.controllers.IAdsController;
import com.xquadro.android.amazeme.screens.LoadScreen;
import com.xquadro.android.amazeme.utils.Settings;
import com.xquadro.android.amazeme.world.Level;
import com.xquadro.android.amazeme.world.Level.Maze;
import com.xquadro.android.amazeme.world.Level.Pig;

public class AMazeMeGame extends Game {
	
	private IAdsController adsController;
	public AssetManager assetManager;
	public Array<Level> levels;

    public AMazeMeGame(IAdsController adsController) {
    	super();
        this.adsController = adsController;
    }	
	
	@Override
	public void create () {
		assetManager = new AssetManager();
		Settings.load();
		levels = new Array<Level>();
		Gdx.input.setCatchBackKey(true);
		setScreen(new LoadScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
		assetManager.clear();
		assetManager.dispose();
	}
	
	public IAdsController getAdsController () {
		return adsController;
	}
	
	public void loadLevels(){
		Level l = new Level();
		l.mazeFile = "data/levels/level1.json";
		l.tileFile = "data/levels/level1.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level2.json";
		l.tileFile = "data/levels/level2.tmx";
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level3.json";
		l.tileFile = "data/levels/level3.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level4.json";
		l.tileFile = "data/levels/level4.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level5.json";
		l.tileFile = "data/levels/level5.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level6.json";
		l.tileFile = "data/levels/level6.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level7.json";
		l.tileFile = "data/levels/level7.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level8.json";
		l.tileFile = "data/levels/level8.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level9.json";
		l.tileFile = "data/levels/level9.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level10.json";
		l.tileFile = "data/levels/level10.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level11.json";
		l.tileFile = "data/levels/level11.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level12.json";
		l.tileFile = "data/levels/level12.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level13.json";
		l.tileFile = "data/levels/level13.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level14.json";
		l.tileFile = "data/levels/level14.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level15.json";
		l.tileFile = "data/levels/level15.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level16.json";
		l.tileFile = "data/levels/level16.tmx";
		levels.add(l);		
		
		l = new Level();
		l.mazeFile = "data/levels/level17.json";
		l.tileFile = "data/levels/level17.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level18.json";
		l.tileFile = "data/levels/level18.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level19.json";
		l.tileFile = "data/levels/level19.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level20.json";
		l.tileFile = "data/levels/level20.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level21.json";
		l.tileFile = "data/levels/level21.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level22.json";
		l.tileFile = "data/levels/level22.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level23.json";
		l.tileFile = "data/levels/level23.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level24.json";
		l.tileFile = "data/levels/level24.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level25.json";
		l.tileFile = "data/levels/level25.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level26.json";
		l.tileFile = "data/levels/level26.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level27.json";
		l.tileFile = "data/levels/level27.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level28.json";
		l.tileFile = "data/levels/level28.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level29.json";
		l.tileFile = "data/levels/level29.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level30.json";
		l.tileFile = "data/levels/level30.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level31.json";
		l.tileFile = "data/levels/level31.tmx";
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level32.json";
		l.tileFile = "data/levels/level32.tmx";
		levels.add(l);
		
		//end of the first pack
		

		
		l = new Level();
		l.mazeFile = "data/levels/level33.json";
		l.tileFile = "data/levels/level33.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level34.json";
		l.tileFile = "data/levels/level34.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level35.json";
		l.tileFile = "data/levels/level35.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level36.json";
		l.tileFile = "data/levels/level36.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level37.json";
		l.tileFile = "data/levels/level37.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level38.json";
		l.tileFile = "data/levels/level38.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level39.json";
		l.tileFile = "data/levels/level39.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);
		
		l = new Level();
		l.mazeFile = "data/levels/level40.json";
		l.tileFile = "data/levels/level40.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);
		
		
		l = new Level();
		l.mazeFile = "data/levels/level41.json";
		l.tileFile = "data/levels/level41.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level42.json";
		l.tileFile = "data/levels/level42.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level43.json";
		l.tileFile = "data/levels/level43.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level44.json";
		l.tileFile = "data/levels/level44.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level45.json";
		l.tileFile = "data/levels/level45.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level46.json";
		l.tileFile = "data/levels/level46.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level47.json";
		l.tileFile = "data/levels/level47.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
		
		l = new Level();
		l.mazeFile = "data/levels/level48.json";
		l.tileFile = "data/levels/level48.tmx";
		l.maze = Maze.NIGHT;
		l.pig = Pig.NINJA; 
		levels.add(l);	
	}
	
}
