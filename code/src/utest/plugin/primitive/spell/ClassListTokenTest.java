/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.primitive.spell;

import java.net.URISyntaxException;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.list.ClassSpellList;
import pcgen.core.Race;
import pcgen.core.spell.Spell;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import plugin.lsttokens.ChooseLst;
import plugin.lsttokens.choose.SpellsToken;
import plugin.lsttokens.testsupport.AbstractPrimitiveTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.TokenRegistration;

public class ClassListTokenTest extends
		AbstractPrimitiveTokenTestCase<CDOMObject, ClassSpellList>
{
	static ChooseLst token = new ChooseLst();
	static SpellsToken subtoken = new SpellsToken();
	static CDOMTokenLoader<CDOMObject> loader = new CDOMTokenLoader<CDOMObject>();

	private static final ClassListToken CLASSLIST_TOKEN = new ClassListToken();

	public ClassListTokenTest()
	{
		super("CLASSLIST", "Foo");
	}

	@Override
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		TokenRegistration.register(CLASSLIST_TOKEN);
		primaryContext.getReferenceContext().constructNowIfNecessary(Spell.class, "Placeholder");
		secondaryContext.getReferenceContext().constructNowIfNecessary(Spell.class, "Placeholder");
	}

	@Override
	public CDOMSecondaryToken<?> getSubToken()
	{
		return subtoken;
	}

	@Override
	public Class<ClassSpellList> getTargetClass()
	{
		return ClassSpellList.class;
	}

	@Override
	public Class<Race> getCDOMClass()
	{
		return Race.class;
	}

	@Override
	public CDOMLoader<CDOMObject> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<CDOMObject> getToken()
	{
		return token;
	}

	public void testPrimitiveIllegalNullTarget()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget(null);
	}

	public void testPrimitiveIllegalNoTarget() throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("");
	}

	public void testPrimitiveIllegalBadArgs() throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[Hi]");
	}

	public void testPrimitiveIllegalBadKnownEquals()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[KNOWN=]");
	}

	public void testPrimitiveIllegalBadKnownEqualsBad()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[KNOWN=Bad]");
	}

	public void testPrimitiveIllegalBadLevelMax()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMAX]");
	}

	public void testPrimitiveIllegalBadLevelMaxEquals()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMAX=]");
	}

	public void testPrimitiveIllegalBadLevelMaxEqualsBad()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMAX=3-]");
	}

	public void testPrimitiveIllegalBadLevelMin()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMIN]");
	}

	public void testPrimitiveIllegalBadLevelMinEquals()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMIN=]");
	}

	public void testPrimitiveIllegalBadLevelMinEqualsBad()
			throws PersistenceLayerException
	{
		doPrimitiveIllegalTarget("Foo[LEVELMIN=3+]");
	}
}