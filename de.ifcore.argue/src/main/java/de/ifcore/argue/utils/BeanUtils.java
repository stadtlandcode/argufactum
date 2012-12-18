package de.ifcore.argue.utils;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

public class BeanUtils
{
	public static void setField(Object target, String name, Object value)
	{
		Field field = ReflectionUtils.findField(target.getClass(), name, null);
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, target, value);
	}
}
