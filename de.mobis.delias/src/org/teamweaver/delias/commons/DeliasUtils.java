package org.teamweaver.delias.commons;

import org.eclipse.ui.PlatformUI;

public class DeliasUtils {
	
	public static String normalizeDescriptiveField(String descriptiveField)
	{
		if (descriptiveField == null || descriptiveField.length() <= 0)
			return descriptiveField;

		final char[][] UNWANTED_CHARACTERS = new char[][]
		{ new char[]
		{ '&', 0 }, new char[]
		{ '\t', ' ' }, new char[]
		{ '\n', ' ' }, new char[]
		{ '\r', ' ' } };
		String cleanDescription = "";
		char currentCharacter;
		boolean isUnwantedChar;
		for (int i = 0, descriptionLength = descriptiveField.length(); i < descriptionLength; i++)
		{
			currentCharacter = descriptiveField.charAt(i);
			isUnwantedChar = false;

			for (int j = 0; j < UNWANTED_CHARACTERS.length; j++)
			{
				if (currentCharacter == UNWANTED_CHARACTERS[j][0])
				{
					if (UNWANTED_CHARACTERS[j][1] > 0)
						cleanDescription += UNWANTED_CHARACTERS[j][1];

					isUnwantedChar = true;
					break;
				}
			}

			if (!isUnwantedChar)
				cleanDescription += currentCharacter;
		}

		return cleanDescription;
	}
	
	public static String getActivePartTitle(){
		return PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart().getTitle();
	}
}
