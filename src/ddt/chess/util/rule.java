package ddt.chess.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Rule {
    private ArrayList<String> rule;

    public Rule() {
        rule = new ArrayList<>();
        readRulesFromResource();
    }

    private void readRulesFromResource() {
        try (
                InputStream is = getClass().getResourceAsStream("/ddt/chess/util/rule.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                rule.add(line);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Không thể đọc file: " + e.getMessage());
        }
    }

    public void printRules() {
        for (String line : rule) {
            System.out.println(line);
        }
    }
}
