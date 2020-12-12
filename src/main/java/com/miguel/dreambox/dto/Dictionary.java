package com.miguel.dreambox.dto;

import com.miguel.dreambox.utils.Hasher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Dictionary implements DictionaryInterface {

    private HashMap<Integer, LinkedList<String>> mainDictionary;
    private HashMap<Integer, String> sameVowels;
    private HashMap<Integer, String> dropVowels;
    private HashMap<Integer, String> consecutiveLetters;

    @Value("${dictionary.capacity}")
    private Integer capacity;

    @Override
    @PostConstruct
    public void init() throws IOException {

        /*
        Initializes the dictionary upon booting up the application.  this uses 4 dictionaries.
        Main dictionary - partitioned depending on the capacity defined in dictionary.capacity of the
                          application.properties file.  inside each partition is a linked list with all the words
                          sharing the same hash key. The contents are loaded from the dict.txt file.
        Same vowels - dictionary where all the words uses the vowel 'a' as the only vowel
        Drop vowels - dictionary where all the words have been stripped of their vowels
        Consecutive letters - dictionary where all consecutive letters have been reduced to only 1 letter
         */

        mainDictionary = new HashMap<>();
        sameVowels = new HashMap<>();
        dropVowels = new HashMap<>();
        consecutiveLetters = new HashMap<>();

        InputStream resource = new ClassPathResource("dict.txt").getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

        reader.lines().forEach(this::add);
    }

    private void add(String word) {
        /*
        method invoked for each line entry in the 'dict.txt' file.  the purpose is to populate all the dictionaries.
         */

        String lowerWord = word.toLowerCase();
        addToMainDictionary(lowerWord);
        addToSameVowelsDictionary(lowerWord);
        addToDropVowelsDictionary(lowerWord);
        addToConsecutiveLettersDictionary(lowerWord);
    }

    private void addToDropVowelsDictionary(String lowerWord) {
        /*
        call the drop vowels hasher and the word to the drop vowels dict.
         */
        putIntoDict(lowerWord, Hasher.getDropVowelsHash(lowerWord), dropVowels);
    }

    private void addToSameVowelsDictionary(String lowerWord) {
        /*
        call the same vowels hasher and the word to the same vowels dict.
         */
        putIntoDict(lowerWord, Hasher.getSameVowelsHash(lowerWord), sameVowels);
    }

    private void addToConsecutiveLettersDictionary(String lowerWord) {
        /*
        call the consecutive letters hasher and the word to the consecutive letters dict.
         */
        putIntoDict(lowerWord, Hasher.getConsecutiveLettersHash(lowerWord), consecutiveLetters);
    }

    private void putIntoDict(String word, Integer hash, HashMap<Integer, String> dict) {
        /*
        generic method to add a word with a given hash to a specific dictionary
         */
        if (!dict.containsKey(hash)) {
            dict.put(hash, word);
        }
    }

    private void addToMainDictionary(String lowerWord) {
        /*
        populate the main dictionary, according to the capacity limited hash, using linked lists to store the words that
        resolved to the same hash.  this helps on reducing the amount of items to search for to avoid going through the
        full word list in the worst scenarios where the word does not exists in the list, or is near the end of it.
         */
        Integer hash = Hasher.getCappedHash(capacity, lowerWord);
        if (mainDictionary.containsKey(hash)) {
            mainDictionary.get(hash).add(lowerWord);
        } else {
            LinkedList<String> list = new LinkedList<>();
            list.add(lowerWord);
            mainDictionary.put(hash, list);
        }
    }

    @Override
    public Boolean find(String word) {
        /*
        looks for the word in the main dictionary, looking only in the partition defined by the capacity limited hash
        operation that the current search word produces
         */
        String lowerWord = word.toLowerCase();
        Integer hash = Hasher.getCappedHash(capacity, lowerWord);
        return mainDictionary.containsKey(hash) && mainDictionary.get(hash).contains(lowerWord);
    }

    @Override
    public List<String> suggest(String word) {
        /*
        calculates the hashes according to each of the defined hasher algorithms, and searchs for a match in each of
        the suggestion dictionaries.  the idea of the suggestion dictionaries is to have collisions on hashes, which is
        key in order to look for suggestions on words not found in the main dictionary
         */
        String lowerWord = word.toLowerCase();
        List<String> suggestions = new ArrayList<>();

        Integer sameVowelsHash = Hasher.getSameVowelsHash(lowerWord);
        Integer dropVowelsHash = Hasher.getDropVowelsHash(lowerWord);
        Integer consecutiveLettersHash = Hasher.getConsecutiveLettersHash(lowerWord);

        if (sameVowels.containsKey(sameVowelsHash)) {
            suggestions.add(sameVowels.get(sameVowelsHash));
        }

        if (dropVowels.containsKey(dropVowelsHash)) {
            suggestions.add(dropVowels.get(dropVowelsHash));
        }

        if (consecutiveLetters.containsKey(consecutiveLettersHash)) {
            suggestions.add(consecutiveLetters.get(consecutiveLettersHash));
        }

        return suggestions.stream().distinct().sorted().collect(Collectors.toList());
    }

}
