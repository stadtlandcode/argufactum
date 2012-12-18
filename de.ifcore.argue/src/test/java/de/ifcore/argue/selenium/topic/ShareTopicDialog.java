package de.ifcore.argue.selenium.topic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ShareTopicDialog
{
	private final WebElement dialogElement;
	private final WebElement visibilityForm;
	private final WebElement addAccessRightForm;

	public ShareTopicDialog(WebElement element)
	{
		this.dialogElement = element;
		this.visibilityForm = dialogElement.findElement(By.id("set-visibility-form"));
		this.addAccessRightForm = dialogElement.findElement(By.id("add-access-right-form"));
	}

	public WebElement getVisibilityCheckbox()
	{
		return visibilityForm.findElement(By.name("visibility"));
	}

	public void submitVisibilityForm()
	{
		visibilityForm.findElement(By.tagName("button")).click();
	}

	public WebElement getContactInputElement()
	{
		return addAccessRightForm.findElement(By.name("contact"));
	}

	public Select getPermissionSelect()
	{
		return new Select(addAccessRightForm.findElement(By.name("permission")));
	}

	public void submitAddAccessRightForm()
	{
		addAccessRightForm.findElement(By.tagName("button")).click();
	}

	public List<ContactEntry> getEntries()
	{
		List<ContactEntry> entries = new ArrayList<>();
		for (WebElement row : dialogElement.findElements(By.cssSelector(".access-rights-list tbody tr")))
		{
			entries.add(new ContactEntry(row));
		}
		return entries;
	}

	public ContactEntry getEntry(String contactName)
	{
		for (ContactEntry entry : getEntries())
		{
			if (entry.getContactName().contains(contactName))
				return entry;
		}
		return null;
	}

	public void close()
	{
		dialogElement.findElement(By.cssSelector(".modal-footer button")).click();
	}

	public static class ContactEntry
	{
		private final WebElement element;

		public ContactEntry(WebElement element)
		{
			this.element = element;
		}

		public String getContactName()
		{
			return element.findElement(By.cssSelector("td:first-child")).getText();
		}

		public String getAccessRight()
		{
			return element.findElement(By.cssSelector("td:last-child")).getText();
		}

		public boolean hasDeleteButton()
		{
			return !element.findElements(By.cssSelector(".icon-trash")).isEmpty();
		}

		public void clickDeleteButton()
		{
			element.findElement(By.cssSelector(".icon-trash")).click();
		}
	}
}
