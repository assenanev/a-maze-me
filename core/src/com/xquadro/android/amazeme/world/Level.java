package com.xquadro.android.amazeme.world;


public class Level {
	
	public enum Pig {PIRATE, NINJA};
	public enum Maze {DAY, NIGHT};
	
	public Pig pig = Pig.PIRATE;
	public Maze maze = Maze.DAY;
	public String mazeFile = "data/level1/level1.json";
	public String tileFile = "data/level1/level1.tmx";
}
