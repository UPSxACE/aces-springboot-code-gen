package com.upsxace.aces_springboot_code_gen.lib.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class with few custom utilities to handle strings
 */
public class StringUtils {
    /**
     * Removes line breaks, tabs with space, multiple spaces, and leading/trailing spaces.
     * @param input string to inline
     * @return inlined text
     */
    public static String inline(String input){
        return input
                .replaceAll("[\\n\\r\\t]", " ")      // Replace line breaks and tabs with space
                .replaceAll("\\s{2,}", " ")          // Replace multiple spaces with a single space
                .trim();                                              // Remove leading and trailing spaces
    }

    /**
     * Returns the substring of {@code input} that appears before the first occurrence
     * of the specified {@code delimiter}.
     * @param input string to process
     * @param delimiter delimiter to search for
     * @return resulting substring
     */
    public static String removeAllAfter(String input, String delimiter){
        int index = input.indexOf(delimiter);
        return (index != -1) ? input.substring(0, index) : input;
    }

    private static String[] extractWords(String input){
        var parts = input.split("[\\s_\\-]+"); // handles spaces, snake_case and kebab-case

        if (parts.length == 1) {
            // If no separators found, split by uppercase boundaries (handles camelCase and Pascal case)
            parts = input.split("(?=[A-Z])");
        }

        return parts;
    }

    /**
     * Returns the given string in pascal case.
     * @param input string to process
     * @return string in pascal case
     */
    public static String toPascalCase(String input) {
        String[] words = extractWords(input);

        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .reduce("", String::concat);
    }

    /**
     * Returns the given string in camel case.
     * @param input string to process
     * @return string in camel case
     */
    public static String toCamelCase(String input) {
        String[] words = extractWords(input);

        if (words.length == 0) {
            return "";
        }

        String firstWord = words[0].toLowerCase();

        return Arrays.stream(Arrays.copyOfRange(words, 1, words.length))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .reduce(firstWord, String::concat);
    }

    /**
     * Splits string by commas, but ignore commas nested into parentheses
     * @param input string to split
     * @return list of after the split
     */
    public static List<String> splitIgnoringParentheses(String input) {
        var result = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        var parenthesesLevel = 0;

        for (char c : input.toCharArray()) {
            if (c == ',' && parenthesesLevel == 0) {
                // Comma outside parentheses, split
                result.add(current.toString().trim());
                current.setLength(0); // Clear current buffer
            } else {
                // Track parentheses level
                if (c == '(') {
                    parenthesesLevel++;
                } else if (c == ')') {
                    if (parenthesesLevel > 0) parenthesesLevel--;
                }
                current.append(c);
            }
        }

        // Add the last segment
        if (!current.isEmpty()) {
            result.add(current.toString().trim());
        }

        return result;
    }
}
