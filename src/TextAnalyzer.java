
// I worked on this project alone with the help of the Java API

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * The Class TextAnalyzer contains methods to analyze text files.
 *
 * @author Abhishek
 * @version 1.0
 */
public class TextAnalyzer {

    private static ArrayList<String> abbreviations = new ArrayList<String>();
    private static boolean           loaded        = false;

    /**
     * Total count of words and sentences in the text file.
     *
     * @param srcFileName
     *            the src file name
     * @return the int[]
     * @throws FileNotFoundException
     *             the file not found exception
     */

    public static int[] totalCount(String srcFileName)
            throws FileNotFoundException {

        if (!loaded) {
            loadAbbreviations();
        }

        File srcFile = new File(srcFileName);
        Scanner scan;
        int totalWords = 0;
        int totalSentences = 0;

        scan = new Scanner(srcFile);
        while (scan.hasNext()) {
            scan.next();
            totalWords++;
        }
        scan.close();

        scan = new Scanner(srcFile);
        while (scan.hasNext()) {
            String str = scan.next();
            if (str.startsWith("(") && !str.endsWith(")")) {
                do {
                    str = scan.next();
                } while (!str.contains(")"));
            } else if (str.startsWith("\"") && !str.endsWith("\"")) {
                do {
                    str = scan.next();
                } while (!str.contains("\""));
            } else if (!isAbbreviation(
                    str.replaceAll("[^A-Za-z0-9]+", "").toLowerCase())) {
                char c = str.charAt(str.length() - 1);
                if (c == '.' || c == '?' || c == '!') {
                    totalSentences++;
                }
            }
        }
        scan.close();

        return new int[] {totalWords, totalSentences};
    }

    /**
     * Returns the longest sentence in the text file.
     *
     * @param srcFileName
     *            the src file name
     * @return the string that is the longest sentence
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public static String longestSentence(String srcFileName)
            throws FileNotFoundException {

        if (!loaded) {
            loadAbbreviations();
        }

        String longestSentence = "", currentSentence = "";
        int longestLength = 0, currentLength = 0;

        File srcFile = new File(srcFileName);
        Scanner scan = new Scanner(srcFile);

        while (scan.hasNext()) {
            String str = scan.next();
            char c = str.charAt(str.length() - 1);

            if (c == '.' || c == '?' || c == '!') {
                if ((currentLength + 1) > longestLength) {
                    longestSentence = currentSentence + str;
                    longestLength = currentLength + 1;
                }
                currentSentence = "";
                currentLength = 0;
            } else {
                currentSentence += str + " ";
                currentLength++;
            }
        }
        scan.close();
        return longestSentence;
    }

    /**
     * Target word count. Writes the target words counts to a text file
     *
     * @param srcFileName
     *            the src file name
     * @param targetFileName
     *            the target file name
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public static void targetWordCount(String srcFileName,
            String targetFileName) throws FileNotFoundException {

        if (!loaded) {
            loadAbbreviations();
        }

        File srcFile = new File(srcFileName);
        File targetFile = new File(targetFileName);

        PrintWriter writer = new PrintWriter(new File("targetCount.txt"));
        Scanner scan;
        Map<String, Integer> targetWordsMap =
                new LinkedHashMap<String, Integer>();

        // setting up the map of target words and their counts (0)
        scan = new Scanner(targetFile);
        while (scan.hasNextLine()) {
            targetWordsMap.put(scan.nextLine().toLowerCase(), 0);
        }
        scan.close();

        // finding the number of instances of each key
        scan = new Scanner(srcFile);
        while (scan.hasNext()) {
            String str =
                    scan.next().replaceAll("[^A-Za-z0-9]+", "").toLowerCase();
            if (targetWordsMap.containsKey(str)) {
                targetWordsMap.put(str, targetWordsMap.get(str) + 1);
            }
        }
        scan.close();

        // writing the Map to "targetCount.txt"
        for (Entry<String, Integer> e : targetWordsMap.entrySet()) {
            String str = e.getKey() + ": " + e.getValue();
            writer.println(str);
        }
        writer.close();

    }

    /**
     * Sentiment score. Calculates the score based on good and bad words listed
     * in the text file
     *
     * @param srcFileName
     *            the src file name
     * @param goodFileName
     *            the good file name
     * @param badFileName
     *            the bad file name
     * @return the score
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public static int sentimentScore(String srcFileName, String goodFileName,
            String badFileName) throws FileNotFoundException {

        if (!loaded) {
            loadAbbreviations();
        }

        File srcFile = new File(srcFileName);
        File goodFile = new File(goodFileName);
        File badFile = new File(badFileName);

        int score = 0;
        ArrayList<String> goodWords = new ArrayList<String>();
        ArrayList<String> badWords = new ArrayList<String>();
        Scanner scan;

        // Creating array list of the good words
        scan = new Scanner(goodFile);
        while (scan.hasNext()) {
            goodWords.add(scan.next());
        }
        scan.close();

        // Creating array list of the bad words
        scan = new Scanner(badFile);
        while (scan.hasNext()) {
            badWords.add(scan.next());
        }
        scan.close();

        scan = new Scanner(srcFile);
        while (scan.hasNext()) {
            String str =
                    scan.next().replaceAll("[^A-Za-z0-9]+", "").toLowerCase();

            if (goodWords.contains(str)) {
                score++;
            } else if (badWords.contains(str)) {
                score--;
            }
        }
        scan.close();

        return score;
    }

    /**
     * Load abbreviations.
     */
    private static void loadAbbreviations() {
        Scanner s;
        File abb1 = new File("Abbreviations1.txt");
        File abb2 = new File("Abbreviations2.txt");
        File abb3 = new File("Abbreviations3.txt");

        try {
            s = new Scanner(abb1);
            while (s.hasNextLine()) {
                String abb = s.nextLine();
                abb = abb.substring(abb.indexOf("-") + 1)
                        .replaceAll("[^A-Za-z]+", "").toLowerCase();
                abbreviations.add(abb);
            }
            s = new Scanner(abb2);
            while (s.hasNextLine()) {
                String abb =
                        s.next().replaceAll("[^A-Za-z]+", "").toLowerCase();
                s.nextLine();
                abbreviations.add(abb);
            }
            s = new Scanner(abb3);
            while (s.hasNextLine()) {
                String abb =
                        s.next().replaceAll("[^A-Za-z]+", "").toLowerCase();
                s.nextLine();
                abbreviations.add(abb);
            }

            loaded = true;
        } catch (FileNotFoundException e) {
            int a = 1;
        }

    }

    /**
     * Checks if is abbreviation.
     *
     * @param s
     *            string
     * @return true, if is abbreviation
     */
    public static boolean isAbbreviation(String s) {
        return abbreviations.contains(s);

    }

}
