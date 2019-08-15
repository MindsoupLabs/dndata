package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.repositories.PublishDataRepository;
import net.mindsoup.dndata.services.PublishDataService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Valentijn on 15-8-2019
 */
@Service
public class PublishDataServiceImpl implements PublishDataService {

	private PublishDataRepository publishDataRepository;

	public PublishDataServiceImpl(PublishDataRepository repository) {
		this.publishDataRepository = repository;
	}

	@Override
	public PublishData getMostRecentPublishDataForBook(Book book) {
		return publishDataRepository.findFirstByBookIdOrderByIdDesc(book.getId()).orElse(null);
	}

	@Override
	public PublishData getMostRecentPublishDataForType(ObjectType type) {
		return publishDataRepository.findFirstByNameOrderByIdDesc(type.name()).orElse(null);
	}

	@Override
	public void save(PublishData publishData) {
		publishDataRepository.save(publishData);
	}
}
