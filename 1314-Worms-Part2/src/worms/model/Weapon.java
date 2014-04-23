package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

public class Weapon {
	
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
	
	/**
	 * Activates the next weapon for the worm.
	 */
	//TODO documentation
	public void selectNextWeapon()
	{
		if(this.currentWeapon.equals(" "))
			this.currentWeapon = "Bazooka";
		if(this.currentWeapon.equals("Bazooka"))
			this.currentWeapon = "Rifle";
		if(this.currentWeapon.equals("Rifle"))
			this.currentWeapon = " ";
	}
	
	/**
	 * Variable referencing the weapon of a worm.
	 */
	String currentWeapon = " ";

}
