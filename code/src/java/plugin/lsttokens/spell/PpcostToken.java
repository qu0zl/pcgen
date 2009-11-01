/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.spell;

import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.spell.Spell;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractIntToken;
import pcgen.rules.persistence.token.CDOMPrimaryParserToken;

/**
 * Class deals with PPCOST Token
 */
public class PpcostToken extends AbstractIntToken<Spell> implements CDOMPrimaryParserToken<Spell>
{

	public String getTokenName()
	{
		return "PPCOST";
	}

	@Override
	protected IntegerKey integerKey()
	{
		return IntegerKey.PP_COST;
	}

	@Override
	protected int minValue()
	{
		return 0;
	}

	public String[] unparse(LoadContext context, Spell spell)
	{
		Integer i = context.getObjectContext().getInteger(spell,
				IntegerKey.PP_COST);
		if (i == null)
		{
			return null;
		}
		if (i.intValue() < 0)
		{
			context.addWriteMessage(getTokenName()
					+ " requires a positive Integer");
			return null;
		}
		return new String[] { i.toString() };
	}

	public Class<Spell> getTokenClass()
	{
		return Spell.class;
	}
}
