package de.ifcore.argue.selenium.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.selenium.AnyPage;
import de.ifcore.argue.selenium.SeleniumTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginTest extends SeleniumTest
{
	@Before
	public void registerIfNecessary()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();
		LoginPage.registerSharedLoginIfNecessary(driver);
	}

	@Test
	public void testUsernameLogin()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();

		LoginPage loginPage = LoginPage.open(driver);
		loginPage.login(LoginPage.sharedLogin, LoginPage.sharedLogin);

		assertTrue(AnyPage.isLoggedIn(driver));
		assertEquals(urlFor("/"), driver.getCurrentUrl());
	}

	@Test
	public void testEmailLogin()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();

		LoginPage loginPage = LoginPage.open(driver);
		loginPage.login(LoginPage.sharedLogin, LoginPage.sharedLogin);

		assertTrue(AnyPage.isLoggedIn(driver));
		assertEquals(urlFor("/"), driver.getCurrentUrl());
	}
}
