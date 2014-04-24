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
	private Worm getWorm(){
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
		if(this.currentWeapon.equals(" "))
			return null;
		return this.currentWeapon;
	}
	
	//TODO documentation
	public double getMassOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 0.300;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 0.010;
		else
			return 0;
	}
	
	//TODO documentation
	public double getRadiusOfWeapon() 
			throws IllegalRadiusException{
		double newRadius;
		if(this.getMassOfWeapon() > 0.0){
			newRadius = Math.pow((3.0/4.0)*this.getMassOfWeapon()/
					(DENSITY * Math.PI), (1/3.0));
		}
		else
			newRadius = 0.0;
		if(!canHaveAsRadius(newRadius))
			throw new IllegalRadiusException(newRadius);
		return newRadius;
	}
	
	/**
	 * Activates the next weapon for the worm.
	 */
	//TODO documentation
	public void selectNextWeapon()
	{
		if(this.getCurrentWeapon() == null)
			setCurrentWeapon("Bazooka");
		else if(this.getCurrentWeapon().equals("Bazooka"))
			setCurrentWeapon("Rifle");
		else if(this.getCurrentWeapon().equals("Rifle"))
			setCurrentWeapon(" ");
	}
	
	public void shoot(int propulsion){
		if(getCostOfActionPointsOfWeapon() <= getWorm().getCurrentActionPoints()){
			try{
				@SuppressWarnings("unused")
				Projectile projectile = new Projectile(this.getWorm(), this.getInitialVelocity(propulsion));
				this.getWorm().reduceCurrentActionPoints(getCostOfActionPointsOfWeapon());
			}
			catch(IllegalRadiusException exc){}
			catch(IllegalArgumentException exc){}
		}
	}
	
	//TODO documentation
	private double getInitialVelocity(int propulsion){
		double initialVelocity = 0.0;
		if(this.getCurrentWeapon().equals("Bazooka"))
			initialVelocity = 2.5 + 7.0*(propulsion/100.0);
		else if(this.getCurrentWeapon().equals("Rifle"))
			initialVelocity = 1.5;
		
		return (initialVelocity / getMassOfWeapon()) * 0.5;
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
	
	private int getCostOfActionPointsOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 50;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 10;
		else
			return 0;
	}
	
	private int getDamageOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 80;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 20;
		else
			return 0;
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
	private String currentWeapon = " ";
	
	/**
	 * Final class variable registering the density of all weapons (in kg/m^3).
	 */
	private final static double DENSITY = 7800.0;
}
