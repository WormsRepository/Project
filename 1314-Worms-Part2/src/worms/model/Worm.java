//https://github.com/WormsRepository/Project/
// Laurens Loots 	Informatica
// Pieter Vos		Informatica


package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of worms involving a position, a direction (in radians), 
 * a radius (in meter), a minimum radius, a name, a teamname, a mass (in kilogram), 
 * current amount of action points, maximum amount of action points, 
 * current amount of hit points, maximum amount of hit points, a density, 
 * the standard acceleration, a weapon and a world.
 *
 * @invar	The direction of each worm must be a valid direction for any worm.
 * 			| isValidDirection(getDirection())
 * @invar	The radius of each worm must be a valid radius for any worm.
 * 			| canHaveAsRadius(getRadius())
 * @invar	The name of each worm must be a valid name for any worm.
 * 			| canHaveAsName(getName())
 * @invar	The position of each worm must lie fully within the map.
 * 			| getPosition().inMap(getPosition().getX(),getPosition().getY())
 * @invar	The World to which the worm is attached must be a valid world
 * 			| hasProperWorld()
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public class Worm {


	/**
	 * Create a new worm that is positioned at the given position,
	 * looks in the given direction, has the given radius, the given name,
	 * the right mass and the right amount of action points, maximum action
	 * points, hit points and maximum hit points.
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
	 * 			amount of action points and hit points if the radius is a valid radius for any worm.
	 * 			| this.setRadius(radius)
	 * 			| this.setCurrentActionPoints(new.getMaxActionPoints())
	 * 			| this.setCurrentHitPoints(new.getMaxHitPoints())
	 * @effect	The name of this new worm is set to the given name.
	 * 			| this.setName(name)
	 */
	public Worm(double x, double y, double direction, double radius,String name) 
			throws IllegalRadiusException, IllegalNameException
	{
		wormPosition = new WormPosition(this,x,y);
		setDirection(direction);
		setRadius(radius);
		setCurrentActionPoints(getMaxActionPoints());
		setCurrentHitPoints(getMaxHitPoints());
		setName(name);
	}

	/**
	 * Return the world to which this worm is attached.
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	
	/**
	 * Check whether this worm can be attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to check.
	 * @return	| result == ( (world == null) || 
	 * 			|				(world.canHaveAsWorm(this) )
	 */
	@Raw
	public boolean canHaveAsWorld(World world){
		return world == null || world.canHaveAsWorm(this);
	}
	
	/**
	 * Check whether this worm has a proper world to
	 * which it is attached.
	 * 
	 * @return	| result == ( canHaveAsWorld(getWorld()) &&
	 * 			|				( (getWorld() == null) ||
	 * 			| 					getWorld().hasAsWorm(this)))
	 */
	@Raw
	public boolean hasProperWorld(){
		return canHaveAsWorld(getWorld()) && 
				(getWorld() == null || getWorld().hasAsWorm(this));
	}
	
	/**
	 * Set the world to which this food is attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to attach this food to.
	 * @pre		| if(world != null)
	 * 			|	then world.hasAsWorm(this)
	 * @pre		| if( (world == null) && (getWorld() != null) )
	 * 			| 	then !getWorld().hasAsWorm(this)
	 * @post	| new.getWorld() == world
	 */
	@Raw
	public void setWorld(@Raw World world){
		assert(world == null || world.hasAsWorm(this));
		assert(world != null || getWorld() == null || !getWorld().hasAsWorm(this));
		this.world = world;
	}
	
	
	
	/**
	 * Variable referencing the world to which this worm is attached.
	 */
	private World world = null;
	
	
	
	/**
	 * Returns the reference to the position of this worm.
	 */
	@Basic @Raw
	public WormPosition getWormPosition()
	{
		return this.wormPosition;
	}
	
	/**
	 * Variable referencing the position of the worm.
	 */
	private final WormPosition wormPosition;

	
	
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
	 * @effect	The mass of this new worm and the maximum amount of action points and hit points of 
	 * 			this worm is set, it depends on the new radius of this worm. There is only a mass 
	 * 			and a maximum amount of action points  and hit points if the radius is a valid 
	 * 			radius for any worm.
	 * 			| this.setMass()
	 * 			| this.setMaxActionPoints()
	 * 			| this.setMaxHitPoints()
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
		setMaxHitPoints();
	}
	
	/**
	 * @effect	The Radius is increased with 10 percent.
	 * 			| setRadius(this.getRadius() * 1.1)
	 */
	@Raw
	public void growInRadius() 
			throws IllegalRadiusException{
		setRadius(this.getRadius() * 1.1);
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
	 * @return	True if and only if the given radius is not below the minimum radius
	 * 			and the worm still lies fully within his world.
	 * 			| if(this.getWorld() == null)
	 * 			| 	then result == radius >= getMinimalRadius()
	 * 			| else
	 * 			|	then result == this.getPosition().getX()>radius && 
	 * 			|		this.getPosition().getX()<this.getWorld().getWidth() - radius &&
	 * 			|			this.getPosition().getY()>radius && 
	 * 			|				this.getPosition().getY()<this.getWorld().getHeight() - radius 
	 * 			|					&& radius >= getMinimalRadius();
	 */
	@Raw
	public boolean canHaveAsRadius(double radius)
	{
		if(this.getWorld() == null)
			return radius >= getMinimalRadius();
		double x = this.getWormPosition().getX();
		double y = this.getWormPosition().getY();
		return x>radius && x<this.getWorld().getWidth() - radius &&
		y>radius && y<this.getWorld().getHeight() - radius 
		&& radius >= getMinimalRadius();
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
	 * Variable registering the direction of a worm.
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
	 * 			| new.mass = DENSITY * ((4.0/3.0) * Math.PI * Math.pow(getRadius(),3.0))
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
		mass = DENSITY * ((4.0/3.0) * Math.PI * Math.pow(getRadius(),3.0));
	}

	/**
	 * Variable registering the mass of a worm (in kilograms).
	 */
	private double mass = 0;



	/**
	 * Final class variable registering the density of all worms.
	 */
	private final static double DENSITY = 1062;




	/**
	 * Returns the maximum amount of action points of the worm.
	 */
	@Basic @Raw
	public int getMaxActionPoints()
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
		maxActionPoints = (int)Math.round(getMass());
		if(getCurrentActionPoints() > getMaxActionPoints())
			setCurrentActionPoints(getMaxActionPoints());
	}

	/**
	 * Variable registering the maximum of action points of a worm, derived from the worm's mass.
	 */
	private int maxActionPoints = 0;




	/**
	 * Return the current amount of action points of this worm.
	 */
	@Basic @Raw
	public int getCurrentActionPoints() {
		return this.currentActionPoints;
	}

	/**
	 * Decrements the value of the current amount of action points with
	 * the given value.
	 * 
	 * @param 	valueToReduce
	 * 			The value in order to reduce.
	 * @effect	| setCurrentActionPoints(getCurrentActionPoints() - valueToReduce)
	 */
	@Raw
	public void reduceCurrentActionPoints(int valueToReduce){
		setCurrentActionPoints(getCurrentActionPoints() - valueToReduce);
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
	 * @effect	If the new amount of current action points is zero and the world of this world
	 * 			doesn't equals null, the worm's turn ends.
	 * 			| if(newActionPoints == 0 && this.getWorld() != null)
	 * 			| 	then this.getWorld().startNextTurn()
	 */
	@Model @Raw
	protected void setCurrentActionPoints(int newActionPoints){
		if(newActionPoints < 0 || newActionPoints > getMaxActionPoints())
			return;
		if(newActionPoints == 0 && this.getWorld() != null)
			this.getWorld().startNextTurn();
		this.currentActionPoints = newActionPoints;
	}

	/**
	 * Variable registering the current amount of action points of a worm.
	 */
	private int currentActionPoints = 0;




	/**
	 * Check whether the given name is a valid name.
	 * 
	 * @param 	name
	 * 			The name to check.
	 * @return	True if and only if the given name is at least two characters long,
	 * 			starts with an uppercase letter and only uses letters, quates, spaces and digits.
	 * 			| result == name.length()>1 && name.substring(0,1).matches("[A-Z]+") && 
	 * 			| 				name.matches("[A-Za-z0-9 '\"]+")
	 */
	@Raw
	public boolean canHaveAsName(String name)
	{
		return name.length()>1 && name.substring(0,1).matches("[A-Z]+") && name.matches("[A-Za-z0-9 '\"]+");
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

	/**
	 * A variable referencing the name of a worm.
	 */
	private String name = " ";
	

	
	/**
	 * Returns the reference to the weapon of this worm.
	 */
	@Basic @Raw
	public Weapon getWeapon(){
		return this.weapon;
	}
	
	/**
	 * Variable referencing the weapon of the worm.
	 */
	private final Weapon weapon = new Weapon(this);
	
	
	
	/**
	 * Returns the current number of hit points of the given worm.
	 */
	@Basic @Raw
	public int getCurrentHitPoints()
	{
		return this.currentHitPoints;
	}
	
	/**
	 * Decrements the value of the current amount of hit points with
	 * the given value.
	 * 
	 * @param 	valueToReduce
	 * 			The value in order to reduce.
	 * @effect	| setCurrentHitPoints(getCurrentHitPoints() - valueToReduce)
	 */
	@Raw
	public void reduceCurrentHitPoints(int valueToReduce){
		setCurrentHitPoints(getCurrentHitPoints() - valueToReduce);
	}
	
	/**
	 * Set the amount of current action points of this worm to the given amount.
	 * 
	 * @param 	newHitPoints
	 * 			The new amount of current hit points for this worm.
	 * @post	If the new amount of current hit points is smaller than zero or equals 
	 * 			zero, the current amount of hit points is set to zero.
	 * 			| if(newHitPoints <= 0)
	 * 			|	then (new.getCurrentHitPoints() == 0)
	 * 			If the new amount of current hit points is larger than the maximum
	 * 			amount of hit points, nothing happens.
	 * 			| if(newHitPoints > getMaxHitPoints())
	 * 			|	then (new.getCurrentHitPoints() == getCurrentHitPoints())
	 * 			Else the new amount of current hit points is equal to the given amount.
	 * 			| else (new.getCurrentHitPoints() == newHitPoints)
	 * @effect	If the new amount of current hit points is smaller than zero or equals 
	 * 			zero, the worm dies.
	 * 			| this.wormDeath()
	 */
	@Model @Raw
	protected void setCurrentHitPoints(int newHitPoints){
		if(newHitPoints <= 0)
			{
			this.currentHitPoints = 0;
			this.wormDeath();
			}
		
		if(newHitPoints > getMaxHitPoints())
			return;
		this.currentHitPoints = newHitPoints;
	}
	
	
	/**
	 * Variable registering the current amount of hit points of a worm.
	 */
	private int currentHitPoints = 0;
	
	
	
	/**
	 * Return the maximum amount of hit points of a worm.
	 */
	@Basic @Raw
	public int getMaxHitPoints()
	{
		return this.maxHitPoints;
	}
	
	/**
	 * Set the max hit points of the worm according to the mass, 
	 * if needed change the current amount of hit points.
	 * 
	 * @post 	The new maximum hit points is the mass rounded to the nearest integer.
	 * 			| new.maxHitPoints = Math.round(getMass())
	 * 			If the current amount of hit points is bigger than the maximum amount of hit points
	 * 			the current amount of hit points is set to the maximum amount of hit points.
	 * 			| if(getCurrentHitPoints() > getMaxHitPoints())
	 * 			|	then(new.getCurrentHitPoints() == new.getMaxHitPoints())
	 */
	@Raw @Model
	private void setMaxHitPoints()
	{
		maxHitPoints = (int)Math.round(getMass());
		if(getCurrentHitPoints() > getMaxHitPoints())
			setCurrentHitPoints(getMaxHitPoints());
	}
	
	/**
	 * Variable registering the maximum amount of hit points.
	 */
	private int maxHitPoints = 0;
	
	
	
	
	/**
	 * A variable referencing the team name of a worm.
	 */
	
	
	/**
	 * Check whether this worm can be attached to the given team.
	 * 
	 * @param 	team
	 * 			The team to check.
	 * @return	True if and only if the team equals null or if
	 * 			the given team can have this worm as one of its worms.
	 * 			| result == ((team == null) || 
	 * 			|				team.canHaveAsTeamWorm(this))
	 */
	@Raw
	public boolean canHaveAsTeam(Team team){
		return team == null || team.canHaveAsTeamWorm(this);
	}
	
	/**
	 * Check whether this worm has a proper team 
	 * to which it is attached.
	 * 
	 * @return	True if and only if this worm can have the team that 
	 * 			is attached to this worm as its team and if
	 * 			the team that is attached to this worm equals null
	 * 			or that team has this worm as one of its worms.
	 * 			| result == canHaveAsTeam(getTeam()) && 
	 * 			|		(getTeam() == null || getTeam().hasAsTeamWorm(this))
	 */
	@Raw
	public boolean hasProperTeam(){
		return canHaveAsTeam(getTeam()) &&
				(getTeam() == null || getTeam().hasAsTeamWorm(this));
	}
	
	/**
	 * Return the team to which this worm is attached.
	 */
	@Basic @Raw
	public Team getTeam(){
		return this.team;
	}
	
	/**
	 * Set the team to which this worm is attached to the given team.
	 * 
	 * @param 	team
	 * 			The team to attach this worm to.
	 * @pre		If the given team doesn't equal null, then the given
	 * 			team will have this worm as one of its worms.
	 * 			| if(team != null)
	 * 			|	then team.hasAsTeamWorm(this)
	 * @pre		If the given team equals null and the old team of
	 * 			this worm doesn't equal null then the given team
	 * 			will not have this worm as one of its worms.
	 * 			| if( (team == null) && (getTeam() != null) )
	 * 			| 	then !getTeam().hasAsTeamWorm(this)
	 * @post	The new team of this worm will be equal to the given team.
	 * 			| new.getTeam() == team
	 */
	@Raw
	void setTeam(@Raw Team team){
		assert(team == null || team.hasAsTeamWorm(this));
		assert(team != null || getTeam() == null || !getTeam().hasAsTeamWorm(this));
		this.team = team;
	}
	
	/**
	 * Variable referencing the team to which this worm is attached.
	 */
	private Team team = null;

	
	
	/**
	 * Returns if the worm is alive or not.
	 */
	public boolean isAlive() {
		return this.isAlive;
	}
	
	/**
	 * The worm dies. This means that his turn ends, that he will be removed
	 * as a worm in his world and that he is no longer alive.
	 * 
	 * @post	The worm is no longer alive.
	 * 			| new.isAlive() = false
	 * @effect	If the team of the worm doesn't equal null, this worm will
	 * 			no longer be part of that team.
	 * 			| if(this.getTeam() != null)
	 * 			|	then( this.getTeam().removeAsTeamWorm(this) )
	 * @effect	The turn of the worm ends if it was his turn.
	 * 			| if(this == this.getWorld().getCurrentWorm())
	 * 			| 	then( this.getWorld().startNextTurn() )
	 * @effect	The worm is removed from his world.
	 * 			| this.getworld().removeAsWorm(this)
	 */
	public void wormDeath(){
		if(this.getTeam() != null)
			this.getTeam().removeAsTeamWorm(this);
		if(this == this.getWorld().getCurrentWorm())
			this.getWorld().startNextTurn();
		this.getWorld().removeAsWorm(this);
		this.isAlive = false;
	}
	
	/**
	 * A boolean indicating whether or not the worm is alive.
	 */
	private boolean isAlive = true;
}

