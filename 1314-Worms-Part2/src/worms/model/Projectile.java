package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Projectile {
	
	public Projectile(Worm worm){
		this.weapon = worm.getWeapon();
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

	public double getJumpTime(double timeStep) {
		// TODO methode maken
		return 0;
	}
	
	public void jump(double timeStep) {
		// TODO methode maken
		
	}


	
	@Model @Raw
	private void setPosition(double x, double y){
		//TODO methode maken
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
	 * Check whether the given radius is a valid radius for this projectile.
	 * 
	 * @param 	radius
	 * 			The radius to check.
	 * @return	True if and only if the given radius is larger than zero.
	 * 			| radius > 0
	 */
	@Raw
	public boolean canHaveAsRadius(double radius){
		return radius > 0;
	}
	/**
	 * Return the radius of this projectile (in meter).
	 */
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	@Model @Raw
	private void setRadius(double radius){
		if(!canHaveAsRadius(radius))
			throw new IllegalRadiusException(radius);
		//TODO setradius
	}
	
	/**
	 * 	Variable registering the radius of a projectile.
	 */
	private final double radius = 0;
	
	
	
	/**
	 * Variable registering the mass of a projectile (in kilograms).
	 */
	private final double mass = 0;
	
	
	/**
	 * 
	 */
	//TODO documentation
	@Basic @Raw
	public Weapon getWeapon(){
		return this.weapon;
	}
	
	/**
	 * Variable referencing the weapon that this projectile represents.
	 */
	private final Weapon weapon;
	
	
	
	/**
	 * Final class variable registering the density of all projectiles (in kg/m^3).
	 */
	private final static double DENSITY = 7800;
}
