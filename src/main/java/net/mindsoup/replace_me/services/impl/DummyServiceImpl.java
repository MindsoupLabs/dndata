package net.mindsoup.replace_me.services.impl;

import net.mindsoup.replace_me.domain.DummyInput;
import net.mindsoup.replace_me.models.Dummy;
import net.mindsoup.replace_me.services.DummyService;
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
