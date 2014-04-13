//https://github.com/WormsRepository/WormsOfPieterAndLaurens
// Laurens Loots 	Informatica
// Pieter Vos		Informatica

package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of worms involving an x-coordinate, an y-coordinate, a direction in radians, a radius (in meter),
 * a minimum radius, a name, a mass (in kilogram), current amount of action points,
 * maximum amount of action points, a density, the standard acceleration and a world.
 *
 * @invar	The direction of each worm must be a valid direction for any worm.
 * 			| isValidDirection(getDirection())
 * @invar	The radius of each worm must be a valid radius for any worm.
 * 			| canHaveAsRadius(getRadius())
 * @invar	The name of each worm must be a valid name for any worm.
 * 			| canHaveAsName(getName())
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
//TODO world invariants? isValidWorld?
public class Worm {


	/**
	 * Create a new worm that is positioned at the given position,
	 * looks in the given direction, has the given radius, the given name,
	 * the right mass and the right amount of maximum action points.
	 * 
	 * @param	x
	 * 			The x-coordinate of the position of the new worm (in meter).
	 * @param 	y
	 * 			The y-coordinate of the position of the new worm (in meter).
	 * @param 	direction
	 * 			The direction of the new worm (in radians).
	 * @param 	radius 
	 * 			The radius of the new worm (in meter).
	 * @param 	name
	 * 			The name of the new worm
	 * @effect	The position of this new worm is initialized with this worm, 
	 * 			the x coordinate and the y coordinate as arguments.
	 * 			| new.position == new Position(this,x,y)
	 * @effect	The direction is set to the given value.
	 * 			| this.setDirection(direction)
	 * @effect	The radius of this new worm, the mass of this new worm, the maximum and current amount of action points of this new worm
	 * 			is set, some of them depend on the radius of this new worm. There is only a mass, a maximum and a current
	 * 			amount of action points if the radius is a valid radius for any worm.
	 * 			| this.setRadius(radius)
	 * 			| this.setCurrentActionPoints(new.getMaxActionPoints())
	 * @effect	The name of this new worm is set to the given name.
	 * 			| this.setName(name)
	 */
	public Worm(double x, double y, double direction, double radius,String name) 
			throws IllegalRadiusException, IllegalNameException
	{
		position = new Position(this,x,y);
		setDirection(direction);
		setRadius(radius);
		setCurrentActionPoints(getMaxActionPoints());
		setName(name);
	}

	/**
	 * Return the world to which this worm is attached.
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	//TODO setter en checkers voor world...
	
	/**
	 * Variable referencing the world to which this worm is attached.
	 */
	private World world = null;

	/**
	 * Return the x-coordinate of the position of the worm (in meter).
	 */
	@Basic @Raw
	public double getX() {
		return this.position.getX();
	}

	/**
	 * Set the x-coordinate for this worm to the given x-coordinate.
	 * 
	 * @param 	x
	 * 			The new x-coordinate for this worm (in meter).
	 * @effect	The x position of this worm is set to the given coordinate.
	 * 			|this.position.setX(x)
	 */
	@Model
	private void setX(double x)
	{
		this.position.setX(x);
	}

	/**
	 * Return the y-coordinate of the position of the worm (in meter).
	 */
	@Basic @Raw
	public double getY() {
		return this.position.getY();
	}

	/**
	 * Set the y-coordinate for this worm to the given y-coordinate.
	 * 
	 * @param 	y
	 * 			The new y-coordinate for this worm (in meter).
	 * @effect	The y position of this worm is set to the given coordinate.
	 * 			|this.position.setY(y)
	 */
	@Model
	private void setY(double y)
	{
		this.position.setY(y);
	}

	/**
	 * Check whether the given number of steps is a valid amount of steps for the worm.
	 * 
	 * @param 	nbSteps
	 * 			The amount of steps to check.
	 * @return	True if and only if the amount of steps is larger than zero 
	 * 			and there are enough action points available for the move.
	 * 			| result == this.position.canMove(nbSteps)
	 */
	@Raw
	public boolean canMove(int nbSteps) 
	{
		return this.position.canMove(nbSteps);
	}

	/**
	 * Return the position of the worm at a given time in a jump.
	 * 
	 * @param 	t
	 * 			The time to check the position of the worm.
	 * @return	The position of the worm at the given time in the jump.
	 * 			| result == this.position.getJumpStep(t)
	 */
	public double[] getJumpStep(double t) 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		return this.position.getJumpStep(t);
	}

	/**
	 * Calculate the jump time from a jump in the current direction with the number of remaining action points.
	 * 
	 * @return	The time needed for a jump in the current state of the worm.
	 * 			| result == this.position.getJumpTime()
	 */
	public double getJumpTime() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		return this.position.getJumpTime();
	}

	/**
	 * Change the position of the worm as the result of a jump from the current position 
	 * and with respect to the worm's orientation, his mass, the standard acceleration 
	 * and the number of remaining action points.
	 * 
	 * @effect	A help method is executed.
	 * 			| this.position.jump()
	 */
	public void jump() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		this.position.jump();
	}

	/**
	 * Moves the worm in the current direction with the given number of steps.
	 * 
	 * @param 	nbSteps
	 * 			The number of steps to move.
	 * @effect	A help method is executed.
	 * 			| this.position.move(nbSteps)
	 */
	public void move(int nbSteps) 
			throws IllegalArgumentException
	{
		this.position.move(nbSteps);
	}
	
	/**
	 * Variable registering the position of the worm.
	 */
	private final Position position;

	
	
	

	/**
	 * Return the radius of this worm.
	 * 	The radius expresses how big a worm actually is.
	 */
	@Basic @Raw
	public double getRadius() 
	{
		return this.radius;
	}

	/**
	 * Set the radius of this worm to the given radius.
	 * 
	 * @param 	radius
	 * 			The new radius for this worm.
	 * @post	The new radius of this worm is equal to the given radius.
	 * 			| new.getRadius() == radius
	 * @effect	The mass of this new worm and the maximum amount of action points of this worm
	 * 			is set, it depends on the new radius of this worm. There is only a mass and a maximum
	 * 			amount of action points if the radius is a valid radius for any worm.
	 * 			| this.setMass()
	 * 			| this.setMaxActionPoints()
	 * @throws	IllegalRadiusException(radius,this)
	 * 			The given radius is not a valid radius for this worm.
	 * 			| !canHaveAsRadius(radius)
	 */
	@Raw
	public void setRadius(double radius) 
			throws IllegalRadiusException
	{
		if(! canHaveAsRadius(radius))
			throw new IllegalRadiusException(radius,this);
		this.radius = radius;
		setMass();
		setMaxActionPoints();
	}

	/**
	 * 	Variable registering the radius of a worm.
	 */
	private double radius = 0;





	/**
	 * Check whether the given radius is a valid radius for this worm.
	 * 
	 * @param 	radius
	 * 			The radius to check.
	 * @return	True if and only if the given radius is not below the minimum radius.
	 * 			| radius >= getMinimalRadius()
	 */
	@Raw
	public boolean canHaveAsRadius(double radius)
	{
		return radius >= getMinimalRadius();
	}

	/**
	 * Return the variable minRadius of this worm.
	 * 	The variable minRadius expresses the minimum radius the worm has.
	 */
	@Basic @Raw
	public double getMinimalRadius() 
	{
		return this.minRadius;
	}

	/**
	 * Set the minimal radius to the given minimal radius.
	 * 
	 * @param 	minRadius
	 * 			The new minimum radius of this worm.
	 * @post	The new minimum radius of this worm is equal to the given minimum radius.
	 * 			| new.getMinimalRadius() == minRadius
	 * @throws 	IllegalRadiusException(minRadius,this)
	 * 			The given minimum radius is not a valid radius for any worm.
	 * 			| (minRadius <= 0)
	 */
	@Raw
	public void setMinimalRadius(double minRadius) 
			throws IllegalRadiusException
	{
		if(minRadius <= 0)
			throw new IllegalRadiusException(minRadius, this);
		this.minRadius = minRadius;
	}

	/**
	 * 	Variable registering the minimal radius of a worm.
	 */
	private double minRadius = 0.25;




	/**
	 * Return the direction of this worm.
	 * 	The direction of a worm expresses the orientation the worm has.
	 */
	@Basic @Raw
	public double getDirection() 
	{
		return this.direction;
	}

	/**
	 * Check whether the given direction is a valid direction for any worm.
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
	 * Checks whether a worm can turn with the given angle.
	 * 
	 * @param 	angle
	 * 			The angle to check.
	 * @return 	False if the absolute value of the given angle is bigger than pi or zero.
	 * 			| if (Math.abs(angle) > Math.PI || angle == 0)
	 * 			|	then result == false
	 * 			Otherwise, true if and only if the amount of action points needed for such a turn
	 * 			is smaller than the current amount of action points.
	 * 			| else result == (getCurrentActionPoints() >= (int)(Math.ceil(Math.abs(angle) / (2*Math.PI) * 60)))
	 */
	@Raw
	public boolean canTurn(double angle) {
		if(Math.abs(angle) > Math.PI || angle == 0) {
			return false;
		}
		return getCurrentActionPoints() >= (int)(Math.ceil(Math.abs(angle) / (2*Math.PI) * 60));
	}

	/**
	 * Changes the direction with the given angle.
	 * 
	 * @param 	angle
	 * 			The angle to turn.
	 * @pre		The worm must be able to turn with the given angle.
	 * 		    | canTurn(angle)
	 * @effect	The new direction is equal to the old direction incremented with the given angle
	 * 			and possibly incremented or decremented with two pi.
	 * 			The new amount of current action points is equal to the old amount 
	 * 			decremented with the used action points
	 * 			| this.setCurrentActionPoints(
	 * 			|			getCurrentActionPoints - (int)(Math.ceil(Math.abs(angle) / (2*Math.PI) * 60)))
	 * 			| if ((this.getDirection() + angle) > 2*Math.PI)
	 * 			|	then (this.setDirection(getDirection() + angle - 2*Math.PI))
	 * 			| else if ((this.getDirection() + angle) < 0)
	 * 			|	then (this.setDirection(getDirection() + angle + 2*Math.PI))
	 * 			| else
	 * 			| 	then (this.setDirection(getDirectin() + angle)	
	 */
	public void turn(double angle) {
		assert(canTurn(angle)):
			"Precondition: Acceptable angle to turn";
		double orientation = getDirection() + angle;
		if(!isValidDirection(orientation))
		{
			if(orientation > 2*Math.PI)
				orientation -= 2*Math.PI;
			if(orientation < 0)	
				orientation += 2*Math.PI;
		}
		setDirection(orientation);
		setCurrentActionPoints(getCurrentActionPoints() - (int)(Math.ceil(Math.abs(angle) / (2*Math.PI) * 60)));
	}

	/**
	 * Set the direction of this worm to the given direction.
	 * 
	 * @param 	direction
	 * 			The new direction for this worm.
	 * @pre		The given direction must be a valid direction for any worm.
	 * 		    | isValidDirection(direction)
	 * @post	The new direction of this worm is equal to the given direction.
	 * 			| new.getDirection() == direction
	 */
	@Model @Raw
	private void setDirection(double direction)
	{
		assert(isValidDirection(direction)):
			"Precondition: Acceptable direction to set";
		this.direction = direction;
	}

	/**
	 * variable registering the direction of a worm.
	 */
	private double direction = 0;





	/**
	 * Return the mass of this worm.
	 * 	The Mass of a worm expresses how heavy a worm is.
	 */
	@Basic @Raw
	public double getMass() 
	{
		return this.mass;
	}





	/**
	 * Set the mass of the worm according to the radius.
	 * 
	 * @post	The new mass is calculated with the radius and the density.
	 * 			| new.mass = DENSITY * ((4/3) * Math.PI * Math.pow(getRadius(),3))
	 * @throws	IllegalRadiusException(getRadius(),this)
	 * 			The given radius is not a valid radius for this worm.
	 * 			| !canHaveAsRadius(getRadius())
	 */
	@Raw
	private void setMass() 
			throws IllegalRadiusException
	{
		if(! canHaveAsRadius(getRadius()))
			throw new IllegalRadiusException(getRadius(), this);
		mass = DENSITY * ((4/3) * Math.PI * Math.pow(getRadius(),3));
	}

	/**
	 * Variable registering the mass of a worm.
	 */
	private double mass = 0;



	/**
	 * Final class variable registering the density of all worms.
	 */
	private final static double DENSITY = 1062;




	/**
	 * Returns the variable maxActionPoints.
	 * 		The maximum amount of action points a worm has is represented by the variable maxActionPoints.
	 */
	@Basic @Raw
	public long getMaxActionPoints()
	{
		return this.maxActionPoints;
	}

	/**
	 * Set the max action points of this worm according to the mass, 
	 * if needed change the current amount of action points.
	 * 
	 * @post 	the new maximum action points is the mass rounded to the nearest integer.
	 * 			| new.maxActionPoints = Math.round(getMass())
	 * 			If the current amount of action points is bigger than the maximum amount of action points
	 * 			the current amount of action points is set to the maximum amount of action points.
	 * 			| if(getCurrentActionPoints() > getMaxActionPoints())
	 * 			|	then(new.getCurrentActionPoints() == new.getMaxActionPoints())
	 */
	@Raw @Model
	private void setMaxActionPoints()
	{
		maxActionPoints = Math.round(getMass());
		if(getCurrentActionPoints() > getMaxActionPoints())
			setCurrentActionPoints(getMaxActionPoints());
	}

	/**
	 * variable registering the maximum of action points of a worm, derived from the worm's mass.
	 */
	private long maxActionPoints = 0;




	/**
	 * Return the current amount of action points of this worm.
	 */
	@Basic @Raw
	public long getCurrentActionPoints() {
		return this.currentActionPoints;
	}

	/**
	 * Set the amount of current action points of this worm to the given amount.
	 * 
	 * @param 	newActionPoints
	 * 			The new amount of current action points for this worm.
	 * @post	If the new amount of current action points is below zero or greater than
	 * 			the maximum amount of action points, nothing happens.
	 * 			| if(newActionPoints < 0 || newActionPoints > getMaxActionPoints())
	 * 			|	then new.getCurrentActionPoints() == getCurrentActionPoints()
	 * 			Else the new amount of current action points is equal to the given amount.
	 * 			| else (new.getCurrentActionPoints() == newActionPoints)
	 */
	@Model @Raw
	protected void setCurrentActionPoints(long newActionPoints){
		if(newActionPoints < 0 || newActionPoints > getMaxActionPoints())
			return;
		this.currentActionPoints = newActionPoints;
	}

	/**
	 * variable registering the current amount of aciton points of a worm.
	 */
	private long currentActionPoints = 0;




	/**
	 * Check whether the given name is a valid name.
	 * 
	 * @param 	name
	 * 			The name to check.
	 * @return	True if and only if the given name is at least two characters long,
	 * 			starts with an uppercase letter and only uses letters, quates and spaces.
	 * 			| name.length()>1 && name.substring(0,1).matches("[A-Z]+") && name.matches("[A-Za-z '\"]+")
	 */
	@Raw
	public boolean canHaveAsName(String name)
	{
		return name.length()>1 && name.substring(0,1).matches("[A-Z]+") && name.matches("[A-Za-z '\"]+");
	}

	/**
	 * Return the name of this worm.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of this worm to the given name.
	 * 
	 * @param	name
	 * 			The new name for this worm.
	 * @post	The new name of this worm is equal to the given name.
	 * 			| new.getName() == name
	 * @throws	IllegalNameException(name,this)
	 * 			This new worm cannot have the given name as its name.
	 * 			| !canHaveAsName(name)
	 */
	@Raw
	public void setName(String name) 
			throws IllegalNameException
	{
		if(! canHaveAsName(name))
			throw new IllegalNameException(name,this);
		this.name = name;
	}

	private String name = " ";
}
