
import java.util.Scanner;
import java.util.Set;

public class CiaParserMain {
    
    public static void main (String[] args) {
        CiaParser myParser = new CiaParser();
        System.out.println("Which question would you like to answer?");
        System.out.println("If you want to answer question 1, type Q1, if you " +
                "want to answer question 2, type Q2, if you want to answer " +
                "question 3...");
        Scanner myScanner = new Scanner(System.in);
        String questionNumber = myScanner.nextLine();
        if (questionNumber.equals("Q1")) {
            System.out.println("What would you like the first color to be?");
            Scanner insideScanner = new Scanner(System.in);
            String firstColor = insideScanner.nextLine();
            System.out.println("What would you like the second color to be?");
            Scanner insideScanner2 = new Scanner(System.in);
            String secondColor = insideScanner2.nextLine();
            System.out.println("These are the countries!");
            Set mySet = myParser.countryFlags(firstColor, secondColor);
            for (Object country : mySet) {
                String countryInString = (String) country;
                System.out.println(countryInString);
            }
        } else if (questionNumber.equals("Q2")) {
            System.out.println("What would ocean would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.lowestPointInOcean(input));
        } else if (questionNumber.equals("Q3")) {
            System.out.println("What continent would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.electricityProduction(input));
        } else if (questionNumber.equals("Q4")) {
            System.out.println("What continent would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.largestCoastlineToLandAreaRatio(input));
        } else if (questionNumber.equals("Q5")) {
            System.out.println("What continent would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.populationAndHighestMeanElevation(input));
        } else if (questionNumber.equals("Q6")) {
            System.out.println("What sea would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.importPartnersThirdLargestIsland(input));
        } else if (questionNumber.equals("Q7")) {
            System.out.println("What is the character (please capitalize) you would like to input?");
            Scanner insideScanner = new Scanner(System.in);
            char input = insideScanner.nextLine().charAt(0);
            System.out.println(myParser.countriesStartingWithSomeCharSortedArea(input));
        } else if (questionNumber.equals("Q8")) {
            System.out.println("What substring would you like to input?");
            Scanner insideScanner = new Scanner(System.in);
            String input = insideScanner.nextLine();
            System.out.println(myParser.containsSubstringOrderedByLandBoundary(input));
        }
    }
}
