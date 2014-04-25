package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * A class of food rations involving an x-coordinate, an y-coordinate, 
 * a radius (in meter), the activity of food rations and a world.
 * 
 * @invar	| isValidPosition(getX(),getY())
 * @invar	| hasProperWorld()
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public class Food {
	
	/**
	 * Create a new food ration that is positioned at the given
	 * coordinates.
	 * 
	 * @param 	x
	 * 			The x-coordinate of this new food ration (in meter).
	 * @param 	y
	 * 			The y-coordinate of this new food ration (in meter).
	 * @effect	| this.setPosition(x,y)
	 */
	public Food(double x, double y) 
			throws IllegalArgumentException{
		this.setPosition(x,y);
	}
	
	
	
	/**
	 * Returns whether or not this food ration is alive (active), i.e., not eaten.
	 */
	@Basic @Raw
	public boolean isActive(){
		return this.isActive;
	}
	
	/**
	 * Deactivate this food.
	 * 
	 * @post	| ( !new.isActive() )
	 * @effect	| world.removeAsFood(this)
	 */
	public void deactivate(){
		world.removeAsFood(this);
		this.isActive = false;
	}
	
	/**
	 * Variable registering whether or not this food is active.
	 */
	private boolean isActive = true;
	
	
	
	/**
	 * Return the world to which this food is attached.
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Check whether this food can be attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to check.
	 * @return	| result == ( (world == null) || 
	 * 			|				(world.canHaveAsFood(this) )
	 */
	@Raw
	public boolean canHaveAsWorld(World world){
		return world == null || world.canHaveAsFood(this);
	}
	
	/**
	 * Check whether this food has a proper world to
	 * which it is attached.
	 * 
	 * @return	| result == ( canHaveAsWorld(getWorld()) &&
	 * 			|				( (getWorld() == null) ||
	 * 			| 					getWorld().hasAsFood(this)))
	 */
	@Raw
	public boolean hasProperWorld(){
		return canHaveAsWorld(getWorld()) && 
				(getWorld() == null || getWorld().hasAsFood(this));
	}
	
	/**
	 * Set the world to which this food is attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to attach this food to.
	 * @pre		| if(world != null)
	 * 			|	then world.hasAsFood(this)
	 * @pre		| if( (world == null) && (getWorld() != null) )
	 * 			| 	then !getWorld().hasAsFood(this)
	 * @post	| new.getWorld() == world
	 */
	@Raw
	void setWorld(@Raw World world){
		assert(world == null || world.hasAsFood(this));
		assert(world != null || getWorld() == null || !getWorld().hasAsFood(this));
		this.world = world;
	}
	
	/**
	 * Variable referencing the world to which this food is attached.
	 */
	private World world = null;
	
	
	
	/**
	 * Checks whether or not the given position is a valid position 
	 * in the world where this food ration belongs to.
	 * 
	 * @param 	x
	 * 			The x-coordinate to check.
	 * @param 	y
	 * 			The y-coordinate to check.
	 * @return	| x >= 0 && y >= 0
	 */
	@Raw
	public boolean isValidPosition(double x, double y){
		return x >= 0 && y >= 0;
	}
	
	/**
	 * Returns the x-coordinate of this food ration.
	 */
	@Basic @Raw
	public double getX(){
		return this.x;
	}
	
	/**
	 * Returns the y-coordinate of this food ration.
	 */
	@Basic @Raw
	public double getY(){
		return this.y;
	}
	
	/**
	 * Sets the x-coordinate and y-coordinate to the given coordinates.
	 * 
	 * @param 	x
	 * 			The x-coordinate to set.
	 * @param 	y
	 * 			The y-coordinate to set.
	 * @throws 	IllegalArgumentException("This is not a valid position to place a food ration!")
	 * 			| !isValidPosition(x,y)
	 */
	@Model @Raw
	private void setPosition(double x, double y) 
			throws IllegalArgumentException{
		if(! isValidPosition(x,y))
			throw new IllegalArgumentException("This is not a valid position to place a food ration!");
		this.x = x;
		this.y = y;
	}
	
	/**
	 * The x-coordinate of this food ration.
	 */
	private double x = 0;
	
	/**
	 * The y-coordinate of this food ration.
	 */
	private double y = 0;
	
	
	
	/**
	 * Returns the radius of this food ration.
	 */
	@Basic @Raw
	public static double getRadius(){
		return RADIUS;
	}
	
	/**
	 * The radius of a food ration.
	 */
	private static final double RADIUS = 0.20;
}
