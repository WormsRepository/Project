package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Projectile {
	
	public Projectile(Worm worm) 
			throws IllegalRadiusException, IllegalArgumentException{
		this.weapon = worm.getWeapon();
		this.mass = weapon.getMassOfWeapon();
		this.radius = weapon.getRadiusOfWeapon();
		this.world = worm.getWorld();
		//TODO associatie met world aanvullen
		this.direction = worm.getDirection();
		this.setInitialPosition(worm.getPosition().getX(), 
				worm.getPosition().getY(), worm.getRadius());
	}
	
	/**
	 * Returns whether or not this projectile is still active.
	 */
	@Basic @Raw
	public boolean isActive() {
		return this.isActive;
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
		double xPos = xWorm + Math.cos(this.getDirection())*resultingRadius;
		double yPos = yWorm + Math.sin(this.getDirection())*resultingRadius;
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
	
	
	
	/**
	 * Return the reference to the world to which this projectile belongs
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Variable referencing the world to which this projectile belongs.
	 */
	private final World world;
}
