package de.ifcore.argue.selenium.topic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HistoryDialog
{
	private final WebElement dialogElement;

	public HistoryDialog(WebElement element)
	{
		this.dialogElement = element;
	}

	public List<HistoryEntry> getEntries()
	{
		List<HistoryEntry> entries = new ArrayList<>();
		for (WebElement row : dialogElement.findElements(By.cssSelector("tbody tr")))
		{
			entries.add(new HistoryEntry(row));
		}
		return entries;
	}

	public HistoryEntry getEntry(String text)
	{
		for (HistoryEntry entry : getEntries())
		{
			if (text.equals(entry.getText()))
				return entry;
		}
		return null;
	}

	public void close()
	{
		dialogElement.findElement(By.cssSelector(".btn-success")).click();
	}

	public static class HistoryEntry
	{
		private final WebElement element;

		public HistoryEntry(WebElement element)
		{
			this.element = element;
		}

		public String getText()
		{
			return element.findElement(By.cssSelector("td:first-child span")).getText();
		}

		public String getDefinition()
		{
			List<WebElement> elements = element.findElements(By.cssSelector(".definition"));
			return elements.isEmpty() ? null : elements.iterator().next().getText();
		}

		public void clickRestoreButton()
		{
			element.findElement(By.tagName("button")).click();
		}
	}
}
