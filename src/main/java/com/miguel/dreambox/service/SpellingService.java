package com.miguel.dreambox.service;

import com.miguel.dreambox.dto.DictionaryInterface;
import com.miguel.dreambox.dto.SpellingResponse;
import com.miguel.dreambox.exception.WordNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpellingService implements SpellingServiceInterface {

    @Resource
    private DictionaryInterface dictionary;

    @Override
    public SpellingResponse check(String word) throws WordNotFoundException {

        /*
        Searches for the word in the dictionary.  builds the response accordingly.  if the word is not found in the
        dictionary, and there are no suggestions, it throws a WordNotFound exception
         */

        SpellingResponse.SpellingResponseBuilder builder = SpellingResponse.builder();
        List<String> suggestionList;

        Boolean found = dictionary.find(word);
        builder.correct(found);

        if (!found) {
            suggestionList = dictionary.suggest(word);

            if (suggestionList.isEmpty()) {
                throw new WordNotFoundException();
            } else {
                builder.suggestions(suggestionList);
            }
        }

        return builder.build();
    }

}
