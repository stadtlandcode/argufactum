package de.ifcore.argue.services;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractXmlImportService<T> implements XmlImportService, InitializingBean
{
	private static final Logger log = Logger.getLogger(ComparisonValueDefinitionXmlService.class.getName());
	private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private final PlatformTransactionManager transactionManager;
	private final Set<File> startupFiles;
	private final String mainNodeName;

	public AbstractXmlImportService(PlatformTransactionManager transactionManager, String startupFile,
			String directory, String mainNodeName)
	{
		this.transactionManager = transactionManager;
		this.startupFiles = getStartupFiles(startupFile, directory);
		this.mainNodeName = mainNodeName;
	}

	Set<File> getStartupFiles(String startupFile, String directory)
	{
		Set<File> startupFiles = new HashSet<>();
		if (startupFile != null)
		{
			try
			{
				Resource resource = resolver.getResource(startupFile);
				startupFiles.add(resource.getFile());
			}
			catch (IOException e)
			{
				log.error("could not read " + startupFile, e);
			}
		}
		if (directory != null)
		{
			try
			{
				Resource resource = resolver.getResource(directory);
				File directoryFile = resource.getFile();
				for (File fileInDirectory : directoryFile.listFiles())
				{
					if (fileInDirectory.isFile())
						startupFiles.add(fileInDirectory);
				}
			}
			catch (IOException e)
			{
				log.error("could not read " + directory, e);
			}
		}
		return Collections.unmodifiableSet(startupFiles);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (!startupFiles.isEmpty() && transactionManager != null)
		{
			TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
			String currentFileName = null;
			try
			{
				if (checkPreconditions())
				{
					for (File startupFile : startupFiles)
					{
						currentFileName = startupFile.getName();
						importFile(startupFile);
					}
				}
				transactionManager.commit(transaction);
			}
			catch (Exception e)
			{
				if (!(e instanceof TransactionException) && !transaction.isCompleted())
					transactionManager.rollback(transaction);
				log.error("error importing file " + currentFileName, e);
			}
		}
	}

	public abstract boolean checkPreconditions();

	@Override
	public void importFile(File startupFile)
	{
		if (startupFile == null)
			throw new IllegalArgumentException();

		log.info("Importing " + startupFile);
		try
		{
			Document document = getDocument(startupFile);
			NodeList nodeList = document.getElementsByTagName(mainNodeName);
			Set<T> parsedObjects = parseMainNodes(nodeList);
			saveObjects(parsedObjects);
			log.info("imported " + parsedObjects.size() + " nodes of file " + startupFile);
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	Set<T> parseMainNodes(NodeList nodeList)
	{
		Set<T> parsedObjects = new HashSet<>();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			T parsedObject = parseMainNode((Element)nodeList.item(i));
			if (parsedObject != null)
				parsedObjects.add(parsedObject);
		}
		return parsedObjects;
	}

	protected abstract void saveObjects(Set<T> parsedObjects);

	protected abstract T parseMainNode(Element element);

	protected Document getDocument(File startupFile) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(startupFile);
	}

	protected String getTagValue(String tag, Element element)
	{
		try
		{
			return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
		}
		catch (NullPointerException e)
		{
			log.error("npe when searching for tag " + tag + " in element " + element);
			throw e;
		}
	}

	protected String getOptionalTagValue(String tag, Element element)
	{
		return element.getElementsByTagName(tag).getLength() <= 0 ? null : getTagValue(tag, element);
	}
}
