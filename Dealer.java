import java.util.*;

public class Dealer extends Participant{
    ArrayList<Player> players;
    ArrayDeque<Byte> shoe;

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

    void printShoe(){
        for(byte c :shoe)
            System.out.println(c);
    }

    void deal(){
        // deal exactly 2 cards to each player at the table
        for(Player p : players){
            p.hand.clear();
            p.hand.add(shoe.pop()); p.hand.add(shoe.pop());
        }
        // deal exactly 2 cards to self.
        hand.clear();
        hand.add(shoe.pop()); hand.add(shoe.pop());
    }

    public static int play(ArrayDeque<Byte> shoe,Hand hand){
        while (hand.value < 17 || (hand.value == 17 && hand.softAces!=0))
            hand.add(shoe.pop());
        return hand.value;
    }

    public static void main(String[] args){
        int shoeSize, shoes = 0;
        if (args.length == 1) shoeSize = Integer.parseInt(args[0]);
        else shoeSize = 8;

        Dealer d = new Dealer(shoeSize);
        Player p = new NaivePlayer();
        d.players.add(p);
        int games = 0;
        Result[] result;
        while(games < 100){
            d.deal();
            result = d.play();
            System.out.print(p.hand.toString());
            System.out.print(d.hand.toString());
            System.out.print('\t');
            System.out.println(Arrays.toString(result));
            games++;

            if (d.shoe.size() < 10){
                d.shoe = newShoe(6);
                shoes++;
                System.out.println("Reshuffled   ---------------------------");
            }
        }

        System.out.println("------------------------------------------------");
        System.out.printf("Wins: %d\n",p.wins);
        System.out.printf("Losses: %d\n",p.losses);
        System.out.printf("Games: %d\n",games);
        System.out.printf("Shoes: %d\n",shoes);
        System.out.printf("BankRoll: %d\n",p.bankroll);
    }

    /**
     * Plays a round with all players at the table after they are delt and
     * returns a byte array specifying results of each players final hand
     * against the dealer.
     */
    public Result[] play(){
        Result[] results = new Result[players.size()];
        int dVal = play(shoe,hand);
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            int pVal = p.play(this);
            results[i] = getResults(p.hand, this.hand);
            if (results[i] == Result.PLAYERBUST ||
                results[i] == Result.DEALERWIN ||
                results[i] == Result.DEALERBLACKJACK){
                p.losses++;
                p.bankroll -= 10;
            } else if (results[i] == Result.PLAYERWIN ||
                results[i] == Result.DEALERBUST){
                p.wins++;
                p.bankroll += 10;
            } else if (results[i] == Result.PLAYERBLACKJACK){
                p.wins ++;
                p.bankroll += 15;
            }

        }
        return results;
    }

    /**
     * returns a byte specifying the results of a game of blackjack between 2
     * Participants.
     * <p>
     * 0 = player bust
     * 1 =
     */
    public static Result getResults(Hand pHand, Hand dHand){
        if (pHand.value > 21){
            return Result.PLAYERBUST; // player bust
        } else if (pHand.value ==21 && pHand.size() == 2 && !(dHand.value==21
        && dHand.size()==2)){
            return Result.PLAYERBLACKJACK; // player BlackJack
        } else if (dHand.value > 21){
            return Result.DEALERBUST; // Dealer Bust
        } else if (pHand.value > dHand.value){
            return Result.PLAYERWIN; // player exceed
        } else if (dHand.value > pHand.value){
            return Result.DEALERWIN; // Dealer Exceed
        } else if (dHand.value == 21 && dHand.size()==2 && pHand.size()!=2){
            return Result.DEALERBLACKJACK; // Dealer BlackJack.
        } else
            return Result.PUSH; // push
    }
}
