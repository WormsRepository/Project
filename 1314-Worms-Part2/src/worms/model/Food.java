package worms.model;

import be.kuleuven.cs.som.annotate.*;


/**
 * A class of food rations involving an x-coordinate, an y-coordinate, 
 * a radius (in meter), the activity of food rations and a world.
 * 
 * @invar	| hasProperWorld()
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public class Food extends Position{
	
	/**
	 * Create a new food ration that is positioned at the given
	 * coordinates.
	 * 
	 * @param 	x
	 * 			The x-coordinate of this new food ration (in meter).
	 * @param 	y
	 * 			The y-coordinate of this new food ration (in meter).
	 * @effect	| super(x,y)
	 */
	public Food(double x, double y) 
			throws IllegalPositionException{
		super(x,y);
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
	 * Return the world to which this food is attached.
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
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
	 * Returns the radius of this food ration.
	 */
	@Basic @Raw @Immutable
	public static double getRadius(){
		return RADIUS;
	}
	
	/**
	 * The radius of a food ration.
	 */
	private static final double RADIUS = 0.20;



	/**
	 * Check if the given position is a valid position for any food.
	 * 
	 * @return 	The food ration must lie fully within the borders of the world.
	 * 			| if(this.getWorld() != null)
	 * 			|	then( result == (x>RADIUS && 
	 * 			|		x<this.getWorld().getWidth() - RADIUS &&
				|		y>RADIUS && y<this.getWorld().getHeight() - RADIUS) )
	 */
	@Override
	protected boolean isValidPosition(double x, double y) {
		if(this.getWorld() == null)
			return x>=0 && y>=0;
		return x>RADIUS && x<this.getWorld().getWidth() - RADIUS &&
				y>RADIUS && y<this.getWorld().getHeight() - RADIUS;
	}
}
