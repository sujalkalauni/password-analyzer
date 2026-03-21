# 🔐 Password Strength Analyzer & Breach Checker

A Java CLI tool that analyzes password strength using entropy-based scoring and checks if your password has appeared in real-world data breaches using the **HaveIBeenPwned API** — with full **k-anonymity** protection.

## Features

- **Entropy-based strength scoring** (0–100) based on length, character variety, and patterns
- **K-anonymity breach checking** — only the first 5 chars of your SHA-1 hash are sent to the API; your actual password never leaves your machine
- **Common password detection** against a local blocklist
- **Repeating pattern detection** (e.g., `aaa`, `abcabc`)
- **Actionable feedback** — tells you exactly what to improve
- Checks against **800M+ breached passwords** from HIBP's database

## How K-Anonymity Works

```
Your password  →  SHA-1 hash  →  first 5 chars sent to API
                                  └─ API returns all hashes with that prefix
                                     └─ We check locally if our full hash matches
```

Your password (or its full hash) is **never transmitted**. This is the same technique used by browsers like Firefox and Chrome for breach detection.

## Tech Stack

- Java 17
- `java.net.HttpURLConnection` for HTTP (no external dependencies)
- `java.security.MessageDigest` for SHA-1 hashing
- JUnit 5 for tests
- Maven for build

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Build & Run

```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/password-analyzer.git
cd password-analyzer

# Build
mvn clean package

# Run
java -jar target/password-analyzer.jar
```

### Run Tests

```bash
mvn test
```

## Sample Output

```
╔══════════════════════════════════════════╗
║     Password Strength Analyzer v1.0      ║
║        + HaveIBeenPwned Checker           ║
╚══════════════════════════════════════════╝

Enter password to analyze: Tr0ub4dor&3!

┌─── ANALYSIS REPORT ─────────────────────────┐
│  Length  : 12 characters
│  Entropy : 78.8 bits
│  Score   : 95/100
│  Rating  : 💪 STRONG
├─── CHARACTER CHECKS ────────────────────────┤
│  Lowercase  : ✓
│  Uppercase  : ✓
│  Numbers    : ✓
│  Special    : ✓
│  Common pw  : ✓ No
├─── STRENGTHS ───────────────────────────────┤
│  [+] Good length (12 chars)
│  [+] Contains lowercase letters
│  [+] Contains uppercase letters
│  [+] Contains numbers
│  [+] Contains special characters
│  [██████████████████░░] 95%
└──────────────────────────────────────────────┘

[*] Checking HaveIBeenPwned database...
[✓] Good news! This password was NOT found in any known data breach.
```

## Project Structure

```
src/
├── main/java/com/passwordanalyzer/
│   ├── Main.java             # CLI entry point
│   ├── PasswordAnalyzer.java # Scoring & strength logic
│   ├── BreachChecker.java    # HIBP API + k-anonymity
│   └── AnalysisResult.java   # Result model
└── test/java/com/passwordanalyzer/
    └── PasswordAnalyzerTest.java
```

## Security Note

This tool is for **educational purposes**. Never type passwords you actually use into any tool — even this one. Use a password manager like Bitwarden or 1Password.

## References

- [HaveIBeenPwned API](https://haveibeenpwned.com/API/v3#PwnedPasswords)
- [K-Anonymity in HIBP](https://www.troyhunt.com/ive-just-launched-pwned-passwords-version-2/)
- [Password Entropy](https://en.wikipedia.org/wiki/Password_strength#Entropy_as_a_measure_of_password_strength)
