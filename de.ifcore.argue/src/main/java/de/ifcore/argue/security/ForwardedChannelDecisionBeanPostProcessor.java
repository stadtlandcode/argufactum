package de.ifcore.argue.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;
import org.springframework.security.web.access.channel.ChannelProcessor;

public class ForwardedChannelDecisionBeanPostProcessor implements BeanPostProcessor
{
	private static final Logger log = Logger.getLogger(ForwardedChannelDecisionBeanPostProcessor.class.getName());

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		if (bean instanceof ChannelDecisionManagerImpl)
		{
			log.debug("Post-processing " + beanName);
			List<ChannelProcessor> channelProcessors = Collections.unmodifiableList(Arrays.<ChannelProcessor> asList(
					new ForwardedInsecureChannelProcessor(), new ForwardedSecureChannelProcessor()));
			((ChannelDecisionManagerImpl)bean).setChannelProcessors(channelProcessors);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
		return bean;
	}
}
