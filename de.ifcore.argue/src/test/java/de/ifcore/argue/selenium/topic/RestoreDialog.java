package de.ifcore.argue.selenium.topic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RestoreDialog
{
	private final WebElement dialogElement;

	public RestoreDialog(WebElement element)
	{
		this.dialogElement = element;
	}

	public List<RestoreEntry> getEntries()
	{
		List<RestoreEntry> entries = new ArrayList<>();
		for (WebElement row : dialogElement.findElements(By.cssSelector("tbody tr")))
		{
			entries.add(new RestoreEntry(row));
		}
		return entries;
	}

	public RestoreEntry getEntry(String argumentId)
	{
		for (RestoreEntry entry : getEntries())
		{
			if (argumentId.equals(entry.getId()))
				return entry;
		}
		return null;
	}

	public void close()
	{
		dialogElement.findElement(By.cssSelector(".btn-success")).click();
	}

	public static class RestoreEntry
	{
		private final WebElement element;

		public RestoreEntry(WebElement element)
		{
			this.element = element;
		}

		public String getId()
		{
			return element.getAttribute("data-id");
		}

		public void clickRestoreButton()
		{
			element.findElement(By.tagName("button")).click();
		}
	}
}
