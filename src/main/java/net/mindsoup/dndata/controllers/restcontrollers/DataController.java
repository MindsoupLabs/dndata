package net.mindsoup.dndata.controllers.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping(value = "/data", method = RequestMethod.POST)
public class DataController extends ErrorController {

}
