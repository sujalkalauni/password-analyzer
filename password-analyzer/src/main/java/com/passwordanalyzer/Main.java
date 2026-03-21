package com.passwordanalyzer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     Password Strength Analyzer v1.0      ║");
        System.out.println("║        + HaveIBeenPwned Checker           ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        PasswordAnalyzer analyzer = new PasswordAnalyzer();
        BreachChecker breachChecker = new BreachChecker();

        while (true) {
            System.out.print("Enter password to analyze (or 'quit' to exit): ");
            String password = scanner.nextLine().trim();

            if (password.equalsIgnoreCase("quit")) {
                System.out.println("Exiting. Stay secure!");
                break;
            }

            if (password.isEmpty()) {
                System.out.println("[!] Password cannot be empty.\n");
                continue;
            }

            System.out.println();

            // Strength analysis
            AnalysisResult result = analyzer.analyze(password);
            result.print();

            // Breach check
            System.out.println("\n[*] Checking HaveIBeenPwned database...");
            try {
                int breachCount = breachChecker.checkBreach(password);
                if (breachCount == 0) {
                    System.out.println("[✓] Good news! This password was NOT found in any known data breach.");
                } else {
                    System.out.printf("[✗] DANGER! This password appeared in %,d data breach(es).%n", breachCount);
                    System.out.println("    You should NEVER use this password.");
                }
            } catch (Exception e) {
                System.out.println("[!] Could not reach HIBP API: " + e.getMessage());
                System.out.println("    (Check your internet connection)");
            }

            System.out.println("\n" + "─".repeat(50) + "\n");
        }

        scanner.close();
    }
}
