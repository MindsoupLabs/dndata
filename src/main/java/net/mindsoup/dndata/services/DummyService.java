package net.mindsoup.dndata.services;

import net.mindsoup.dndata.domain.DummyInput;
import net.mindsoup.dndata.models.Dummy;

/**
 *
 */
public interface DummyService {

	Dummy getDummy(DummyInput dummyInput);
}
