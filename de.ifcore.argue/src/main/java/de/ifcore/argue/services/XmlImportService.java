package de.ifcore.argue.services;

import java.io.File;

public interface XmlImportService
{
	/**
	 * imports the content of the given xml file (when supported)
	 * 
	 * @param xmlFile
	 */
	public void importFile(File xmlFile);
}
