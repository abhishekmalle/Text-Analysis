
// I worked on this project alone with the help of the Java API

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Class TextCheck. Contains the main class.
 *
 * @author Abhishek
 * @version 1.0
 */
public class TextCheck {

    /**
     * The main method.
     *
     * @param args
     *            the two good and bad files
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean problems;
        String goodFileName = args[0];
        String badFileName = args[1];
        String srcFileName = null, targetFile = null;
        int[] totals = null;
        String longestSentence = null;
        int sentimentScore = 0;

        // getting source and computing answers
        do {
            problems = false;
            try {
                System.out.println(
                        "What is the name of the source file to analyze?");
                srcFileName = scan.nextLine();

                totals = TextAnalyzer.totalCount(srcFileName);
                longestSentence = TextAnalyzer.longestSentence(srcFileName);
            } catch (FileNotFoundException e) {
                System.err.println("File not found, try again:");
                problems = true;
            } catch (Exception e) {
                int a = 1;
            }
        } while (problems);

        // getting target files and computing answers
        do {
            problems = false;
            try {
                System.out.println("What is the name of the "
                        + "target words file to analyze?");
                targetFile = scan.nextLine();

                TextAnalyzer.targetWordCount(srcFileName, targetFile);
                sentimentScore = TextAnalyzer.sentimentScore(srcFileName,
                        goodFileName, badFileName);

            } catch (FileNotFoundException e) {
                System.err.println("File not found, try again:");
                problems = true;
            } catch (Exception e) {
                int a = 1;
            }
        } while (problems);

        // printing results/info to the console
        System.out.println("Analyzing");

        System.out.printf("Your file contains %d words and %d sentences.",
                totals[0], totals[1]);

        System.out.println("\n\nThe longest sentence in this document is:\n"
                + longestSentence);

        System.out.println(
                "\nWriting out target word counts to file targetCount.txt\n");

        if (sentimentScore > 0) {
            System.out.println(
                    "The sentiment analysis score is +" + sentimentScore);
        } else {
            System.out.println(
                    "The sentiment analysis score is " + sentimentScore);
        }
    }

}
