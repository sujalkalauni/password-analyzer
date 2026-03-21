package com.passwordanalyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordAnalyzerTest {

    private PasswordAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new PasswordAnalyzer();
    }

    @Test
    void testVeryWeakPassword() {
        AnalysisResult result = analyzer.analyze("123456");
        assertTrue(result.getScore() <= 20, "Common password should score very low");
        assertTrue(result.isCommon(), "Should detect as common password");
    }

    @Test
    void testShortPassword() {
        AnalysisResult result = analyzer.analyze("Ab1!");
        assertTrue(result.getScore() < 60, "Short password should not score high");
    }

    @Test
    void testStrongPassword() {
        AnalysisResult result = analyzer.analyze("Tr0ub4dor&3!xK9#");
        assertTrue(result.getScore() >= 70, "Strong password should score high");
        assertTrue(result.isHasLower());
        assertTrue(result.isHasUpper());
        assertTrue(result.isHasDigit());
        assertTrue(result.isHasSpecial());
    }

    @Test
    void testOnlyLowercase() {
        AnalysisResult result = analyzer.analyze("alllowercase");
        assertFalse(result.isHasUpper());
        assertFalse(result.isHasDigit());
        assertFalse(result.isHasSpecial());
    }

    @Test
    void testRepeatingPattern() {
        AnalysisResult result = analyzer.analyze("abcabcabc");
        // Score should be penalized for repeating pattern
        assertTrue(result.getScore() < 60, "Repeating pattern should be penalized");
    }

    @Test
    void testEntropyIncreaseWithLength() {
        AnalysisResult short1 = analyzer.analyze("Ab1!efgh");
        AnalysisResult long1  = analyzer.analyze("Ab1!efghijklmnop");
        assertTrue(long1.getEntropy() > short1.getEntropy(),
            "Longer password should have higher entropy");
    }

    @Test
    void testCommonPasswordDetection() {
        String[] commons = {"password", "qwerty", "letmein", "monkey"};
        for (String pw : commons) {
            AnalysisResult result = analyzer.analyze(pw);
            assertTrue(result.isCommon(), pw + " should be detected as common");
        }
    }
}
