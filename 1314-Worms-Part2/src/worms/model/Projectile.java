package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Projectile {
	
	public Projectile(Worm worm, double initialVelocity, double radius, int damage) 
			throws IllegalRadiusException, IllegalArgumentException{
		this.direction = worm.getDirection();
		this.initialVelocity = initialVelocity;
		if(!canHaveAsRadius(radius))
			throw new IllegalRadiusException(radius);
		this.radius = radius;
		this.damage = damage;
		
		this.setWorld(worm.getWorld());
		try{
			this.setInitialPosition(worm.getPosition().getX(), 
					worm.getPosition().getY(), worm.getRadius());
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
	
	//TODO documentation
	public void deactivate(){
		World tempWorld = getWorld();
		this.world = null;
		tempWorld.setProjectile(null);
		this.isActive = false;
	}
	
	private Worm getHittedWorm(){
		return this.hittedWorm;
	}
	
	private void setHittedWorm(Worm worm){
		this.hittedWorm = worm;
	}
	
	/**
	 * Worm referencing the hitted worm, if there is one.
	 */
	private Worm hittedWorm = null;
	
	/**
	 * Variable registering whether or not this projectile is active.
	 */
	private boolean isActive = true;
	
	
	/**
	 * Return the x-coordinate of the position of the projectile (in meter).
	 */
	@Basic @Raw
	public double getX() {
		return this.x;
	}
	
	/**
	 * Return the y-coordinate of the position of the projectile (in meter).
	 */
	@Basic @Raw
	public double getY() {
		return this.y;
	}

	public double[] getJumpStep(double t) {
		double horizontalVelocity = getInitialVelocity() * Math.cos(this.getDirection());
		double xPosition = getX() + horizontalVelocity * t;
		double verticalVelocity = getInitialVelocity() * Math.sin(this.getDirection());
		double yPosition = getY() + verticalVelocity*t - (STANDARD_ACCELERATION*Math.pow(t,2))/2.0;
		double[] position = {xPosition, yPosition};
		return position;
	}
	
	
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
		
		// We have chosen (1/4)/getInitialVelocity() because with the maximum horizontal or
		// vertical velocity the worm can only move the minimum radius in the horizontal or vertical
		// direction per step, this equals the minimum radius, so we will probably not skip
		// any impassable point of the map.
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
		if(this.getWorld().isImpassable(tempXY[0], tempXY[1], radius))
			throw new IllegalDirectionException(this.getDirection());
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
	//TODO documentation
	public void jump(double timeStep) 
			throws NullPointerException{
		double[] tempXY = getJumpStep(getJumpTime(timeStep));
		setPosition(tempXY[0],tempXY[1]);
		if(getHittedWorm() != null)
			getHittedWorm().reduceCurrentHitPoints(this.getDamage());
		
		deactivate();
	}
	
	//TODO documenation
	@Model
	private boolean isValidPosition(double x, double y){
		return !this.getWorld().isImpassable(x,y,this.getRadius());
	}
	
	//TODO documentation
	@Model @Raw
	private void setPosition(double x, double y) 
			throws IllegalArgumentException{
		if(!isValidPosition(x,y)){
			String z = "Invalid Position: x-pos: " + x + " y-pos: "+ y + " radius: " + getRadius();
			throw new IllegalArgumentException(z);
		}
		this.x = x;
		this.y = y;
	}
	
	//TODO documentation
	@Model @Raw
	private void setInitialPosition(double xWorm, double yWorm, double wormRadius) 
			throws IllegalArgumentException{
		double resultingRadius = this.getRadius() + wormRadius;
		double xPos = xWorm + Math.cos(this.getDirection())*resultingRadius*1.05;
		double yPos = yWorm + Math.sin(this.getDirection())*resultingRadius*1.05;
		setPosition(xPos, yPos);
	}
	
	/**
	 * Variable registering the x-coordinate of a position of a projectile in meters.
	 */
	private double x;

	/**
	 * Variable registering the y-coordinate of a position of a projectile in meters.
	 */
	private double y;
	
	

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
	
	//TODO documentation
	public void setWorld(World world){
		if(!isValidWorld(world))
			throw new IllegalArgumentException();
		if(world.getProjectile() != null && world != null)
			throw new IllegalArgumentException();
		this.world = world;
		world.setProjectile(this);
	}
	
	/**
	 * Variable referencing the world to which this projectile belongs.
	 */
	private World world;
	
	
	
	/**
	 * Final class variable registering the standard acceleration (m/(s*s)).
	 */
	private final static double STANDARD_ACCELERATION = 9.80665;
}
