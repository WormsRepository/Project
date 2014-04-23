package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Projectile {
	
	public Projectile(Worm worm, double initialForce) 
			throws IllegalRadiusException, IllegalArgumentException{
		this.weapon = worm.getWeapon();
		this.mass = weapon.getMassOfWeapon();
		this.radius = weapon.getRadiusOfWeapon();
		this.direction = worm.getDirection();
		this.initialForce = initialForce;
		
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
		// TODO Auto-generated method stub
		return null;
	}
	
	public double getJumpTime(double timeStep) {
		// TODO methode maken
		return 0;
	}
	
	public void jump(double timeStep) {
		// TODO methode maken
		
	}

	private boolean inMap(double x, double y){
		double radius = this.getRadius();
		return x>radius && x<this.getWorld().getWidth() - radius &&
				y>radius && y<this.getWorld().getHeight() - radius;
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
		if(!isValidPosition(x,y))
			throw new IllegalArgumentException("Invalid Position");
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
	 * 	Variable registering the radius of a projectile.
	 */
	private final double radius;
	
	
	
	/**
	 * Return the mass of this projectile (in kilogram).
	 */
	@Basic @Raw
	public double getMass(){
		return this.mass;
	}
	
	/**
	 * Variable registering the mass of the projectile (in kilograms).
	 */
	private final double mass;
	
	
	
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
	 * Return the initial force of this projectile.
	 */
	@Basic @Raw
	public double getInitialForce(){
		return this.initialForce;
	}
	
	/**
	 * Variable registering the initial force of the projectile (in N).
	 */
	private final double initialForce;
	
	
	
	/**
	 * Return the reference to the actual weapon this projectile represents.
	 */
	@Basic @Raw
	public Weapon getWeapon(){
		return this.weapon;
	}
	
	/**
	 * Variable referencing the weapon that this projectile represents.
	 */
	private final Weapon weapon;
	
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
}
