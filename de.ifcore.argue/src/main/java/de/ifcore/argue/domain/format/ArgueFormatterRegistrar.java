package de.ifcore.argue.domain.format;

import org.springframework.context.MessageSource;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicThesis;

public class ArgueFormatterRegistrar implements FormatterRegistrar
{
	private final MessageSource messageSource;

	public ArgueFormatterRegistrar(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	@Override
	public void registerFormatters(FormatterRegistry registry)
	{
		registry.addFormatterForFieldType(DiscussionType.class, new EnumNameFormatter(messageSource,
				DiscussionType.class));
		registry.addFormatterForFieldType(TopicThesis.class, new EnumNameFormatter(messageSource, TopicThesis.class));
	}
}
