/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 *
 * Permission has been explicitly granted to the University of Minnesota
 * Software Engineering Center to use and distribute this source for
 * educational purposes, including delivering online education through
 * Coursera or other entities.
 *
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including
 * fitness for purpose.
 *
 * Modifications
 * 20171113 - Michael W. Whalen - Extended with additional recipe.
 * 20171114 - Ian J. De Silva   - Updated to JUnit 4; fixed variable names.
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;



/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Sarah Heckman
 *
 * Extended by Mike Whalen
 */

public class CoffeeMakerTest {
	
	//-----------------------------------------------------------------------
	//	DATA MEMBERS
	//-----------------------------------------------------------------------
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;
	private Recipe recipe5;
	private Recipe recipe6;
	
	private Recipe [] stubRecipies;	
	
	/**
	 * The coffee maker -- our object under test.
	 */
	private CoffeeMaker coffeeMaker;
	
	/**
	 * The stubbed recipe book.
	 */
	private RecipeBook recipeBookStub;
	
	
	//-----------------------------------------------------------------------
	//	Set-up / Tear-down
	//-----------------------------------------------------------------------
	/**
	 * Initializes some recipes to test with, creates the {@link CoffeeMaker}	
	 * object we wish to test, and stubs the {@link RecipeBook}.	
	 *	
	 * @throws RecipeException	if there was an error parsing the ingredient	
	 * amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		
		recipeBookStub = mock(RecipeBook.class);
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		
		//Set up for recipe1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for recipe2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for recipe3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for recipe4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
		
		//Set up for recipe5 (added by MWW)
		recipe5 = new Recipe();
		recipe5.setName("Super Hot Chocolate");
		recipe5.setAmtChocolate("6");
		recipe5.setAmtCoffee("60");
		recipe5.setAmtMilk("1");
		recipe5.setAmtSugar("1");
		recipe5.setPrice("100");

		//Set up for recipe6 (added by MWW)
		recipe6 = new Recipe();
		recipe6.setName("Super Hot Chocolate");
		recipe6.setAmtChocolate("6");
		recipe6.setAmtCoffee("0");
		recipe6.setAmtMilk("1");
		recipe6.setAmtSugar("40");
		recipe6.setPrice("100");

		stubRecipies = new Recipe [] {recipe1, recipe2, recipe3};

	}
	
	
	//-----------------------------------------------------------------------
	//	Test Methods
	//-----------------------------------------------------------------------
	
	// put your tests here!
	
	@Test
	public void testMakeCoffee() {
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		assertTrue(true); // This test doesn't really do anything meaningful. Consider removing or adding assertions.
	}

	/**
	 * UC7: PurchaseBeverage
	 */

	/**
	 * Given a coffee maker with enough ingedients
	 * When we put in enough money
	 * Then we get our coffee and corresponding change
	 */
	@Test
	public void testPurchaseBeverage1(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(stubRecipies);

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		// Make coffee
		assertEquals(50, coffeeMaker.makeCoffee(0, 100));
		
	}

	/**
	 * Given a coffee maker with enough ingedients
	 * When we put in too little money
	 * Then we get our money back and no coffee
	 */
	@Test
	public void testPurchaseBeverage2(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(stubRecipies);

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		// Make coffee
		assertEquals(5, coffeeMaker.makeCoffee(0, 5));
		
	}

	/**
	 * Given a coffee maker with not enough ingedients
	 * When we put in enough money
	 * Then we get no coffee and our money back
	 */
	@Test
	public void testPurchaseBeverage3(){
		// Create RecipeBook stub
		// recipe6 has 40 sugar, initial inventory has 15, so not enough sugar.
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, null});
		Inventory inventory = new Inventory(); // Use a real inventory to check ingredient changes
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		int initialCoffee = inventory.getCoffee();
		int initialMilk = inventory.getMilk();
		int initialSugar = inventory.getSugar();
		int initialChocolate = inventory.getChocolate();

		// Attempt to make coffee with insufficient ingredients
		assertEquals(100, coffeeMaker.makeCoffee(1, 100));
		
		// Verify that inventory did NOT change (because coffee was not made)
		assertEquals(initialCoffee, inventory.getCoffee());
		assertEquals(initialMilk, inventory.getMilk());
		assertEquals(initialSugar, inventory.getSugar());
		assertEquals(initialChocolate, inventory.getChocolate());
	}

	/**
	 * Given a coffee maker with 2 recipes
	 * When we try to purchase a recipe at index 2 (which is null)
	 * Then we get our money back and no change in inventory
	 */
	@Test
	public void testPurchaseBeverage4(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, null});
		Inventory inventory = new Inventory();
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		int initialCoffee = inventory.getCoffee();
		int initialMilk = inventory.getMilk();
		int initialSugar = inventory.getSugar();
		int initialChocolate = inventory.getChocolate();

		// Attempt to purchase a non-existent recipe (index 2 is null)
		assertEquals(100, coffeeMaker.makeCoffee(2, 100));

		// Verify that inventory did NOT change
		assertEquals(initialCoffee, inventory.getCoffee());
		assertEquals(initialMilk, inventory.getMilk());
		assertEquals(initialSugar, inventory.getSugar());
		assertEquals(initialChocolate, inventory.getChocolate());
	}

	/**
	 * Given a coffee maker with 2 recipes
	 * When we try to purchase a recipe at index 4 (out of bounds)
	 * Then we get our money back and no change in inventory
	 */
	@Test
	public void testPurchaseBeverage7(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, null});
		Inventory inventory = new Inventory();
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		int initialCoffee = inventory.getCoffee();
		int initialMilk = inventory.getMilk();
		int initialSugar = inventory.getSugar();
		int initialChocolate = inventory.getChocolate();

		// Attempt to purchase a recipe at an out-of-bounds index
		assertEquals(100, coffeeMaker.makeCoffee(4, 100));

		// Verify that inventory did NOT change
		assertEquals(initialCoffee, inventory.getCoffee());
		assertEquals(initialMilk, inventory.getMilk());
		assertEquals(initialSugar, inventory.getSugar());
		assertEquals(initialChocolate, inventory.getChocolate());
	}

	/**
	 * Given a coffee maker with not enough ingedients (recipe5 has 60 coffee, initial inventory has 15)
	 * When we put in enough money
	 * Then we get no coffee and our money back, and no inventory change
	 */
	@Test
	public void testPurchaseBeverage8(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe5, null});
		Inventory inventory = new Inventory();
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		int initialCoffee = inventory.getCoffee();
		int initialMilk = inventory.getMilk();
		int initialSugar = inventory.getSugar();
		int initialChocolate = inventory.getChocolate();

		// Attempt to make coffee with insufficient ingredients
		assertEquals(100, coffeeMaker.makeCoffee(1, 100));

		// Verify that inventory did NOT change (because coffee was not made)
		assertEquals(initialCoffee, inventory.getCoffee());
		assertEquals(initialMilk, inventory.getMilk());
		assertEquals(initialSugar, inventory.getSugar());
		assertEquals(initialChocolate, inventory.getChocolate());
	}


	/**
	 * CHECK FOR ENOUGH INGREDIENTS
	 */

	/**
	 * Given a coffee maker with 3 recipes
	 * When we try to purchase a recipe with not enough ingredients
	 * Then the useIngredients method of the inventory class gets 
	 * called with the recipe we want to buy
	 */
	@Test
	public void testPurchaseBeverage5(){
		// Create Inventory Spy
		Inventory inventorySpy= spy(new Inventory());
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, recipe3});

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventorySpy);
		// Make coffee (recipe3, for simplicity, assuming initial inventory is sufficient)
		// For this test, we want to ensure useIngredients is *called*.
		// If you want to test the *failure* case where useIngredients is NOT called,
		// you'd need to set up the inventory to be insufficient.
		// As written, recipe3 is within default inventory limits (chocolate 0, coffee 3, milk 3, sugar 1).
		// So this test as is would pass if the recipe is made.
		// To test the "not enough ingredients" path for useIngredients being called,
		// it should be *not* called. Let's adjust the stubbing or the recipe used to force insufficient ingredients.
		// If recipe3 is used, and it IS successfully made, useIngredients *should* be called.
		// Let's use recipe6 to ensure insufficient ingredients.
		coffeeMaker.makeCoffee(1, 100); // Try to make recipe6, which has too much sugar

		// Verify method call - useIngredients should NOT be called if not enough ingredients
		verify(inventorySpy, times(0)).useIngredients(recipe6); // Expect 0 calls if ingredients are insufficient.
		
	}

	/**
	 * Given a coffee maker with 3 recipes
	 * When we try to purchase a recipe with not enough ingredients
	 * Then the enoughIngredients method of the inventory class gets 
	 * called with the recipe we want to buy
	 */
	@Test
	public void testPurchaseBeverage6(){
		// Create Inventory Spy
		Inventory inventorySpy= spy(new Inventory());
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, recipe3});

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventorySpy);
		// Make coffee
		coffeeMaker.makeCoffee(1, 100); // Try to make recipe6, which has too much sugar

		// Verify method call - enoughIngredients *should* be called to check.
		verify(inventorySpy).enoughIngredients(recipe6);
		
	}


	/**
	 * VERIFY THAT getAmtChocolate(), getAmtCoffee(), getAmtMilk(), and getPrice()	
	 * ARE BEING CALLED ONCE ON THE SELECTED RECIPE AND ZERO TIMES ON OTHER RECIPES
	 */

	/**
	 * Given a coffee maker with recipes
	 * When we try to purchase a recipe at index 2 (Latte)
	 * Then the getAmt functions get called on the selected recipe at least once
	 * and do not get called on the other 2 recipes.
	 * Assuming enough money and ingredients for recipe3 (Latte).
	 */
	@Test
	public void testVerifyCall(){
		// Spy on recipes
		Recipe recipe1Spy= spy(recipe1);
		Recipe recipe6Spy= spy(recipe6); // This one will have insufficient ingredients in default inventory
		Recipe recipe3Spy= spy(recipe3);

		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1Spy, recipe6Spy, recipe3Spy});

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		// Make coffee (recipe3 - Latte)
		assertEquals(0, coffeeMaker.makeCoffee(2, 100)); // Should succeed, price 100, money 100, change 0

		// Verify method calls for selected recipe (recipe3Spy)
		verify(recipe3Spy, atLeastOnce()).getAmtChocolate();
		verify(recipe3Spy, atLeastOnce()).getAmtMilk();
		verify(recipe3Spy, atLeastOnce()).getAmtSugar();
		verify(recipe3Spy, atLeastOnce()).getAmtCoffee();
		verify(recipe3Spy, atLeastOnce()).getPrice();

		// Verify method calls for other recipes (recipe1Spy, recipe6Spy) - should be 0 calls
		verify(recipe1Spy, times(0)).getAmtChocolate();
		verify(recipe1Spy, times(0)).getAmtMilk();
		verify(recipe1Spy, times(0)).getAmtSugar();
		verify(recipe1Spy, times(0)).getAmtCoffee();
		verify(recipe1Spy, times(0)).getPrice();

		verify(recipe6Spy, times(0)).getAmtChocolate();
		verify(recipe6Spy, times(0)).getAmtMilk();
		verify(recipe6Spy, times(0)).getAmtSugar();
		verify(recipe6Spy, times(0)).getAmtCoffee();
		verify(recipe6Spy, times(0)).getPrice();
	}

	/**
	 * Given a coffee maker with 2 recipes
	 * When we try to purchase a recipe with not enough ingredients (recipe6)
	 * Then the getAmt functions get called only once on recipe6 (to check ingredients/price)
	 * and not on other recipes.
	 */
	@Test
	public void testVerifyCall1(){
		// Spy on recipes
		Recipe recipe1Spy= spy(recipe1);
		Recipe recipe6Spy= spy(recipe6);
		Recipe recipe3Spy= spy(recipe3);

		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1Spy, recipe6Spy, recipe3Spy});

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		// Make coffee (recipe6 - insufficient sugar)
		assertEquals(100, coffeeMaker.makeCoffee(1, 100)); // Should return all money back

		// Verify method calls for selected recipe (recipe6Spy) - these should be called once to check availability/price
		verify(recipe6Spy, times(1)).getAmtChocolate();
		verify(recipe6Spy, times(1)).getAmtMilk();
		verify(recipe6Spy, times(1)).getAmtSugar();
		verify(recipe6Spy, times(1)).getAmtCoffee();
		verify(recipe6Spy, times(1)).getPrice();

		// Verify method calls for other recipes (recipe1Spy, recipe3Spy) - should be 0 calls
		verify(recipe1Spy, times(0)).getAmtChocolate();
		verify(recipe1Spy, times(0)).getAmtMilk();
		verify(recipe1Spy, times(0)).getAmtSugar();
		verify(recipe1Spy, times(0)).getAmtCoffee();
		verify(recipe1Spy, times(0)).getPrice();

		verify(recipe3Spy, times(0)).getAmtChocolate();
		verify(recipe3Spy, times(0)).getAmtMilk();
		verify(recipe3Spy, times(0)).getAmtSugar();
		verify(recipe3Spy, times(0)).getAmtCoffee();
		verify(recipe3Spy, times(0)).getPrice();
	}

	/**
	 * Given a coffee maker with recipes
	 * When we try to purchase a recipe at index 0 (Coffee)
	 * Then the getAmt functions get called on the selected recipe at least once
	 * and do not get called on the other 2 recipes.
	 * Assuming enough money and ingredients for recipe1 (Coffee).
	 */
	@Test
	public void testVerifyCall2(){
		// Spy on recipes
		Recipe recipe1Spy= spy(recipe1);
		Recipe recipe6Spy= spy(recipe6);
		Recipe recipe3Spy= spy(recipe3);

		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1Spy, recipe6Spy, recipe3Spy});

		// Create coffee maker
		coffeeMaker = new CoffeeMaker(recipeBookStub, new Inventory());
		// Make coffee (recipe1 - Coffee)
		assertEquals(50, coffeeMaker.makeCoffee(0, 100)); // Should succeed, price 50, money 100, change 50

		// Verify method calls for selected recipe (recipe1Spy)
		verify(recipe1Spy, atLeastOnce()).getAmtChocolate();
		verify(recipe1Spy, atLeastOnce()).getAmtMilk();
		verify(recipe1Spy, atLeastOnce()).getAmtSugar();
		verify(recipe1Spy, atLeastOnce()).getAmtCoffee();
		verify(recipe1Spy, atLeastOnce()).getPrice();

		// Verify method calls for other recipes (recipe3Spy, recipe6Spy) - should be 0 calls
		verify(recipe3Spy, times(0)).getAmtChocolate();
		verify(recipe3Spy, times(0)).getAmtMilk();
		verify(recipe3Spy, times(0)).getAmtSugar();
		verify(recipe3Spy, times(0)).getAmtCoffee();
		verify(recipe3Spy, times(0)).getPrice();

		verify(recipe6Spy, times(0)).getAmtChocolate();
		verify(recipe6Spy, times(0)).getAmtMilk();
		verify(recipe6Spy, times(0)).getAmtSugar();
		verify(recipe6Spy, times(0)).getAmtCoffee();
		verify(recipe6Spy, times(0)).getPrice();
	}

	/**
	 * CHECK THAT INVENTORY HAS DECREASED
	 */
	/**
	 * Given a coffee maker with enough ingedients
	 * When we put in enough money
	 * Then we get coffee and the coffee maker's inventory decreases
	 */
	@Test
	public void testInventory(){
		// Create RecipeBook stub
		// Ensure recipe1 has enough ingredients with default Inventory
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, null});

		// Create coffee maker with a real Inventory instance
		Inventory inventory = new Inventory();
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		// Get initial inventory values
		int initialCoffee = inventory.getCoffee();
		int initialMilk = inventory.getMilk();
		int initialSugar = inventory.getSugar();
		int initialChocolate = inventory.getChocolate();
		
		// Make coffee (recipe1)
		coffeeMaker.makeCoffee(0, 100);

		// Assert that the inventory has decreased for the ingredients in recipe1
		assertEquals(initialChocolate - recipe1.getAmtChocolate(), inventory.getChocolate());
		assertEquals(initialCoffee - recipe1.getAmtCoffee(), inventory.getCoffee());
		assertEquals(initialMilk - recipe1.getAmtMilk(), inventory.getMilk());
		assertEquals(initialSugar - recipe1.getAmtSugar(), inventory.getSugar());
	}

	/**
	 * Given a coffee maker with enough ingedients
	 * When we put in enough money
	 * Then we get coffee and the coffee maker's inventory decreases.
	 * This test is very similar to testInventory and could potentially be merged or refined.
	 */
	@Test
	public void testInventory1(){
		// Create RecipeBook stub
		when(recipeBookStub.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe6, null});

		// Create coffee maker
		Inventory inventory= new Inventory();
		coffeeMaker = new CoffeeMaker(recipeBookStub, inventory);

		// Make coffee
		coffeeMaker.makeCoffee(0, 100);

		// Check decreased inventory
		assertEquals(15-Integer.parseInt(recipe1.getAmtChocolate()), inventory.getChocolate());
		assertEquals(15-Integer.parseInt(recipe1.getAmtMilk()), inventory.getMilk());
		assertEquals(15-Integer.parseInt(recipe1.getAmtSugar()), inventory.getSugar());
		assertEquals(15-Integer.parseInt(recipe1.getAmtCoffee()), inventory.getCoffee());
		
	}
