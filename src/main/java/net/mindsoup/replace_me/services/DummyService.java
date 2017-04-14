package net.mindsoup.replace_me.services;

import net.mindsoup.replace_me.domain.DummyInput;
import net.mindsoup.replace_me.models.Dummy;

/**
 *
 */
public interface DummyService {

	Dummy getDummy(DummyInput dummyInput);
}
