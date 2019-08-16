package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.repositories.PublishDataRepository;
import net.mindsoup.dndata.services.PublishDataService;
import org.springframework.stereotype.Service;

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
		return publishDataRepository.findFirstByBookIdAndGameOrderByIdDesc(book.getId(), book.getGame()).orElse(null);
	}

	@Override
	public PublishData getMostRecentPublishDataForName(Game game, String name) {
		return publishDataRepository.findFirstByNameAndGameOrderByIdDesc(name, game).orElse(null);
	}

	@Override
	public void save(PublishData publishData) {
		publishDataRepository.save(publishData);
	}
}
