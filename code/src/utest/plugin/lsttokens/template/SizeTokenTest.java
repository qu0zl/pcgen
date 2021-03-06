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
package plugin.lsttokens.template;

import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.core.PCTemplate;
import pcgen.core.SizeAdjustment;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class SizeTokenTest extends AbstractTokenTestCase<PCTemplate>
{

	static SizeToken token = new SizeToken();
	static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>();

	@Override
	public Class<PCTemplate> getCDOMClass()
	{
		return PCTemplate.class;
	}

	@Override
	public CDOMLoader<PCTemplate> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<PCTemplate> getToken()
	{
		return token;
	}

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		SizeAdjustment ps = primaryContext.getReferenceContext().constructCDOMObject(SizeAdjustment.class, "Small");
		primaryContext.getReferenceContext().registerAbbreviation(ps, "S");
		SizeAdjustment pm = primaryContext.getReferenceContext().constructCDOMObject(SizeAdjustment.class, "Medium");
		primaryContext.getReferenceContext().registerAbbreviation(pm, "M");
		SizeAdjustment ss = secondaryContext.getReferenceContext().constructCDOMObject(SizeAdjustment.class, "Small");
		secondaryContext.getReferenceContext().registerAbbreviation(ss, "S");
		SizeAdjustment sm = secondaryContext.getReferenceContext().constructCDOMObject(SizeAdjustment.class, "Medium");
		secondaryContext.getReferenceContext().registerAbbreviation(sm, "M");
	}

	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	@Test
	public void testRoundRobinS() throws PersistenceLayerException
	{
		runRoundRobin("S");
	}

	@Test
	public void testRoundRobinM() throws PersistenceLayerException
	{
		runRoundRobin("M");
	}

	@Test
	public void testRoundRobinFormula() throws PersistenceLayerException
	{
		runRoundRobin("max(4,String)");
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "max(4,String)";
	}

	@Override
	protected String getLegalValue()
	{
		return "M";
	}

	@Test
	public void testUnparseNull() throws PersistenceLayerException
	{
		primaryProf.put(FormulaKey.SIZE, null);
		assertNull(getToken().unparse(primaryContext, primaryProf));
	}

	@Test
	public void testUnparseLegal() throws PersistenceLayerException
	{
		primaryProf.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		expectSingle(getToken().unparse(primaryContext, primaryProf), "1");
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.OVERWRITE;
	}
}
