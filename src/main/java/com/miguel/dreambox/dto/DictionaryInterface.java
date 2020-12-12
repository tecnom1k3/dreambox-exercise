package com.miguel.dreambox.dto;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public interface DictionaryInterface {
    @PostConstruct
    void init() throws IOException;

    Boolean find(String word);

    List<String> suggest(String word);
}
