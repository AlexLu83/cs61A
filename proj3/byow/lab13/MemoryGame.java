package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.Stopwatch;
import java.util.Scanner;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        rand = new Random(seed);
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


    }

    public String generateRandomString(int n) {
        String AlphaNumericString = "0123456789" + "abcdefghijklmnopqrstuvxyz";
        String result = "";
        for (int i = 0; i < n; i++) {
            result = result + AlphaNumericString.charAt(rand.nextInt(AlphaNumericString.length()));
        }
        return result;
    }

    public void drawFrame(String s) {
        //Take the string and display it in the center of the screen
        //If game is not over, display relevant game information at the top of the screen
        StdDraw.clear();
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.setPenColor(Color.black);
        StdDraw.text(15, 20, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        Stopwatch s;
        String space = " ";
        String toBeDisplayed;
        for (int i = 0; i < letters.length(); i++) {
            toBeDisplayed = "";
            s = new Stopwatch();
            while(s.elapsedTime() != 0.6) {

            }
            for(int j = 0; j < i; j++) {
                toBeDisplayed = toBeDisplayed + space;
            }
            drawFrame(toBeDisplayed + Character.toString(letters.charAt(i)));
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        //Scanner scan = new Scanner();
        int i = 0;
        String result = "";
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(4000);
        }
        while (StdDraw.hasNextKeyTyped() && i < n) {
            result = result + StdDraw.nextKeyTyped();
            i++;
        }
        return result;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        //drawFrame(generateRandomString(5));
        //flashSequence("");
        flashSequence(solicitNCharsInput(5));


        //TODO: Establish Engine loop
    }

}
