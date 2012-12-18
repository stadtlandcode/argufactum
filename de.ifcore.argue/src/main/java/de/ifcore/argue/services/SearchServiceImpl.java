package de.ifcore.argue.services;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import de.ifcore.argue.dao.SearchDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.form.SearchForm;
import de.ifcore.argue.domain.models.view.SearchResultModel;

@Service
public class SearchServiceImpl implements SearchService
{
	private static final Logger log = Logger.getLogger(SearchServiceImpl.class.getName());

	private final SearchDao searchDao;
	private final MessageSource messageSource;

	@Inject
	public SearchServiceImpl(SearchDao searchDao, MessageSource messageSource)
	{
		this.searchDao = searchDao;
		this.messageSource = messageSource;
	}

	@Override
	public SearchResultModel search(SearchForm form)
	{
		List<Topic> topics = searchDao.searchTopics(form.getSearchTerm());
		log.info("search for " + form.getSearchTerm());
		return new SearchResultModel(form.getSearchTerm(), topics, messageSource);
	}
}
