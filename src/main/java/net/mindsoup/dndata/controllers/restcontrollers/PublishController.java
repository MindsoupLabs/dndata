package net.mindsoup.dndata.controllers.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Valentijn on 3-8-2019
 */
@RestController
@RequestMapping(value = "/publish", method = RequestMethod.POST)
public class PublishController extends ErrorController {
}
