package de.ifcore.argue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.JsonStringDao;
import de.ifcore.argue.domain.entities.JsonString;

@Service
public class JsonStorageServiceImpl implements JsonStorageService
{
	@Autowired
	private JsonStringDao dao;

	@Override
	@Transactional(readOnly = true)
	public JsonString get(String base36Id)
	{
		long id = Long.parseLong(base36Id, 36);
		JsonString jsonString = dao.get(id);
		return jsonString;
	}

	@Override
	@Transactional(readOnly = false)
	public String store(String json)
	{
		JsonString jsonString = new JsonString(json);
		dao.save(jsonString);
		String base36Id = Long.toString(jsonString.getId(), 36);
		return base36Id;
	}
}
