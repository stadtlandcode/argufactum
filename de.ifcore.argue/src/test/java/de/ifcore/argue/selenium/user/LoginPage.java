package de.ifcore.argue.selenium.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.ifcore.argue.selenium.AnyPage;
import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class LoginPage
{
	private static final String path = "/login";

	private static boolean registeredSharedLogin = false;
	public static final String sharedLogin = "seleniumtest";

	@FindBy(id = "login-username")
	private WebElement usernameElement;

	@FindBy(id = "login-password")
	private WebElement passwordElement;

	@FindBy(css = "form:first-child button.btn-success")
	private WebElement submitButton;

	public static LoginPage open(WebDriver driver)
	{
		driver.get(SeleniumTest.urlFor(path));
		return PageFactory.initElements(driver, LoginPage.class);
	}

	public static void autoLogin(WebDriver driver)
	{
		if (!AnyPage.isLoggedIn(driver))
		{
			registerSharedLoginIfNecessary(driver);
		}
		if (!AnyPage.isLoggedIn(driver))
		{
			LoginPage loginPage = LoginPage.open(driver);
			loginPage.login(sharedLogin, sharedLogin);
		}
	}

	public static void registerSharedLoginIfNecessary(WebDriver driver)
	{
		if (!registeredSharedLogin && "false".equals(System.getProperty("seleniumSharedLoginProvided")))
		{
			RegistrationPage.autoRegister(driver, sharedLogin);
			registeredSharedLogin = true;
		}
	}

	public void enterUsername(String username)
	{
		PageUtils.clearAndSend(usernameElement, username);
	}

	public void enterPassword(String password)
	{
		PageUtils.clearAndSend(passwordElement, password);
	}

	public void submit()
	{
		submitButton.click();
	}

	public void login(String username, String password)
	{
		enterUsername(username);
		enterPassword(password);
		submit();
	}
}
