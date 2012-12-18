package de.ifcore.argue.selenium.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.selenium.AnyPage;
import de.ifcore.argue.selenium.SeleniumTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationTest extends SeleniumTest
{
	@Test
	public void testRegistration()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();
		RegistrationPage registrationPage = RegistrationPage.open(driver);
		registrationPage.register(RandomStringUtils.randomAlphabetic(15));

		assertFalse(driver.findElements(By.cssSelector(".alert-success")).isEmpty());
		assertEquals(urlFor("/"), driver.getCurrentUrl());
		assertTrue(AnyPage.isLoggedIn(driver));
	}

	@Test
	public void testRegistrationWhenLoggedIn()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		LoginPage.autoLogin(driver);
		RegistrationPage registrationPage = RegistrationPage.open(driver);
		registrationPage.register(RandomStringUtils.randomAlphabetic(15));

		assertTrue(driver.findElements(By.cssSelector(".alert-success")).isEmpty());
		assertEquals(urlFor("/"), driver.getCurrentUrl());
	}

	@Test
	public void testRegistrationOfTakenLogin()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();
		String randomName = RandomStringUtils.randomAlphabetic(15);

		RegistrationPage registrationPage = RegistrationPage.open(driver);
		registrationPage.register(randomName);
		assertTrue(driver.findElements(By.cssSelector(".alert-error")).isEmpty());

		driver.manage().deleteAllCookies();
		registrationPage = RegistrationPage.open(driver);
		registrationPage.register(randomName);
		assertFalse(driver.findElements(By.cssSelector(".alert-error")).isEmpty());
	}

	@Test
	public void testRegistrationWithSessionStoredPassword()
	{
		WebDriver driver = driverService.getSharedCleanedWebDriver();
		String randomName = RandomStringUtils.randomAlphabetic(15);

		RegistrationPage registrationPage = RegistrationPage.open(driver);
		registrationPage.register(randomName, "", randomName);
		assertFalse(driver.findElements(By.cssSelector(".alert-error")).isEmpty());

		registrationPage.register(randomName, randomName + "@localhost", "");
		assertTrue(driver.findElements(By.cssSelector(".alert-error")).isEmpty());
	}
}
