/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens;

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractGlobalListTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class TemplateLstTest extends
		AbstractGlobalListTokenTestCase<PCTemplate>
{

	@Override
	public char getJoinCharacter()
	{
		return '|';
	}

	@Override
	public Class<PCTemplate> getTargetClass()
	{
		return PCTemplate.class;
	}

	@Override
	public boolean isTypeLegal()
	{
		return true;
	}

	@Override
	public boolean isAllLegal()
	{
		return false;
	}

	@Override
	public boolean isClearDotLegal()
	{
		return false;
	}

	@Override
	public boolean isClearLegal()
	{
		return false;
	}

	static CDOMPrimaryToken<CDOMObject> token = new TemplateLst();
	static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>();

	@Override
	public CDOMLoader<PCTemplate> getLoader()
	{
		return loader;
	}

	@Override
	public Class<PCTemplate> getCDOMClass()
	{
		return PCTemplate.class;
	}

	@Override
	public CDOMPrimaryToken<CDOMObject> getToken()
	{
		return token;
	}

	@Test
	public void testChooseInvalidInputString() throws PersistenceLayerException
	{
		assertTrue(parse("CHOOSE:String"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputType() throws PersistenceLayerException
	{
		assertTrue(parse("CHOOSE:TestType"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputJoinedComma()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("CHOOSE:TestWP1,TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputJoinedDot()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("CHOOSE:TestWP1.TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidListEnd() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("CHOOSE:TestWP1" + getJoinCharacter()));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidChooseRemove() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertTrue(parse("CHOOSE:" + "TestWP1.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidAddChoiceRemove() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertTrue(parse("ADDCHOICE:" + "TestWP1.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidListStart() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("CHOOSE:" + getJoinCharacter() + "TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidListDoubleJoin()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("CHOOSE:TestWP2" + getJoinCharacter()
				+ getJoinCharacter() + "TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidInputCheckMult()
			throws PersistenceLayerException
	{
		// Explicitly do NOT build testChooseWP2
		construct(primaryContext, "TestWP1");
		assertTrue(parse("CHOOSE:TestWP1" + getJoinCharacter() + "TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testChooseValidInputs() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("TestWP1"));
		assertTrue(parse("CHOOSE:TestWP1" + getJoinCharacter() + "TestWP2"));
		assertCleanConstruction();
	}

	@Test
	public void testChooseRoundRobinOne() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		runRoundRobin("CHOOSE:TestWP1");
	}

	@Test
	public void testParseStuffList() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		assertTrue(parse("TestWP1" + getJoinCharacter() + "TestWP2"
			+ getJoinCharacter() + "%LIST"));
	}

	@Test
	public void testRoundRobinList() throws PersistenceLayerException
	{
		runRoundRobin("%LIST");
	}

	@Test
	public void testChooseRoundRobinThree() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(primaryContext, "TestWP3");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		construct(secondaryContext, "TestWP3");
		runRoundRobin("CHOOSE:TestWP1" + getJoinCharacter() + "TestWP2"
				+ getJoinCharacter() + "TestWP3");
	}

	@Test
	public void testChooseInvalidInputAddString()
			throws PersistenceLayerException
	{
		assertTrue(parse("ADDCHOICE:String"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputAddType()
			throws PersistenceLayerException
	{
		assertTrue(parse("ADDCHOICE:TestType"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputAddJoinedComma()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("ADDCHOICE:TestWP1,TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidInputAddJoinedDot()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("ADDCHOICE:TestWP1.TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testChooseInvalidAddListEnd() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("ADDCHOICE:TestWP1" + getJoinCharacter()));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidAddListStart()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("ADDCHOICE:" + getJoinCharacter() + "TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidAddListDoubleJoin()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("ADDCHOICE:TestWP2" + getJoinCharacter()
				+ getJoinCharacter() + "TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testChooseInvalidAddInputCheckMult()
			throws PersistenceLayerException
	{
		// Explicitly do NOT build testChooseWP2
		construct(primaryContext, "TestWP1");
		assertTrue(parse("ADDCHOICE:TestWP1" + getJoinCharacter() + "TestWP2"));
		assertConstructionError();
	}

	/*
	 * TODO Need to do tests with ADDCHOICE:
	 */
	// @Test
	// public void testChooseRoundRobinAddOne() throws PersistenceLayerException
	// {
	// construct(primaryContext, "TestWP1");
	// construct(primaryContext, "TestWP2");
	// construct(secondaryContext, "TestWP1");
	// construct(secondaryContext, "TestWP2");
	// runRoundRobin("ADDCHOICE:TestWP1");
	// }
	//
	// @Test
	// public void testChooseRoundRobinAddThree() throws
	// PersistenceLayerException
	// {
	// construct(primaryContext, "TestWP1");
	// construct(primaryContext, "TestWP2");
	// construct(primaryContext, "TestWP3");
	// construct(secondaryContext, "TestWP1");
	// construct(secondaryContext, "TestWP2");
	// construct(secondaryContext, "TestWP3");
	// runRoundRobin("ADDCHOICE:TestWP1" + getJoinCharacter() + "TestWP2"
	// + getJoinCharacter() + "TestWP3");
	// }
	//

	@Test
	public void testRemoveInvalidInputString() throws PersistenceLayerException
	{
		assertTrue(parse("String.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testRemoveInvalidInputJoinedComma()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("TestWP1.REMOVE,TestWP2.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testRemoveInvalidInputJoinedDot()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("TestWP1.REMOVE.TestWP2.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testRemoveInvalidListEnd() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("TestWP1.REMOVE" + getJoinCharacter()));
		assertNoSideEffects();
	}

	@Test
	public void testRemoveInvalidListStart() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse(getJoinCharacter() + "TestWP1.REMOVE"));
		assertNoSideEffects();
	}

	@Test
	public void testRemoveInvalidListDoubleJoin()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("TestWP2.REMOVE" + getJoinCharacter()
				+ getJoinCharacter() + "TestWP1.REMOVE"));
		assertNoSideEffects();
	}

	@Test
	public void testRemoveInvalidInputCheckMult()
			throws PersistenceLayerException
	{
		// Explicitly do NOT build testChooseWP2
		construct(primaryContext, "TestWP1");
		assertTrue(parse("TestWP1.REMOVE" + getJoinCharacter() + "TestWP2.REMOVE"));
		assertConstructionError();
	}

	@Test
	public void testRemoveRoundRobinOne() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		runRoundRobin("TestWP1.REMOVE");
	}

	@Test
	public void testRemoveRoundRobinThree() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(primaryContext, "TestWP3");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		construct(secondaryContext, "TestWP3");
		runRoundRobin("TestWP1.REMOVE" + getJoinCharacter() + "TestWP2.REMOVE"
				+ getJoinCharacter() + "TestWP3.REMOVE");
	}
}
