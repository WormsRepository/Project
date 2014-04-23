package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Weapon {
	
	//TODO documentation
	public Weapon(Worm worm){
		this.worm = worm;
	}
	
	//TODO documentation
	@Basic @Raw
	protected Worm getWorm(){
		return this.worm;
	}
	
	/**
	 * Variable registering the worm to which this weapon belongs.
	 */
	private final Worm worm;
	
	//TODO documentation
	private boolean isValidWeapon(String weapon){
		return (weapon.equals(" ") || weapon.equals("Bazooka") || weapon.equals("Rifle"));
	}
	
	//TODO documentation
	/**
	 * Returns the name of the weapon that is currently active for the given worm,
	 * or null if no weapon is active.
	 */
	@Basic @Raw
	public String getCurrentWeapon(){
		return this.currentWeapon;
	}
	
	/**
	 * Activates the next weapon for the worm.
	 */
	//TODO documentation
	public void selectNextWeapon()
	{
		if(this.getCurrentWeapon().equals(" "))
			setCurrentWeapon("Bazooka");
		if(this.getCurrentWeapon().equals("Bazooka"))
			setCurrentWeapon("Rifle");
		if(this.getCurrentWeapon().equals("Rifle"))
			setCurrentWeapon(" ");
	}
	
	//TODO documentation
	public double getMassOfWeapon(){
		if(this.currentWeapon.equals("Bazooka"))
			return 0.300;
		else if(this.currentWeapon.equals("Rifle"))
			return 0.010;
		else
			return 0;
	}
	
	//TODO documentation
	public double getRadiusOfWeapon() 
			throws IllegalRadiusException{
		double newRadius;
		if(this.getMassOfWeapon() > 0){
			newRadius = Math.pow((3/4)*this.getMassOfWeapon()/
					(DENSITY * Math.PI), (1/3));
		}
		else
			newRadius = 0;
		if(!canHaveAsRadius(newRadius))
			throw new IllegalRadiusException(newRadius);
		return newRadius;
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
	
	private double getCostOfActionPointsOfWeapon(){
		return 0;
		//TODO implementeren
	}
	
	//TODO documentation
	@Raw
	private void setCurrentWeapon(String weapon){
		if(isValidWeapon(weapon))
			this.currentWeapon = weapon;
	}
	
	/**
	 * Variable referencing the weapon of a worm.
	 */
	String currentWeapon = " ";
	
	/**
	 * Final class variable registering the density of all weapons (in kg/m^3).
	 */
	private final static double DENSITY = 7800;
}
