package worms.model;

import java.util.Collection;
import java.util.Random;

public class World {
	
	/**
	 * Creates a new world.
	 * 
	 * @param 	width 
	 * 			The width of this new world (in meter)
	 * @param 	height 
	 * 			The height of this new world (in meter)
	 * @param 	passableMap A 
	 * 			Rectangular matrix indicating which parts of the terrain are passable and impassable.
	 *  		This matrix is derived from the transparency of the pixels in the image file of the terrain.
	 *  		passableMap[r][c] is true if the location at row r and column c is passable, and false if that location is impassable.
	 *  		The elements in the first row (row 0) represent the pixels at the top of the terrain (i.e., largest y-coordinates).
	 *  		The elements in the last row (row passableMap.length-1) represent pixels at the bottom of the terrain (smallest y-coordinates).
	 *  		The elements in the first column (column 0) represent the pixels at the left of the terrain (i.e., smallest x-coordinates).
	 *  		The elements in the last column (column passableMap[0].length-1) represent the pixels at the right of the terrain (i.e., largest x-coordinates).     
	 * @param 	random A 
	 * 			Random number generator, seeded with the value obtained from the command line or from GUIOptions,
	 *  		that can be used to randomize aspects of the world in a repeatable way.
	 * @post	| new.getwidth = width
	 * @post	| new.getheight = height
	 * @post	| new.getpassableMap = passableMap
	 * @throws	IllegalArgumentException("The given width or height is not valid.")
	 * 			| !canHaveAsWidthOrHeight(width) || !canHaveAsWidthOrHeight(height)
	 *	
	 */
	public World(double width, double height,
			boolean[][] passableMap, Random random){
		if(!canHaveAsWidthOrHeight(width) || !canHaveAsWidthOrHeight(height))
			throw new IllegalArgumentException("The given width or height is invalid.");
		this.width = width;
		this.height = height;
		this.passableMap = passableMap;
	}
	
	/**
	 * 
	 * @param 	x
	 * 			The width or height to check.
	 * @return	| 0 <= x && x <= UPPER_BOUND
	 */
	public boolean canHaveAsWidthOrHeight(double x){
		return 0 <= x && x <= UPPER_BOUND;
	}
	
	/**
	 * Return the width of this world.
	 */
	public double getWidth(){
		return this.width;
	}
	
	/**
	 * Variable registering the width of this world.
	 */
	private final double width;
	
	
	
	/**
	 * Return the height of this world.
	 */
	public double getHeight(){
		return this.height;
	}
	
	/**
	 * Variable registering the height of this world.
	 */
	private final double height;
	
	
	
	/**
	 * Return the passable map of this world.
	 */
	public boolean[][] getPassableMap(){
		return passableMap;
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is impassable. 
	 * 
	 * @param world The world in which to check impassability 
	 * @param x The x-coordinate of the center of the circle to check  
	 * @param y The y-coordinate of the center of the circle to check
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if the given region is impassable, false otherwise.
	 */
	boolean isImpassable(World world, double x, double y, double radius){
		//TODO
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is passable and adjacent to impassable terrain. 
	 * 
	 * @param world The world in which to check adjacency
	 * @param x The x-coordinate of the center of the circle to check  
	 * @param y The y-coordinate of the center of the circle to check
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 */
	boolean isAdjacent(World world, double x, double y, double radius){
		//TODO
	}
	
	/**
	 * Variable referencing a rectangular matrix indicating which parts 
	 * of the terrain are passable and impassable (true or false).
	 */
	private final boolean[][] passableMap;
	

	
	/**
	 * Variable registering the upper bound for all worlds.
	 */
	private static final double UPPER_BOUND = Double.MAX_VALUE; 
	
	
	
	/**
	 * Starts a game in the given world.
	 */
	void startGame(World world){
		//TODO
	}
	
	//start
	

	
	
	/**
	 * Returns whether the game in the given world has finished.
	 */
	boolean isGameFinished(World world){
		//TODO
	}
	
	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner. This method should null if there is no winner.
	 * 
	 * (For single-student groups that do not implement teams, this method should always return the name of the winning worm, or null if there is no winner)
	 */
	String getWinner(World world){
		//TODO
	}
	
	/**
	 * Create and add an empty team with the given name to the given world.
	 * 
	 * 
	 */
	void addEmptyTeam(World world, String newName){
		//TODO
	}
	
	//TEAMNAMES
	
	
	
	/**
	 * Returns all the food rations in the world
	 * 
	 * (For single-student groups that do not implement food, this method must always return an empty collection)
	 */
	Collection<Food> getFood(World world){
		//TODO
	}
	
	/**
	 * Create and add a new food ration to the given world.
	 * The food must be placed at a random adjacent location.
	 * 
	 * 
	 */
	void addNewFood(World world){
		//TODO
	}
	
	//FOOD
	
	
	/**
	 * Returns the active worm in the given world (i.e., the worm whose turn it is).
	 */
	Worm getCurrentWorm(World world){
		//TODO
	}
	
	/**
	 * Returns all the worms in the given world
	 */
	Collection<Worm> getWorms(World world){
		//TODO
	}

	
	/**
	 * Starts the next turn in the given world
	 */
	void startNextTurn(World world){
		//TODO
	}
	
	/**
	 * Create and add a new worm to the given world.
	 * The new worm must be placed at a random adjacent location.
	 * The new worm can have an arbitrary (but valid) radius and direction.
	 * The new worm may (but isn't required to) have joined a team.
	 */
	void addNewWorm(World world){
		
	}
	
	//WORM
	
	
	/**
	 * Returns the active projectile in the world, or null if no active projectile exists.
	 */
	public Projectile getActiveProjectile(World world){
		//TODO
	}
	
	//PROJECTILE

}
