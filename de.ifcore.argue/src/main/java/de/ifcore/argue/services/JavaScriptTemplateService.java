package de.ifcore.argue.services;

import java.util.Map;

import de.ifcore.argue.domain.models.view.ViewModel;

public interface JavaScriptTemplateService
{
	public Map<String, Object> getTemplateMap(Class<? extends ViewModel> clazz);

	public String getTemplateString(String propertyName);
}
