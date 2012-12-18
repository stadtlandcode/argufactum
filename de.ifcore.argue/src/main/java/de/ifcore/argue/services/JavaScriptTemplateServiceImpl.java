package de.ifcore.argue.services;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import de.ifcore.argue.domain.models.view.ViewModel;

@Service
public class JavaScriptTemplateServiceImpl implements JavaScriptTemplateService
{
	private boolean cacheEnabled;
	private final ConcurrentMap<Class<?>, Map<String, Object>> cache = new ConcurrentHashMap<>();

	@Override
	public String getTemplateString(String propertyName)
	{
		return "{{= " + propertyName + "}}";
	}

	@Override
	public Map<String, Object> getTemplateMap(Class<? extends ViewModel> clazz)
	{
		try
		{
			Map<String, Object> templateMap = cache.get(clazz);
			if (templateMap == null)
			{
				templateMap = createTemplateMap(clazz, "", new HashMap<String, Object>());
				if (cacheEnabled)
					cache.put(clazz, templateMap);
			}
			return templateMap;
		}
		catch (IntrospectionException e)
		{
			throw new RuntimeException(e);
		}
	}

	private Map<String, Object> createTemplateMap(Class<?> clazz, String nestedPath, Map<String, Object> templateMap)
			throws IntrospectionException
	{
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
		{
			if (propertyDescriptor.getReadMethod() == null || propertyDescriptor.getName().equals("class"))
				continue;
			boolean nested = ViewModel.class.isAssignableFrom(propertyDescriptor.getPropertyType());
			Object value;
			if (nested)
			{
				value = createTemplateMap(propertyDescriptor.getPropertyType(),
						nestedPath + propertyDescriptor.getName() + ".", new HashMap<String, Object>());
			}
			else
			{
				value = getTemplateString(nestedPath + propertyDescriptor.getName());
			}
			templateMap.put(propertyDescriptor.getName(), value);
		}
		return Collections.unmodifiableMap(templateMap);
	}

	public boolean isCacheEnabled()
	{
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled)
	{
		this.cacheEnabled = cacheEnabled;
	}
}
