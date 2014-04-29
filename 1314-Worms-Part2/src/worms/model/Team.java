package worms.model;

import java.util.HashSet;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of teams...
 * 
 * @invar	| hasProperWorms()
 * @invar	| hasProperWorld()
 * @invar	| canHaveAsTeamName(getTeamName())
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
//TODO nakijken
public class Team {

	/**
	 * Create a new team with the given team name
	 * as its team name.
	 * 
	 * @param 	teamName
	 * 			The team name for this new team.
	 * @post	| new.getTeamName() == teamName
	 * @throws 	IllegalNameException(teamName)
	 * 			| !canHaveAsTeamName(teamName)
	 */
	public Team(String teamName) 
			throws IllegalNameException{
		if(!canHaveAsTeamName(teamName))
			throw new IllegalNameException(teamName);
		this.teamName = teamName;
	}
	
	
	
	/**
	 * Returns whether or not the team is active.
	 */
	@Basic @Raw
	public boolean isActive(){
		return this.isActive;
	}
	
	/**
	 * Deactivate this team.
	 * 
	 * @post	| ( !new.isActive() )
	 * @effect	| world.removeAsTeam(this)
	 * @throws	| teamWorms.size() > 0
	 */
	public void deactivate(){
		if(teamWorms.size() > 0)
			throw new RuntimeException("You can't deactivate this team right now.");
		world.removeAsTeam(this);
		this.isActive = false;
	}
	
	/**
	 * Variable registering whether or not this team is active.
	 */
	private boolean isActive = true;
	
	
	
	/**
	 * Return the world to which this team is attached.
	 */
	@Basic @Raw
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Check whether this team can be attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to check.
	 * @return	| result == ( (world == null) || 
	 * 			|				(world.canHaveAsTeam(this) )
	 */
	@Raw
	public boolean canHaveAsWorld(World world){
		return world == null || world.canHaveAsTeam(this);
	}
	
	/**
	 * Check whether this team has a proper world to
	 * which it is attached.
	 * 
	 * @return	| result == ( canHaveAsWorld(getWorld()) &&
	 * 			|				( (getWorld() == null) ||
	 * 			| 					getWorld().hasAsTeam(this)))
	 */
	@Raw
	public boolean hasProperWorld(){
		return canHaveAsWorld(getWorld()) && 
				(getWorld() == null || getWorld().hasAsTeam(this));
	}
	
	/**
	 * Set the world to which this team is attached to the given world.
	 * 
	 * @param 	world
	 * 			The world to attach this team to.
	 * @pre		| if(world != null)
	 * 			|	then world.hasAsTeam(this)
	 * @pre		| if( (world == null) && (getWorld() != null) )
	 * 			| 	then !getWorld().hasAsTeam(this)
	 * @post	| new.getWorld() == world
	 */
	@Raw
	void setWorld(@Raw World world){
		assert(world == null || world.hasAsTeam(this));
		assert(world != null || getWorld() == null || !getWorld().hasAsTeam(this));
		this.world = world;
	}
	
	/**
	 * Variable referencing the world to which this team is attached.
	 */
	private World world = null;
	
	/**
	 * Check whether or not this team can have the given worm 
	 * as one of its worms or not.
	 * 
	 * @param 	worm
	 * 			The worm to check.
	 * @return	| worm != null && worm.isAlive() && this.isActive()
	 */
	public boolean canHaveAsTeamWorm(Worm worm){
		return worm != null && worm.isAlive() && this.isActive();
	}
	
	/**
	 * Check whether this team has proper worms attached to it.
	 * 
	 * @return	| result ==
	 * 			| 	for each worm in Worm
	 * 			|		( if (this.hasAsTeamWorm(worm))
	 * 			|			then (canHaveAsTeamWorm(worm)
	 * 			|				&& (worm.getTeam() == this)) )
	 */
	@Raw
	public boolean hasProperWorms(){
		if(this.isActive() && this.getWorld() != null && this.getWorld().isStarted())
			if(teamWorms.size() == 0)
				return false;
		for(Worm worm: teamWorms){
			if(!canHaveAsTeamWorm(worm) || worm.getTeam() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * Check whether this team has the given worm as one of its
	 * worms attached to it.
	 * 
	 * @param 	worm
	 * 			The worm to check.
	 * @return	| result == this.teamWorms.contains(worm)
	 */
	public boolean hasAsTeamWorm(Worm worm){
		return this.teamWorms.contains(worm);
	}
	
	/**
	 * Return the size of the current team of worms.
	 * 
	 * @return	| this.teamWorms.size()
	 */
	public int getSizeOfTeam(){
		return this.teamWorms.size();
	}
	
	/**
	 * Add the given worm to the set of worms attached to this team.
	 * 
	 * @param 	worm	
	 * 			the worm to be added
	 * @post	This team has the given worm as one of its worms.
	 * 			| new.hasAsTeamWorm(worm)
	 * @post 	The given worm references this team as the team it is attached to.
	 * 			| (new.worm).getTeam() == this
	 * @throws	IllegalArgumentException()
	 * 			this team cannot have the given worm as one of its worms.
	 * 			| !canHaveAsTeamWorm(worm)
	 * @throws	IllegalArgumentException()
	 * 			The given worm is already attached to a team.
	 * 			| ( (worm != null) 
	 * 			|  && (worm.getTeam() != null) )
	 */
	public void addAsTeamWorm(Worm worm)
		throws IllegalArgumentException
	{
		if(! canHaveAsTeamWorm(worm))
			throw new IllegalArgumentException();
		if(worm.getTeam() != null)
			throw new IllegalArgumentException();
		this.teamWorms.add(worm);
		worm.setTeam(this);
	}
	
	/**
	 * Remove the given worm from the set of worms attached to this team.
	 * 
	 * @param 	worm
	 * 			The worm to be removed.
	 * @post	This team does not have the given worm as one of its worms.
	 * 			| !new.hasAsTeamWorm(worm)
	 * @post 	If this team has the given worm as one of its worms,
	 * 			the given worm is no longer attached to any team.
	 * 			|if (hasAsTeamWorm(worm))
	 * 			|	((new worm).getTeam() == null
	 * @throws	IllegalArgumentException()
	 * 			| !hasAsTeamWorm(worm)
	 */
	public void removeAsTeamWorm(Worm worm) 
			throws IllegalArgumentException{
		if(!hasAsTeamWorm(worm))
			throw new IllegalArgumentException();
		this.teamWorms.remove(worm);
		if(teamWorms.size() == 0)
			this.deactivate();
		worm.setTeam(null);
	}
	
	/**
	 * Set collecting references to worms attached to the team.
	 * 
	 * @invar	| teamWorms != null
	 * @invar	| for each teamWorm in teamWorms(
	 * 			|	canHaveAsTeamWorm(teamWorm))
	 * @invar	| for each teamWorm in teamWorms(
	 * 			|	teamWorm.getTeam() == this)
	 * @invar	| if(this.isActive())
	 * 			|	then( if(this.getWorld() != null && this.getWorld().isStarted())
	 * 			|		then( teamWorms.size() > 0 ))
	 */
	private HashSet<Worm> teamWorms = new HashSet<Worm>();
	
	
	
	/**
	 * Returns the team name of the team.
	 */
	@Basic @Raw
	public String getTeamName(){
		return this.teamName;
	}
	
	/**
	 * Returns whether or not the given name is a valid name for a team.
	 * 
	 * @param 	name
	 * 			the name to check
	 * @return	| result == ( name.length()>1 && name.substring(0,1).matches("[A-Z]+") && 
	 *			|	name.matches("[A-Za-z]+")
	 */
	private boolean canHaveAsTeamName(String name){
		 return name.length()>1 && name.substring(0,1).matches("[A-Z]+") && 
				 name.matches("[A-Za-z]+");
	}
	
	/**
	 * Variable referencing the name of the team.
	 */
	private final String teamName;
}
