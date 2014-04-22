package worms.model;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * A class collecting tests for the class worm.
 * 
 * @version 1.0
 * @author Pieter en Laurens
 */

public class WormTest {

	/**
	 * Variable referencing a worm with radius 1.
	 */
	private static Worm wormRadius1;

	/**
	 * Variable referencing a worm with direction 4.
	 */
	private static Worm wormDirection4;

	/**
	 * Variable referencing a worm with direction 2.
	 */
	private static Worm wormDirection2;
	
	/**
	 * Variable referencing a worm with direction 0.
	 */
	private static Worm wormDirection0;

	/**
	 * Set up a mutable test fixture.
	 * 
	 * @post The variable wormDirection4 references a new worm with radius 0.25, x and y coordinates 0, and direction 4.
	 * @post The variable wormDirection2 references a new worm with radius 0.25, x and y coordinates 0, and direction 2.
	 * @post The variable wormDirection0 references a new worm with radius 0.25, x and y coordinates 0, and direction 0.
	 */
	@Before
	public void setUpMutableFixture(){
		wormDirection4 = new Worm(0.0 , 0.0 , 4 , 0.25 , "Pieter");
		wormDirection2 = new Worm(0.0 , 0.0 , 2 , 0.25 , "Pieter");
		wormDirection0 = new Worm(0.0 , 0.0 , 0 , 0.25 , "Laurens");
	}

	/**
	 * Sets up an immutable test fixture.
	 * 
	 * @post Create a worm with x position 0, y position 0, direction pi/2, radius 1 and name Pieter.
	 */
	@BeforeClass
	public static void setUpImmutableFixture(){
		wormRadius1 = new Worm(0.0 , 0.0 , Math.PI/2 , 1.0 , "Pieter");
	}

	@Test
	public void constructor_LegalCase()
		throws Exception	{
		Worm myWorm = new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "Pieter");
		assertTrue(myWorm.getPosition().getX() == 0);
		assertTrue(myWorm.getPosition().getY() == 0);
		assertTrue(myWorm.getDirection()== Math.PI/2);
		assertTrue(myWorm.getRadius() == 0.30);
		assertEquals("Pieter" , myWorm.getName());	
	}

	@Test(expected = IllegalRadiusException.class)
	public void constructor_InvalidRadius()
		throws Exception{
		Worm myWorm = new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "Pieter");
		if(! myWorm.canHaveAsRadius(0.20))
			new Worm(0.0 , 0.0 , Math.PI/2 , 0.20 , "Pieter");
		if(! myWorm.canHaveAsRadius(Double.MIN_VALUE))
			new Worm(0.0 , 0.0 , Math.PI/2 , Double.MIN_VALUE , "Pieter");
		else
			throw new IllegalRadiusException(0, null);

	}

	@Test(expected = IllegalNameException.class)
	public void constructor_InvalidName()
		throws Exception{
		Worm myWorm = new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "Pieter");
		if(! myWorm.canHaveAsName("A"))
			new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "A");
		if(! myWorm.canHaveAsName("abc"))
			new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "abc");
		if(! myWorm.canHaveAsName("Pieter(vos)"))
			new Worm(0.0 , 0.0 , Math.PI/2 , 0.30 , "Pieter(vos)");
		else
			throw new IllegalNameException(" ",null);

	}

	@Test
	public void canMove_legalCase() {
		assertTrue(wormRadius1.getPosition().canMove(1));
	}

	@Test
	public void canMove_IllegalCase() {
		assertFalse(wormRadius1.getPosition().canMove(Integer.MAX_VALUE));
	}

	@Test
	public void canMove_NegativeSteps() {
		assertFalse(wormRadius1.getPosition().canMove(-1));
	}
	
	//TODO jumps testen!
	
	//TODO : getJumpTime testen
	
	//TODO : jump testen

	//TODO : jumps testen


	@Test(expected = RuntimeException.class)
	public void jump_IllegalDirection()
		throws Exception	{
		wormDirection4.getPosition().jump();
	}

	@Test
	public void move_legalCase()
		throws Exception	{
		wormDirection2.getPosition().move(1);
		assertTrue(wormDirection2.getPosition().getX() == Math.cos(wormDirection2.getDirection()) * wormDirection2.getRadius() * 1);
		assertTrue(wormDirection2.getPosition().getY() == Math.sin(wormDirection2.getDirection()) * wormDirection2.getRadius() * 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void move_IllegalNumberOfSteps()
		throws Exception	{
		wormDirection2.getPosition().move(Integer.MAX_VALUE);
	}

	@Test
	public void setRadius_legalCase()
		throws Exception	{
		wormDirection2.setRadius(0.5);
		assertTrue(wormDirection2.getRadius() == 0.5);
	}

	@Test(expected = IllegalRadiusException.class)
	public void setRadius_IllegalRadius()
		throws Exception	{
		wormDirection2.setRadius(wormDirection2.getMinimalRadius() - 0.02);
	}

	@Test
	public void canHaveAsRadius_LegalCase()
		throws Exception	{
		assertTrue(wormDirection2.canHaveAsRadius(wormDirection2.getMinimalRadius() + 0.02));
	}

	@Test
	public void canHaveAsRadius_IllegalCase()
		throws Exception	{
		assertFalse(wormDirection2.canHaveAsRadius(-0.25));
	}

	@Test
	public void setMinimalRadius_LegalCase()
		throws Exception	{
		wormDirection2.setMinimalRadius(0.2);
		assertTrue(wormDirection2.getMinimalRadius() == 0.2);
	}

	@Test(expected = IllegalRadiusException.class)
	public void setMinimalRadius_NegativeRadius()
		throws Exception	{
		wormDirection2.setMinimalRadius(-0.2);
	}

	@Test
	public void isValidDirection_legalCase()
		throws Exception	{
		assertTrue(wormDirection2.isValidDirection(1));
	}

	@Test
	public void IsValidDirection_illegalCase()
		throws Exception	{
		assertFalse(wormDirection2.isValidDirection(10));
	}

	@Test
	public void canTurn_legalCase()
	{
		assertTrue(wormDirection2.canTurn(1));
	}

	@Test
	public void canTurn_IllegalCase()
	{
		assertFalse(wormDirection2.canTurn(Integer.MAX_VALUE));
	}

	@Test
	public void turn_LegalCase()
	{
		int oldActionPoints = (int) wormDirection2.getCurrentActionPoints();
		wormDirection2.turn(1.0);
		assertTrue(wormDirection2.getDirection() == 3.0);
		assertTrue(wormDirection2.getCurrentActionPoints() == oldActionPoints - (int)(Math.ceil(Math.abs(1) / (2*Math.PI) * 60)));
	}

	@Test
	public void setName_LegalCase()
		throws IllegalNameException
	{
		wormDirection2.setName("Pieter Vos");
		assertTrue(wormDirection2.getName().equals("Pieter Vos"));
	}

	@Test( expected = IllegalNameException.class)
	public void setName_IllegalCase()
		throws IllegalNameException
	{
		wormDirection2.setName("Pieter(Vos)");
		wormDirection2.setName("Laurens'\" LoOtS");
	}

}
