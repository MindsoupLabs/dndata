package net.mindsoup.dndata.controllers;

import net.mindsoup.dndata.domain.DummyInput;
import net.mindsoup.dndata.models.Dummy;
import net.mindsoup.dndata.services.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping(value = "/data", method = RequestMethod.POST)
public class DataController extends ErrorController {

	@Autowired
	private DummyService dummyService;

    @RequestMapping("/dummy")
    public Dummy test(@RequestBody DummyInput dummyInput) {
        return dummyService.getDummy(dummyInput);
    }
}
