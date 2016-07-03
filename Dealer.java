import java.math.*;
import java.util.*;
import org.apache.commons.cli.*;

public class Dealer extends Participant{
    ArrayList<Player> players;
    ArrayDeque<Byte> shoe;
    private Hand hand = new Hand();
    Rules rules = new Rules();

    public Dealer(int shoeSize){
        // initialize the shoe size
        shoe = newShoe(shoeSize);
        players = new ArrayList<Player>();
    }

    public static ArrayDeque<Byte> newShoe(int size){
        ArrayList<Byte> arLshoe = new ArrayList<>(52*size);

        // Build the shoe
        byte[] ranks = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        for(int i = 0; i < size; i++)
            for(byte j = 0; j < 4; j++)
                for(byte rankptr = 0; rankptr < 13; rankptr++)
                    arLshoe.add(ranks[rankptr]);

        // shuffle the shoe
        Collections.shuffle(arLshoe);
        return new ArrayDeque<>(arLshoe);
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    byte getUpCard(){
        return hand.get(0);
    }

    void printShoe(){
        for(byte c :shoe)
            System.out.println(c);
    }

    void deal(){
        // deal exactly 2 cards to each player at the table
        for(Player p : players){
            p.hands.clear();
            p.hands.add(new PlayerHand());
            p.hands.get(0).add(shoe.pop());
            p.hands.get(0).add(shoe.pop());
        }
        // deal exactly 2 cards to self.
        hand.clear();
        hand.add(shoe.pop()); hand.add(shoe.pop());
    }

    public static int play(ArrayDeque<Byte> shoe,Hand hand, boolean hitSoft17){
        while (hand.value < 17 ||
        (hand.value == 17 && hand.softAces!=0 && hitSoft17))
                hand.add(shoe.pop());
        hand.done = true;
        return hand.value;
    }

    public static void main(String[] args) throws ParseException{
        Options options = new Options();
        options.addOption("S", true, "Shoe size in decks");
        options.addOption("n", true, "Games to play");
        options.addOption("N", true, "Shoes to play");
        options.addOption("s", true, "Strategy");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        int shoeSize, maxGames, maxShoes;

        String shoeSizeString = cmd.getOptionValue("S");
        if (shoeSizeString != null)
            shoeSize = Integer.parseInt(shoeSizeString);
        else
            shoeSize = 6;

        String maxGamesString = cmd.getOptionValue("n");
        if (maxGamesString != null)
            maxGames = Integer.parseInt(maxGamesString);
        else
            maxGames = Integer.MAX_VALUE;

        String maxShoesString = cmd.getOptionValue("N");
        if (maxShoesString != null)
            maxShoes = Integer.parseInt(maxShoesString);
        else
            maxShoes = Integer.MAX_VALUE;

        String strategyString = cmd.getOptionValue("s");

        int shoes, games;
        shoes = games = 0;

        Dealer d = new Dealer(shoeSize);
        Player p;
        if(strategyString != null && strategyString.equals("basic")){
            p = new BasicPlayer();
            System.out.println("Selecting Basic");
        } else {
            p = new NaivePlayer();
            System.out.println("Selecting Naïve");
        }

        d.players.add(p);
        p.dealer = d;
        while(games < maxGames && shoes < maxShoes){
            d.deal();
            d.play();
            for (int i = 0; i < p.hands.size(); i++)
                System.out.print(p.hands.get(i).toString());
            System.out.print(d.hand.toString());
            System.out.print('\t');
            System.out.printf("\t%s\n",p.bankroll.toString());
            games++;

            if (d.shoe.size() < 20){
                d.shoe = newShoe(shoeSize);
                shoes++;
                System.out.println("Reshuffled   ---------------------------");
            }
        }

        System.out.println("------------------------------------------------");
        System.out.printf("Wins: %d\n",p.wins);
        System.out.printf("Losses: %d\n",p.losses);
        System.out.printf("Games: %d\n",games);
        System.out.printf("Shoes: %d\n",shoes);
        System.out.printf("BankRoll: %s\n",p.bankroll.toString());
    }

    /**
     * Plays a round with all players at the table after they are delt and
     * returns a Result array specifying results of each players final hand
     * against the dealer.
     */
    public int play(){
        // Play own hand
        play(shoe,hand,rules.hitSoft17s);
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            p.play();
            for (PlayerHand ph: p.hands){
                assignResult(ph);
                payOut(p,ph);
            }
        }
        return 0;
    }

    static int payOut(Player p, PlayerHand h){
        BigDecimal payOut;
        if (h.result == Result.PUSH)
           return 0;
        else if (h.result == Result.PLAYERBUST ||
            h.result == Result.DEALERWIN ||
            h.result == Result.DEALERBLACKJACK ||
            h.result == Result.PLAYERSURRENDER){
            p.losses++;
            payOut = h.wager.negate();
        } else if (h.result == Result.PLAYERWIN ||
            h.result == Result.DEALERBUST){
            p.wins++;
            payOut = h.wager;
        } else if (h.result == Result.PLAYERBLACKJACK){
            p.wins ++;
            payOut = h.wager.multiply(new BigDecimal("1.5"));
        } else
            throw new Error("Unexpected result");
        p.bankroll = p.bankroll.add(payOut);
        return 0;
    }

    /**
     * returns a byte specifying the results of a game of blackjack between 2
     * Participants.
     * <p>
     * 0 = player bust
     * 1 =
     */
    public static Result getResults(Hand pHand, Hand dHand){
        if (pHand.surrendered)
            return Result.PLAYERSURRENDER;
        else if (pHand.value > 21)
            return Result.PLAYERBUST; // player bust
        else if (pHand.value ==21 && pHand.size() == 2 && !(dHand.value==21
        && dHand.size()==2))
            return Result.PLAYERBLACKJACK; // player BlackJack
        else if (dHand.value > 21)
            return Result.DEALERBUST; // Dealer Bust
        else if (pHand.value > dHand.value)
            return Result.PLAYERWIN; // player exceed
        else if (dHand.value > pHand.value)
            return Result.DEALERWIN; // Dealer Exceed
        else if (dHand.value == 21 && dHand.size()==2 && pHand.size()!=2)
            return Result.DEALERBLACKJACK; // Dealer BlackJack.
        else
            return Result.PUSH; // push
    }

    int assignResult(PlayerHand hand){
        assert(hand.done);
        hand.result = getResults(hand,this.hand);
        return 0;
    }
}
