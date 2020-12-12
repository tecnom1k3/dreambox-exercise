package com.miguel.dreambox.utils;

import java.util.regex.Pattern;

public class Hasher {

    public static Integer getCappedHash(Integer capacity, String word) {
        /*
        returns a capacity limited hash of a word
         */
        return Math.abs(word.hashCode()) % capacity;
    }

    public static Integer getSameVowelsHash(String word) {
        /*
        returns the hash of a word which vowels have been replaced to 'a'
         */
        Pattern pattern = Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE);
        return Math.abs(pattern.matcher(word).replaceAll("a").hashCode());
    }

    public static Integer getDropVowelsHash(String word) {
        /*
        returns the hash of a word which vowels have been removed
         */
        Pattern pattern = Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE);
        return Math.abs(pattern.matcher(word).replaceAll("").hashCode());
    }

    public static Integer getConsecutiveLettersHash(String word) {
        /*
        returns the hash of a word which consecutive characters have been reduced to a single one.
         */
        Pattern pattern = Pattern.compile("([a-z])\\1+", Pattern.CASE_INSENSITIVE);
        return Math.abs(pattern.matcher(word).replaceAll("$1").hashCode());
    }
}
