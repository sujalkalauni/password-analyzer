package com.passwordanalyzer;

import java.util.List;

public class AnalysisResult {

    private final String password;
    private int score;
    private double entropy;
    private int length;
    private boolean hasLower, hasUpper, hasDigit, hasSpecial, isCommon;
    private List<String> feedback;
    private List<String> strengths;

    public AnalysisResult(String password) {
        this.password = password;
    }

    public void print() {
        System.out.println("┌─── ANALYSIS REPORT ─────────────────────────┐");
        System.out.printf("│  Length  : %d characters%n", length);
        System.out.printf("│  Entropy : %.1f bits%n", entropy);
        System.out.printf("│  Score   : %d/100%n", score);
        System.out.printf("│  Rating  : %s%n", getRating());
        System.out.println("├─── CHARACTER CHECKS ────────────────────────┤");
        System.out.printf("│  Lowercase  : %s%n", hasLower   ? "✓" : "✗");
        System.out.printf("│  Uppercase  : %s%n", hasUpper   ? "✓" : "✗");
        System.out.printf("│  Numbers    : %s%n", hasDigit   ? "✓" : "✗");
        System.out.printf("│  Special    : %s%n", hasSpecial ? "✓" : "✗");
        System.out.printf("│  Common pw  : %s%n", isCommon   ? "✗ YES (very bad!)" : "✓ No");

        if (!strengths.isEmpty()) {
            System.out.println("├─── STRENGTHS ───────────────────────────────┤");
            for (String s : strengths) {
                System.out.println("│  [+] " + s);
            }
        }

        if (!feedback.isEmpty()) {
            System.out.println("├─── SUGGESTIONS ─────────────────────────────┤");
            for (String f : feedback) {
                System.out.println("│  [-] " + f);
            }
        }

        System.out.println("│                                              │");
        printScoreBar();
        System.out.println("└──────────────────────────────────────────────┘");
    }

    private String getRating() {
        if (score >= 80) return "💪 STRONG";
        if (score >= 60) return "👍 MODERATE";
        if (score >= 40) return "⚠️  WEAK";
        return "🚨 VERY WEAK";
    }

    private void printScoreBar() {
        int filled = score / 5; // 20 chars = 100%
        String bar = "█".repeat(filled) + "░".repeat(20 - filled);
        String color;
        if (score >= 80) color = "STRONG";
        else if (score >= 60) color = "MODERATE";
        else if (score >= 40) color = "WEAK";
        else color = "VERY WEAK";

        System.out.printf("│  [%s] %d%%%n", bar, score);
    }

    // Getters and setters
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public double getEntropy() { return entropy; }
    public void setEntropy(double entropy) { this.entropy = entropy; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public boolean isHasLower() { return hasLower; }
    public void setHasLower(boolean hasLower) { this.hasLower = hasLower; }

    public boolean isHasUpper() { return hasUpper; }
    public void setHasUpper(boolean hasUpper) { this.hasUpper = hasUpper; }

    public boolean isHasDigit() { return hasDigit; }
    public void setHasDigit(boolean hasDigit) { this.hasDigit = hasDigit; }

    public boolean isHasSpecial() { return hasSpecial; }
    public void setHasSpecial(boolean hasSpecial) { this.hasSpecial = hasSpecial; }

    public boolean isCommon() { return isCommon; }
    public void setCommon(boolean common) { isCommon = common; }

    public List<String> getFeedback() { return feedback; }
    public void setFeedback(List<String> feedback) { this.feedback = feedback; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public String getPassword() { return password; }
}
