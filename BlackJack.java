import java.util.*;
import java.lang.Integer;
import static java.util.Arrays.asList;

public class BlackJack {

    Deque<Integer> shoe;
    ArrayList<Integer> dealerHand;
    ArrayList<Integer> playerHand;
    int dealerVal;
    int playerVal;
    int losses, wins, rounds;

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

        losses = 0; wins = 0; rounds = 0;
        // Build the shoe
        ArrayList<Integer> shoeList = new ArrayList<Integer>();
        int[] ranks = new int[] {2,3,4,5,6,7,8,9,10,10,10,10,11};
        for(int suit = 0; suit < 4*shoeSize; suit++)
            for(int j = 0; j < 13; j++)
                shoeList.add(ranks[j]);

        // Shuffle the shoe and convert it to a Deque
        Collections.shuffle(shoeList);
        shoe = new ArrayDeque<Integer>(shoeList);
    }
        
    public static void main(String[] args){
        int ss = Integer.parseInt(args[0]);
        BlackJack bj = new BlackJack(ss);

        int loses = 0 , wins = 0;
        for (int roundCt = 0; bj.shoe.size() > 10; roundCt++){
            System.out.print("Dealing...\t");
            bj.deal();
            bj.play();
            System.out.printf("Dealer Has: %d\tPlayer has: %d\t", bj.dealerVal,
                bj.playerVal);

            if (bj.playerVal > 21){
                System.out.printf(ANSI_RED + "Player busts, Dealer Wins\n" +
                ANSI_RESET);
                loses++;
            }
            else if (bj.playerVal == bj.dealerVal)
                System.out.printf(ANSI_BLUE + "Push.\n" + ANSI_RESET);
            else if (bj.dealerVal > 21 && bj.playerVal <= 21){
                System.out.printf(ANSI_GREEN + 
                "Dealer Busts, but player dosn't, Player wins\n" + ANSI_RESET);
                wins++;
            }
            else if (bj.playerVal > bj.dealerVal){
                System.out.printf(ANSI_GREEN + 
                "Player exceeds Dealer, Player Wins\n" + ANSI_RESET);
                wins++;
            }
            else if (bj.dealerVal > bj.playerVal){
                System.out.printf(ANSI_RED +
                "Dealer exceeds Player, Dealer Wins\n" + ANSI_RESET);
                loses++;
            }
        }

        for(int i = 0; i<80; i++)
            System.out.print("-");
        System.out.print('\n');
        System.out.printf("Summary:\nWins: %d\tLosses: %d\t Score: %d\n",
            wins,loses,wins-loses);
    }

    /**
    * Deals two cards to player and dealer hands. Plays for dealer also, since
    * this is entirely deterministic and independent of player decisions.
    * Dealer stands on Soft 17's.
    * @return rank of dealers face-up card.
    */
    public int deal(){
        
        // Create new empty ArrayLists for dealer and player hands
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();

        // deal two cards from shoe to dealer and player.
        for (int i = 0; i < 2; ++i){
            dealerHand.add(shoe.poll());
            playerHand.add(shoe.poll());
        }
        
        // dealer hits until > 17 stays on all > 17s
        while(dealerHand.stream().mapToInt(Integer::intValue).sum() < 17)
            dealerHand.add(shoe.poll());
        dealerVal = dealerHand.stream().mapToInt(Integer::intValue).sum();

        return dealerHand.get(1);
    }

    public void play(){
        while(playerHand.stream().mapToInt(Integer::intValue).sum() < 17)
            playerHand.add(shoe.poll());
        playerVal = playerHand.stream().mapToInt(Integer::intValue).sum();
    }

    public int getResult(){
        if (playerVal > 21)
            Syetem.out.println(ANSI_RED + "Player Busts, Dealer Wins" +
            ANSI_RESET);

            
}
