package worms.model;


import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
/**
 * A class of weapons associated with a worm.
 * 
 * @invar 	| isValidWeapon(getCurrentWeapon())
 *
 * @version 1.0
 * @author Laurens Loots, Pieter Vos
 */
public class Weapon {
	
	/**
	 * creates a new weapon associated with the given worm.
	 * 
	 * @param 	worm
	 * 			the given worm
	 * @effect	|this.getWorm() == worm
	 */
	public Weapon(Worm worm){
		this.worm = worm;
	}
	
	/**
	 * returns the worm this weapon is attached to.
	 */
	@Basic @Raw @Model
	private Worm getWorm(){
		return this.worm;
	}
	
	/**
	 * Variable registering the worm to which this weapon belongs.
	 */
	private final Worm worm;
	
	/**
	 * checks whether or not the given String is a valid weapon.
	 * 
	 * @param 	weapon
	 * 			the String to check.

	 * @return	| weapon.equals(" ") || weapon.equals("Bazooka") || weapon.equals("Rifle")
	 */
	@Model
	private boolean isValidWeapon(String weapon){
		return (weapon.equals(" ") || weapon.equals("Bazooka") || weapon.equals("Rifle"));
	}
	
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
	 * 
	 * @post 	|if(getCurrentWeapon() == null
	 * 			|	then new.getCurrentWeapon().equals("Bazooka")
	 * @post	|if(getCurrentWeapon() == "Bazooka"
	 * 			|	then new.getCurrentWeapon().equals("Rifle)
	 * @post	|if(getCurrentWeapon() == "Rifle'
	 * 			| 	then new.getCurrentWeapon().equals(" ")
	 */
	public void selectNextWeapon()
	{
		if(this.getCurrentWeapon() == null)
			setCurrentWeapon("Bazooka");
		else if(this.getCurrentWeapon().equals("Bazooka"))
			setCurrentWeapon("Rifle");
		else if(this.getCurrentWeapon().equals("Rifle"))
			setCurrentWeapon(" ");
	}
	
	/**
	 * Creates a new Projectile if and only if the cost of action points of the weapon is smaller or
	 * equal to the current action points of the worm.
	 * 
	 * @param 	propulsion
	 * 			The initial velocity the new Projectile starts with.
	 * @post	a new object of the class projectile is created.
	 */
	public void shoot(int propulsion){
		if(getCostOfActionPointsOfWeapon() <= getWorm().getCurrentActionPoints()){
			try{
				@SuppressWarnings("unused")
				Projectile projectile = new Projectile(this.getWorm(),this.getInitialVelocity(propulsion),
						this.getRadiusOfWeapon(), this.getDamageOfWeapon());
				this.getWorm().reduceCurrentActionPoints(getCostOfActionPointsOfWeapon());
			}
			catch(IllegalRadiusException exc){}
			catch(IllegalArgumentException exc){}
		}
	}
	

	
	/**
	 * Calculates and returns the radius of this weapon.
	 */
	@Model
	private double getRadiusOfWeapon(){
		double newRadius;
		if(this.getMassOfWeapon() > 0.0){
			newRadius = Math.pow((3.0/4.0)*this.getMassOfWeapon()/
					(DENSITY * Math.PI), (1/3.0));
		}
		else
			newRadius = 0.0;
		return newRadius;
	}
	
	
	/**
	 * returns the mass of this weapon based on the current weapon.
	 * 
	 * @return	the mass of the weapon
	 * 			| if(getCurrentWeapon().equals("Bazooka")
	 * 			| 		then ( return 0.300 )
	 * 			| if(getCurrentWeapon().equals("Rifle")
	 * 			|		then ( return 0.010)
	 * 			| else	( return 0 )
	 */
	@Model
	private double getMassOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 0.300;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 0.010;
		else
			return 0;
	}
	/**
	 * Calculate the initial velocity for the projectile based on the propulsion.
	 * 
	 * @return	The initial velocity of the projectile
	 * 			| if(getCurrentWeapon().equals("Bazooka")
	 * 			|		then ( initialVelocity = 2.5 + 7.0*(propulsion/100.0) )
	 * 			| if(getCurrentWeapon().equals("Rifle")
	 * 			|		then ( initialVelocity = 1.5 )
	 */
	@Model
	private double getInitialVelocity(int propulsion){
		double initialVelocity = 0.0;
		if(this.getCurrentWeapon().equals("Bazooka"))
			initialVelocity = 2.5 + 7.0*(propulsion/100.0);
		else if(this.getCurrentWeapon().equals("Rifle"))
			initialVelocity = 1.5;
		
		return (initialVelocity / getMassOfWeapon()) * 0.5;
	}
	
	/**
	 * returns the cost of action points of the weapon, based on the currently selected weapon.
	 * 
	 * @return	the cost of action points 
	 * 			| if(getCurrentWeapon().equals("Bazooka")
	 * 			|		then ( return 50 )
	 * 			| if(getCurrentWeapon().equals("Rifle")
	 * 			|		then ( return 10 )
	 * 			| else
	 * 			|		(return 0)
	 */
	@Model
	private int getCostOfActionPointsOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 50;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 10;
		else
			return 0;
	}
	
	/**
	 * returns the damage of the weapon based on the weapon.
	 * 
	 * @return	The damage of the weapon
	 * 			| if(getCurrentWeapon().equals("Bazooka")
	 * 			|		then ( return 80 )
	 * 			| if(getCurrentWeapon().equals("Rifle")
	 * 			|		then ( return 20 )
	 * 			| else
	 * 			|		(return 0)
	 */
	@Model
	private int getDamageOfWeapon(){
		if(this.getCurrentWeapon().equals("Bazooka"))
			return 80;
		else if(this.getCurrentWeapon().equals("Rifle"))
			return 20;
		else
			return 0;
	}
	
	/**
	 * sets the current weapon to the given weapon if and only if the given weapon is
	 * a valid weapon.
	 * 
	 * @param 	weapon
	 * 			The given weapon
	 * @post	the new weapon is the given weapon if and only if the given weapon is a valid weapon.
	 * 			|if(isValidWeapon(weapon)
	 * 			|	then (new.getCurrentWeapon() == weapon
	 */
	@Raw @Model
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
