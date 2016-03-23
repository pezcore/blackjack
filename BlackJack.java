import java.util.*;
import java.util.stream.IntStream;
import java.lang.Integer;
import static java.util.Arrays.asList;

public class BlackJack {

    byte[] shoe;
    byte[] dealerHand = new byte[21];   //needs only hold 21 cards
    byte[] playerHand = new byte[21];
    byte dealerHandptr, playerHandptr;
    int losses, wins, rounds, shoeptr;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public BlackJack(int shoeSize){
        // initialize the shoe size
        shoe = new byte[52*shoeSize];

        // Build the shoe
        byte[] ranks = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        int shoeptr = 0;
        for(byte i = 0; i < shoeSize; i++)
            for(byte j = 0; j < 4; j++)
                for(byte rankptr = 0; rankptr < 13; rankptr++, shoeptr++)
                    shoe[shoeptr] = ranks[rankptr];

        shoeptr = 0;
        // shuffle the shoe
        shuffle(shoe);
    }

    public static void main(String[] args){

        BlackJack bj = new BlackJack(2);
        while (bj.shoeptr < (bj.shoe.length - 10)){
            System.out.printf("Dealing...\t");
            byte dealerVal = bj.deal();
            byte playerVal = bj.play();
            bj.printResult(playerVal, dealerVal);
        }
    }

    // Implementing Fisher–Yates shuffle, shuffles an array of bytes in place
    public static void shuffle(byte[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            byte a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
    * Deals two cards to player and dealer hands. Plays for dealer also, since
    * this is entirely deterministic and independent of player decisions.
    * Dealer stands on Soft 17's.
    * @return value of dealers hand
    */
    public byte deal(){

        // reset everyones hand
        byte dealerVal = 0;
        playerHandptr = 0; dealerHandptr = 0;
        Arrays.fill(dealerHand, (byte) 0);
        Arrays.fill(playerHand, (byte) 0);
        for(byte i = 0; i < 2; ++i){
            dealerHand[dealerHandptr] = shoe[shoeptr++];
            dealerVal += dealerHand[dealerHandptr++];
            playerHand[playerHandptr++] = shoe[shoeptr++];
        }

        while(dealerVal < 17){
            dealerHand[dealerHandptr] = shoe[shoeptr++];
            dealerVal += dealerHand[dealerHandptr++];
        }

        return dealerVal;

    }

    /**
     * Play a round. updates shoeptr (removes cards from shoe) and updates
     * plater hand
     * @return value of players hand
     */
    public byte play(){
        byte playerval = 0;
        for(byte i = 0; i < 2; ++i)
            playerval += playerHand[i];

        while(playerval < 17){
            playerHand[playerHandptr] = shoe[shoeptr++];
            playerval += playerHand[playerHandptr++];
        }

        return playerval;

    }

    public void printResult(byte playerVal, byte dealerVal){
        System.out.printf(
        "Player Had: %d\tDealer Had: %d\t",playerVal,dealerVal);

        if (playerVal > 21){
            System.out.println(ANSI_RED + "Player Busts, Dealer Wins" +
            ANSI_RESET);
        }
        else if(dealerVal > 21){
            System.out.println(ANSI_GREEN + "Dealer Busts, Player Wins" +
            ANSI_RESET);
        }
        else if(playerVal == dealerVal){
            System.out.println(ANSI_BLUE + "Push." + ANSI_RESET);
        }
        else if(playerVal > dealerVal){
            System.out.println(ANSI_GREEN +
            "Player exceeds dealer, Player Wins" +
            ANSI_RESET);
        }
        else if(dealerVal > playerVal){
            System.out.println(ANSI_RED +
            "Dealer exceeds player, Dealer Wins" +
            ANSI_RESET);
        }

    }
}
