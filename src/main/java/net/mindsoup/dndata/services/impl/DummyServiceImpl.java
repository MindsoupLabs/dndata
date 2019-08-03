package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.domain.DummyInput;
import net.mindsoup.dndata.models.Dummy;
import net.mindsoup.dndata.services.DummyService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DummyServiceImpl implements DummyService {
	@Override
	public Dummy getDummy(DummyInput dummyInput) {
		Dummy dummy = new Dummy();
		dummy.setText(dummyInput.getText());
		return dummy;
	}
}
