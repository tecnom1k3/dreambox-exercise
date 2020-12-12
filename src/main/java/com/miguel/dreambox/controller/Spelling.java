package com.miguel.dreambox.controller;


import com.miguel.dreambox.dto.SpellingResponse;
import com.miguel.dreambox.service.SpellingServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Service
@RestController
public class Spelling {

    @Resource
    private SpellingServiceInterface spellingService;


    @RequestMapping(value = "/spelling/{word}", method = RequestMethod.GET, produces = "application/json")
    public SpellingResponse get(@PathVariable String word) {

        /*
        Controller for the 'spelling' endpoint.  only works with the HTTP GET method.
        Invokes SpellingService::check method to execute search logic
         */
        return spellingService.check(word);
    }
}
