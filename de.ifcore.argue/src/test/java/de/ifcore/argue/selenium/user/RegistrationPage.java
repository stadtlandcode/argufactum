package de.ifcore.argue.selenium.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class RegistrationPage
{
	public static String path = "/registration/usernamePassword";

	@FindBy(id = "username")
	private WebElement usernameElement;

	@FindBy(id = "email")
	private WebElement emailElement;

	@FindBy(id = "password")
	private WebElement passwordElement;

	@FindBy(css = "#userRegistrationForm button")
	private WebElement submitButton;

	public static RegistrationPage open(WebDriver driver)
	{
		driver.get(SeleniumTest.urlFor(path));
		return PageFactory.initElements(driver, RegistrationPage.class);
	}

	public static String autoRegister(WebDriver driver)
	{
		String username = RandomStringUtils.randomAlphabetic(10);
		autoRegister(driver, username);
		return username;
	}

	public static void autoRegister(WebDriver driver, String username)
	{
		driver.manage().deleteAllCookies();
		RegistrationPage registrationPage = RegistrationPage.open(driver);
		registrationPage.register(username);
	}

	public void enterUsername(String username)
	{
		PageUtils.clearAndSend(usernameElement, username);
	}

	public void enterEmail(String email)
	{
		PageUtils.clearAndSend(emailElement, email);
	}

	public void enterPassword(String password)
	{
		PageUtils.clearAndSend(passwordElement, password);
	}

	public void submit()
	{
		submitButton.click();
	}

	public void register(String username)
	{
		register(username, username + "@localhost", username);
	}

	public void register(String username, String email, String password)
	{
		enterUsername(username);
		enterEmail(email);
		enterPassword(password);
		submit();
	}
}
