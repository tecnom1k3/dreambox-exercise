package com.miguel.dreambox.service;

import com.miguel.dreambox.dto.SpellingResponse;
import com.miguel.dreambox.exception.WordNotFoundException;

public interface SpellingServiceInterface {
    SpellingResponse check(String word) throws WordNotFoundException;
}
