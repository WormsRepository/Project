package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of projectile associated with a world involving an initial velocity,
 * a radius, a damage, a hitted worm, an x-coordinate, an y-coordinate, a direction,
 * a standard acceleration and a world.
 *
 * @version 1.0
 * @author Laurens Loots, Pieter Vos
 */
public class Projectile extends Position{
	
	/**
	 * Create a new projectile with the given arguments.
	 * 
	 * @param 	worm
	 * 			The worm that 'shoots' this projectile.
	 * @param 	initialVelocity
	 * 			The initial velocity for the shot of this projectile.
	 * @param 	radius
	 * 			The radius of this projectile.
	 * @param 	damage
	 * 			The damage of this projectile.
	 * @pre		| isValidDirection(worm.getDirection())
	 * @post	| new.getDirection() == worm.getDirection()
	 * @post	| new.getInitialVelocity() == initialVelocity
	 * @post	| new.getRadius() == radius
	 * @post	| new.getDamage() == damage
	 * @effect	| this.setWorld(worm.getWorld())
	 * @effect	| try(
	 * 			|	this.setInitialPosition(worm.getPosition().getX(), 
	 * 			|	worm.getPosition().getY(), worm.getRadius())
	 * 			| )
	 * 			| catch(IllegalArgumentException exc)(
	 * 			| 	this.deactivate();
	 * 			| 	throw exc;
	 * 			| )
	 * @throws 	IllegalRadiusException(radius)
	 * 			| !canHaveAsRadius(radius)
	 */
	public Projectile(Worm worm, double initialVelocity, double radius, int damage) 
			throws IllegalRadiusException, IllegalArgumentException, IllegalPositionException{
		this.direction = worm.getDirection();
		this.initialVelocity = initialVelocity;
		if(!canHaveAsRadius(radius))
			throw new IllegalRadiusException(radius);
		this.radius = radius;
		this.damage = damage;
		
		this.setWorld(worm.getWorld());
		try{
			this.setInitialPosition(worm.getWormPosition().getX(), 
					worm.getWormPosition().getY(), worm.getRadius());
		}
		catch(IllegalArgumentException exc){
			this.deactivate();
			throw exc;
		}
	}
	
	
	
	/**
	 * Returns whether or not this projectile is still active.
	 */
	@Basic @Raw
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Deactivate the projectile.
	 * 
	 * @post	| new.getWorld() == null
	 * @post	| new.isActive() == false
	 * @effect	| (new getWorld()).setProjectile(null)
	 */
	public void deactivate(){
		World tempWorld = getWorld();
		this.world = null;
		tempWorld.setProjectile(null);
		this.isActive = false;
	}
	
	/**
	 * Variable registering whether or not this projectile is active.
	 */
	private boolean isActive = true;
	
	
	
	/**
	 * Return the hitted worm, if any.
	 */
	@Model @Basic @Raw
	private Worm getHittedWorm(){
		return this.hittedWorm;
	}
	
	/**
	 * Set the hitted worm to the given worm.
	 * 
	 * @param 	worm
	 * 			The worm to set.
	 * @post	| new.getHittedWorm() == worm
	 */
	private void setHittedWorm(Worm worm){
		this.hittedWorm = worm;
	}
	
	/**
	 * Worm referencing the hitted worm, if there is one.
	 */
	private Worm hittedWorm = null;
	
	
	
	


	/**
	 * Return the position of the projectile at a given time in a jump.
	 * 
	 * @param 	t
	 * 			The time to check the position of the projectile.
	 * @return	The position of the projectile at the given time in the jump.
	 * 			| result == 
	 * 			|	{getX() + getInitialVelocity() * Math.cos(this.getDirection()) * t,
	 * 			| 	getY() + getInitialVelocity() * Math.sin(this.getDirection())*t - 
	 * 			|		(STANDARD_ACCELERATION*Math.pow(t,2))/2.0}
	 */
	public double[] getJumpStep(double t) {
		double horizontalVelocity = getInitialVelocity() * Math.cos(this.getDirection());
		double xPosition = getX() + horizontalVelocity * t;
		double verticalVelocity = getInitialVelocity() * Math.sin(this.getDirection());
		double yPosition = getY() + verticalVelocity*t - (STANDARD_ACCELERATION*Math.pow(t,2))/2.0;
		double[] position = {xPosition, yPosition};
		return position;
	}
	
	//TODO documentation
	public double getJumpTime(double timeStep) 
			throws NullPointerException, IllegalDirectionException{
		if(this.getWorld() == null)
			throw new NullPointerException();
		double[] tempXY = {getX(),getY()};
		double radius = this.getRadius();
		
		// This temporary variable has to be incremented with a really small value, 
		// with this part of the method we make sure the tempX and tempY are no longer
		// at a adjacent location so we can detect to real collision in the next part 
		// and not the initial position. We also check if the jump in the current 
		// direction is worth to do it, if not, this method throws
		// an IllegalDirectionException.
		double temp = timeStep;
		double tempTime = 0.0;
		while(this.getWorld().isAdjacent(tempXY[0], tempXY[1], radius) && tempTime < (1/8.0)
				&& this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius) == null){
			if(this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius) != null){
				return tempTime;
			}
			tempTime = tempTime + temp;
			tempXY = getJumpStep(tempTime);
		}
		if(this.getWorld().isImpassable(tempXY[0], tempXY[1], radius)){
			tempTime = tempTime - (temp/2.0);
			tempXY = getJumpStep(tempTime);
			if(this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius) == null)
				throw new IllegalDirectionException(this.getDirection());
		}
		//TODO deze exception verschillend aanpakken als een worm gehit is...
		
		// if 'temp' is smaller than 1/400000 the projectile will leave the world because there is no
		// possible adjacent position.
		while(!this.getWorld().isAdjacent(tempXY[0], tempXY[1], radius) && temp >= (1/400000.0)
				&& this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius) == null){
			while(!this.getWorld().isImpassable(tempXY[0], tempXY[1], radius)
					&& this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius) == null){
				tempTime = tempTime + temp;
				tempXY = getJumpStep(tempTime);
			}
			temp = temp / 3.0;
			while(this.getWorld().isImpassable(tempXY[0], tempXY[1], radius)){
				tempTime = tempTime - temp;
				tempXY = getJumpStep(tempTime);
			}
			temp = temp / 3.0;
		}
		
		setHittedWorm(this.getWorld().hitAnyWorm(tempXY[0], tempXY[1], radius));
		
		return tempTime;
	}
	
	/**
	 * Jump with the projectile, this represents the actual shot of the projectile.
	 * 
	 * @param 	timeStep
	 * 	 	 	An elementary time interval during which you may assume
	 *        	that the projectile will not completely move through a piece of impassable terrain.
	 * @effect	| setPosition(getJumpStep(getJumpTime(timeStep))[0],
	 * 			|					getJumpStep(getJumpTime(timeStep))[1])
	 * @effect	| if(getHittedWorm() != null)
	 * 			|	then(getHittedWorm().reduceCurrentHitPoints(this.getDamage()))
	 * @effect	| deactivate()
	 */
	public void jump(double timeStep) 
			throws NullPointerException, IllegalDirectionException{
		double[] tempXY = getJumpStep(getJumpTime(timeStep));
		setPosition(tempXY[0],tempXY[1]);
		if(getHittedWorm() != null)
			getHittedWorm().reduceCurrentHitPoints(this.getDamage());
		deactivate();
	}
	
	/**
	 * Sets the initial position of the projectile.
	 * 
	 * @param 	xWorm
	 * 			The x coordinate of the worm which shoots this projectile.
	 * @param 	yWorm
	 * 			The y coordinate of the worm which shoots this projectile.
	 * @param 	wormRadius
	 * 			The radius of the worm which shoots this projectile.
	 * @effect	| setPosition(xWorm + Math.cos(this.getDirection())*
	 * 			| 	(this.getRadius() + wormRadius)*1.05, 
	 * 			|		yWorm + Math.sin(this.getDirection())*
	 * 			|			(this.getRadius() + wormRadius)*1.05)
	 */
	@Model @Raw
	private void setInitialPosition(double xWorm, double yWorm, double wormRadius) 
			throws IllegalPositionException{
		double resultingRadius = this.getRadius() + wormRadius;
		double xPos = xWorm + Math.cos(this.getDirection())*resultingRadius*1.05;
		double yPos = yWorm + Math.sin(this.getDirection())*resultingRadius*1.05;
		setPosition(xPos, yPos);
	}
	
	
	

	/**
	 * Return the radius of this projectile (in meter).
	 */
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Check whether the given radius is a valid radius for this projectile.
	 * 
	 * @param 	radius
	 * 			The radius to check.
	 * @return	True if and only if the given radius is larger than zero.
	 * 			| radius > 0
	 */
	@Raw @Model
	private boolean canHaveAsRadius(double radius){
		return radius > 0;
	}
	
	/**
	 * 	Variable registering the radius of a projectile.
	 */
	private final double radius;
	
	
	
	/**
	 * Return the damage of this projectile.
	 */
	@Basic @Raw
	public int getDamage(){
		return this.damage;
	}
	
	/**
	 * Variable registering the possible damage of the weapon this projectile represents.
	 */
	private final int damage;
	
	
	
	/**
	 * Check whether the given direction is a valid direction for any projectile.
	 * 
	 * @param 	direction
	 * 			The direction to check.
	 * @return	True if and only if the given direction is not below zero and not above or equal to 2 pi.
	 * 			| result == ( (direction >= 0) && (direction < 2*Math.PI) )
	 */
	@Raw
	public boolean isValidDirection(double direction)
	{
		return ( (direction >= 0) && (direction < 2*Math.PI) );
	}
	
	/**
	 * Return the direction of this projectile.
	 */
	@Basic @Raw
	public double getDirection(){
		return this.direction;
	}
	
	/**
	 * Variable registering the direction of the projectile (in radians).
	 */
	private final double direction;
	
	
	
	/**
	 * Return the initial velocity of this projectile.
	 */
	@Basic @Raw
	public double getInitialVelocity(){
		return this.initialVelocity;
	}
	
	/**
	 * Variable registering the initial velocity of the projectile (in N).
	 */
	private final double initialVelocity;
	
	
	/**
	 * Check whether the given world is a valid world.
	 * 
	 * @param 	world
	 * 			The world to check.
	 * @return	| result == (world != null)
	 */
	public boolean isValidWorld(World world){
		return world != null;
	}
	
	/**
	 * Return the reference to the world to which this projectile belongs
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Set the world of this projectile to the given world.
	 * 
	 * @param 	world
	 * 			The world to set.
	 * @post	| new.getWorld() = world
	 * @effect	| world.setProjectile(this)
	 * @throws	IllegalArgumentException()
	 * 			| !isValidWorld(world) || 
	 * 			| (world != null && world.getProjectile() != null)
	 */
	public void setWorld(World world) 
			throws IllegalArgumentException{
		if(!isValidWorld(world))
			throw new IllegalArgumentException();
		if(world != null && world.getProjectile() != null)
			throw new IllegalArgumentException();
		this.world = world;
		world.setProjectile(this);
	}
	
	/**
	 * Variable referencing the world to which this projectile belongs.
	 */
	private World world = null;
	
	
	
	/**
	 * Final class variable registering the standard acceleration (m/(s*s)).
	 */
	private final static double STANDARD_ACCELERATION = 9.80665;


	
	/**
	 * Check if the given position is a valid position for any projectile.
	 * 
	 * @Return	If the world attached to this projectile exists it is 
	 * 			true if and only if the projectile does not lie 
	 * 			in an impassable part of the map.
	 * 			| result == !this.getWorld().isImpassable(x,y,this.getRadius())
	 */
	@Override @Model
	protected boolean isValidPosition(double x, double y) {
		if(this.getWorld() == null)
			return x>=0 && y>=0;
		return !this.getWorld().isImpassable(x,y,this.getRadius());
	}
}
