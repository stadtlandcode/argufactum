package de.ifcore.argue.selenium;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class SeleniumDriverService implements DisposableBean, InitializingBean
{
	private Integer port;
	private String url;
	private String chromeDriverLocation;
	private Boolean sharedLoginProvided;

	private final Set<WebDriver> openDrivers = new HashSet<>();
	private WebDriver sharedDriver = null;

	@Override
	public void afterPropertiesSet() throws Exception
	{
		System.setProperty("seleniumBaseUrl", url + ":" + port);
		if (chromeDriverLocation != null)
			System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		System.setProperty("seleniumSharedLoginProvided", BooleanUtils.toStringTrueFalse(sharedLoginProvided));
	}

	@Override
	public void destroy()
	{
		for (WebDriver driver : openDrivers)
		{
			driver.quit();
		}
		sharedDriver = null;
	}

	public WebDriver getFreshWebDriver()
	{
		WebDriver freshDriver = new ChromeDriver();
		freshDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		openDrivers.add(freshDriver);
		if (sharedDriver == null)
			sharedDriver = freshDriver;
		return freshDriver;
	}

	public WebDriver getSharedWebDriver()
	{
		if (sharedDriver == null)
			sharedDriver = getFreshWebDriver();
		return sharedDriver;
	}

	public WebDriver getSharedCleanedWebDriver()
	{
		WebDriver webDriver = getSharedWebDriver();
		webDriver.manage().deleteAllCookies();
		return webDriver;
	}

	public void quitWebDriver(WebDriver driver)
	{
		openDrivers.remove(driver);
		driver.quit();
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getChromeDriverLocation()
	{
		return chromeDriverLocation;
	}

	public void setChromeDriverLocation(String chromeDriverLocation)
	{
		this.chromeDriverLocation = chromeDriverLocation;
	}

	public Boolean getSharedLoginProvided()
	{
		return sharedLoginProvided;
	}

	public void setSharedLoginProvided(Boolean sharedLoginProvided)
	{
		this.sharedLoginProvided = sharedLoginProvided;
	}

}
