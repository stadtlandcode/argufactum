package de.ifcore.argue.domain.convert;

import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.google.inject.internal.ImmutableSet;

public class StringTrimConverter implements GenericConverter
{
	private static final ImmutableSet<ConvertiblePair> convertibleTypes = ImmutableSet.of(new ConvertiblePair(
			String.class, String.class));

	@Override
	public Set<ConvertiblePair> getConvertibleTypes()
	{
		return convertibleTypes;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		String trimmedString = source == null ? null : ((String)source).trim();
		return "".equals(trimmedString) ? null : trimmedString;
	}
}
