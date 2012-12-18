package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockProContraTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopic;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.domain.enumerations.TopicThesis;

public class ProContraTopicTest
{
	@Test
	public void testCopy()
	{
		ProContraTopic source = mockProContraTopic();
		source.setThesesMessageCodeAppendix(TopicThesesMessageCodeAppendix.YES_NO);
		Argument firstArgument = new Argument(source, TopicThesis.PRO, "12345", mockAuthor(), 1, null);
		new Argument(source, TopicThesis.PRO, "123", mockAuthor(), 0, null);

		Topic destTopic = mockTopic();
		ProContraTopic dest = source.copyTo(destTopic, 0);
		assertEquals(destTopic, dest.getTopic());
		assertEquals(source.getThesesMessageCodeAppendix(), dest.getThesesMessageCodeAppendix());
		assertEquals(source.getArguments().size(), dest.getArguments().size());
		assertEquals(firstArgument.getText(), dest.getArguments().iterator().next().getText());
		assertEquals(0, dest.getArguments().iterator().next().getAuthorAttribution().getAuthorList().size());
		assertEquals(1, dest.getArguments().iterator().next().getAuthorAttribution().getExternalAuthorList().size());
	}
}
