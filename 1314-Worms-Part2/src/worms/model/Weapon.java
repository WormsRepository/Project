package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
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

}
