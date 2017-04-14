package net.mindsoup.replace_me.controllers;

import net.mindsoup.replace_me.domain.DummyInput;
import net.mindsoup.replace_me.models.Dummy;
import net.mindsoup.replace_me.services.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping(value = "/test", method = RequestMethod.POST)
public class DummyController {

	@Autowired
	private DummyService dummyService;

    @RequestMapping("/dummy")
    public Dummy test(@RequestBody DummyInput dummyInput) {
        return dummyService.getDummy(dummyInput);
    }
}
