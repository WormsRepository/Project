package worms.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WorldTest {
	
	/**
	 * Variable referencing a world.
	 */
	private static World testWorld1;
	/**
	 * a passable map.
	 */
	boolean[][] passableMap;


	/**
	 * Set up a mutable test fixture.
	 * 
	 * @post	The variable testWorld1 references a new world with a width and height of 10 meter,
	 * 			a passable map and a random.
	 */
	@Before
	public void setUpMutableFixture(){
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,false,false,false,false,false}};
		testWorld1 = new World(6,6,passableMap,random);
	}

	/**
	 * 
	 */
	@BeforeClass
	public static void setUpImmutableFixture(){
	}
	
	
	@Test
	public void constructor_legalCase(){
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,false,false,false,false,false}};
		World myWorld = new World(10,15,passableMap, random);
		assertTrue(myWorld.getWidth() == 10);
		assertTrue(myWorld.getHeight() == 15);
		assertTrue(myWorld.getPassableMap().equals(passableMap));
		assertTrue(myWorld.getRandom().equals(random));
	}
	@Test(expected = IllegalArgumentException.class)
	public void constructor_illegalWidth()
		throws Exception{
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,false,false,false,false,false}};
		World myWorld = new World(10,15,passableMap, random);
		if(!myWorld.canHaveAsWidthOrHeight(-1))
			new World(-1,15,passableMap, random);
		if(!myWorld.canHaveAsWidthOrHeight( Double.MAX_VALUE +1 ))
			new World(Double.MAX_VALUE +1,15,passableMap, random);
		else
			throw new IllegalArgumentException();
	}
	
	
	
	@Test(expected = IllegalArgumentException.class)
	public void constructor_illegalHeight()
		throws Exception{
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,true,true,true,true,false},
				   {false,false,false,false,false,false}};
		World myWorld = new World(10,15,passableMap, random);
		if(!myWorld.canHaveAsWidthOrHeight(-1))
			new World(10,-1,passableMap, random);
		if(!myWorld.canHaveAsWidthOrHeight( Double.MAX_VALUE +1 ))
			new World(10,Double.MAX_VALUE +1,passableMap, random);
		else
			throw new IllegalArgumentException();
	}
	
	@Test
	public void deactivate_legalCase1(){
		testWorld1.addAsFood(new Food(1,2));
		
		testWorld1.deactivate();
		assertTrue(!testWorld1.isActive());
		assertTrue(testWorld1.getFood().isEmpty());
	}
	
	@Test
	public void isActive_legalCase1(){
		assertTrue(testWorld1.isActive());
	}
	
	@Test
	public void isActive_legalCase2(){
		testWorld1.deactivate();
		assertTrue(!testWorld1.isActive());
	}
	

	
	@Test
	public void isStarted_legalCase1(){
		assertTrue(!testWorld1.isStarted());
	}
	
	@Test
	public void startGame_IllegalCase1(){
		testWorld1.addAsWorm(new Worm(1,1,0,1,"Test"));
		testWorld1.startGame();
		assertTrue(!testWorld1.isStarted());
	}
	
	@Test
	public void startGame_IllegalCase2(){
		testWorld1.startGame();
		assertTrue(!testWorld1.isStarted());
	}
	
	@Test
	public void canHaveAsWidthOrHeight_legalCase(){
		assertTrue(testWorld1.canHaveAsWidthOrHeight(2));
	}
	
	@Test
	public void canHaveAsWidthOrHeight_legalCase2(){
		assertTrue(testWorld1.canHaveAsWidthOrHeight(Double.MAX_VALUE -1));
	}
	
	@Test
	public void canHaveAsWidthOrHeight_IllegalCase1(){
		assertTrue(!testWorld1.canHaveAsWidthOrHeight(-2));
	}
	
	@Test
	public void getWidth_legalCase(){
		assertTrue(testWorld1.getWidth() == 6);
	}
	
	@Test
	public void getHeight_legalCase(){
		assertTrue(testWorld1.getHeight() == 6);
	}
	
	@Test
	public void getPassableMap(){
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,false,false,false,false,false}};
		
		World testWorld = new World(6,6,passableMap,random);
		assertTrue(testWorld.getPassableMap().equals(passableMap));
	}
	
	@Test
	public void startGame_LegalCase1(){
		testWorld1.addAsWorm(new Worm(1,1,0,1,"Test"));
		testWorld1.addAsWorm(new Worm(1,2,0,1,"Test2"));
		testWorld1.addAsWorm(new Worm(1,2,0,1,"Test3"));
		testWorld1.startGame();
		assertTrue(testWorld1.isStarted());
	}
	
	
	@Test
	public void isImpassable_legalCase1(){
		assertTrue(testWorld1.isImpassable(5, 5, 1));
	}
	
	@Test
	public void isImpassable_IllegalCase1(){
		assertTrue(!testWorld1.isImpassable(2, 2, 1));
	}
	
	@Test
	public void isAdjacent_IllegalCase1(){
		assertTrue(!testWorld1.isAdjacent(5, 5, 1));
	}
	
	@Test
	public void isAdjacent_IllegalCase(){
		assertTrue(!testWorld1.isAdjacent(3, 3, 0.5));
	}
	
	@Test
	public void isAdjacent_LegalCase(){
		assertTrue(testWorld1.isAdjacent(2,2,1));
	}
	
	@Test
	public void canFall_LegalCase(){
		assertTrue(testWorld1.canFall(2, 2, 0.25));
	}
	
	@Test
	public void canFall_IllegalCase(){
		assertTrue(!testWorld1.canFall(0, 0, 1));
	}
	
	@Test
	public void canFall_IllegalCase2(){
		assertTrue(!testWorld1.canFall(2, 2, 1));
	}
	
	@Test
	public void hasAsTeam_legalCase(){
		Team team = new Team("Pieter");
		testWorld1.addAsTeam(team);
		assertTrue(testWorld1.hasAsTeam(team));
	}
	

	
	@Test
	public void addNewWorm_legalCase(){
		for(int i = 0;i<10;i++){
			testWorld1.addNewWorm();
		}
		for(Worm worm: testWorld1.getWorms()){
			assertTrue(testWorld1.isAdjacent(worm.getWormPosition().getX(),
					worm.getWormPosition().getY(),worm.getRadius()) == true);
			assertTrue(worm.getRadius() == 0.25);
		}
	}
	
	@Test
	public void addNewFood_legalCase(){
		for(int i = 0;i<10;i++){
			testWorld1.addNewFood();
		}
		for(Food food: testWorld1.getFood()){
			assertTrue(testWorld1.isAdjacent(food.getX(),
					food.getY(),Food.getRadius()) == true);
			assertTrue(Food.getRadius() == 0.20);
		}
	}
}
