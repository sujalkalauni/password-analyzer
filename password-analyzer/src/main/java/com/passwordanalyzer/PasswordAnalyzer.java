package com.passwordanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordAnalyzer {

    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern DIGITS    = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL   = Pattern.compile("[^a-zA-Z0-9]");

    // Most common passwords (subset for quick local check)
    private static final List<String> COMMON_PASSWORDS = List.of(
        "password", "123456", "12345678", "password1", "qwerty",
        "abc123", "111111", "letmein", "monkey", "dragon",
        "master", "sunshine", "princess", "welcome", "shadow",
        "superman", "michael", "football", "baseball", "iloveyou"
    );

    public AnalysisResult analyze(String password) {
        AnalysisResult result = new AnalysisResult(password);

        boolean hasLower   = LOWERCASE.matcher(password).find();
        boolean hasUpper   = UPPERCASE.matcher(password).find();
        boolean hasDigit   = DIGITS.matcher(password).find();
        boolean hasSpecial = SPECIAL.matcher(password).find();
        int length         = password.length();
        boolean isCommon   = COMMON_PASSWORDS.contains(password.toLowerCase());

        // Calculate score (0-100)
        int score = 0;
        List<String> feedback = new ArrayList<>();
        List<String> strengths = new ArrayList<>();

        // Length scoring
        if (length < 8) {
            feedback.add("Too short — use at least 8 characters");
        } else if (length < 12) {
            score += 20;
            feedback.add("Consider using 12+ characters for better security");
        } else if (length < 16) {
            score += 35;
            strengths.add("Good length (" + length + " chars)");
        } else {
            score += 50;
            strengths.add("Excellent length (" + length + " chars)");
        }

        // Character variety scoring
        if (hasLower) { score += 10; strengths.add("Contains lowercase letters"); }
        else           { feedback.add("Add lowercase letters (a-z)"); }

        if (hasUpper) { score += 10; strengths.add("Contains uppercase letters"); }
        else           { feedback.add("Add uppercase letters (A-Z)"); }

        if (hasDigit) { score += 10; strengths.add("Contains numbers"); }
        else           { feedback.add("Add numbers (0-9)"); }

        if (hasSpecial) { score += 20; strengths.add("Contains special characters"); }
        else             { feedback.add("Add special characters (!@#$%^&*)"); }

        // Entropy calculation
        int charsetSize = 0;
        if (hasLower)   charsetSize += 26;
        if (hasUpper)   charsetSize += 26;
        if (hasDigit)   charsetSize += 10;
        if (hasSpecial) charsetSize += 32;

        double entropy = charsetSize > 0
            ? length * (Math.log(charsetSize) / Math.log(2))
            : 0;

        // Common password penalty
        if (isCommon) {
            score = Math.min(score, 10);
            feedback.add("This is an extremely common password — never use it!");
        }

        // Repeated character penalty
        if (hasRepeatingPattern(password)) {
            score = (int)(score * 0.7);
            feedback.add("Avoid repeating patterns (aaa, 123123, etc.)");
        }

        result.setScore(Math.min(score, 100));
        result.setEntropy(entropy);
        result.setFeedback(feedback);
        result.setStrengths(strengths);
        result.setLength(length);
        result.setHasLower(hasLower);
        result.setHasUpper(hasUpper);
        result.setHasDigit(hasDigit);
        result.setHasSpecial(hasSpecial);
        result.setCommon(isCommon);

        return result;
    }

    private boolean hasRepeatingPattern(String password) {
        // Check for 3+ consecutive same chars
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1)
                    && password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        // Check for repeated substrings (e.g. "abcabc")
        int len = password.length();
        for (int size = 2; size <= len / 2; size++) {
            String sub = password.substring(0, size);
            if (password.replace(sub, "").isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
