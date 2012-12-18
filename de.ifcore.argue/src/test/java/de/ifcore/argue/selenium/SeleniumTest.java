package de.ifcore.argue.selenium;

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "/seleniumContext.xml" })
@ProfileValueSourceConfiguration(SeleniumProfileValueSource.class)
@IfProfileValue(name = "runSeleniumTests", value = "true")
public abstract class SeleniumTest
{
	private static final Logger log = Logger.getLogger(SeleniumTest.class.getName());
	private static Boolean appRunning;

	@Autowired
	protected SeleniumDriverService driverService;

	@Before
	public void assumeThatAppIsRunning()
	{
		if (appRunning == null)
		{
			log.debug("check whether app is running");
			ServerSocket socket = null;
			try
			{
				socket = new ServerSocket(driverService.getPort().intValue());
				appRunning = Boolean.FALSE;
				log.debug("app is not running, opened socket");
			}
			catch (IOException e)
			{
				appRunning = Boolean.TRUE;
				log.debug("app is running, starting tests ...");
			}
			finally
			{
				if (socket != null)
				{
					try
					{
						socket.close();
						log.debug("socket closed");
					}
					catch (IOException e)
					{
					}
				}
			}
		}

		assumeTrue(Boolean.TRUE.equals(appRunning));
	}

	protected void selectAnotherOption(Select select)
	{
		for (WebElement webElement : select.getOptions())
		{
			if (!webElement.isSelected())
			{
				webElement.click();
				break;
			}
		}
	}

	public static void waitForAjax(WebDriver driver)
	{
		while (true)
		{
			Boolean ajaxIsComplete = (Boolean)((JavascriptExecutor)driver).executeScript("return jQuery.active == 0");
			if (Boolean.TRUE.equals(ajaxIsComplete))
				break;
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void wait(WebDriver driver)
	{
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static String urlFor(String absolutePath)
	{
		if (!absolutePath.startsWith("/"))
			throw new IllegalArgumentException();
		return System.getProperty("seleniumBaseUrl") + absolutePath;
	}
}
