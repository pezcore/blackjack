import java.util.*;

public class Dealer extends Participant{
    ArrayList<Player> players;
    ArrayDeque<Byte> shoe;

    public Dealer(int shoeSize){
        // initialize the shoe size
        ArrayList<Byte> arLshoe = new ArrayList<>(52*shoeSize);
        players = new ArrayList<Player>();
        hand = new ArrayList<Byte>(21);

        // Build the shoe
        byte[] ranks = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        for(int i = 0; i < shoeSize; i++)
            for(byte j = 0; j < 4; j++)
                for(byte rankptr = 0; rankptr < 13; rankptr++)
                    arLshoe.add(ranks[rankptr]);

        // shuffle the shoe
        Collections.shuffle(arLshoe);
        shoe = new ArrayDeque<>(arLshoe);
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

    public static int play(ArrayDeque<Byte> shoe,ArrayList<Byte> hand){
        assert(hand.size() == 2);

        // number of aces in hand
        int softAces = Collections.frequency(hand,(byte)11);
        int handval = hand.get(0) + hand.get(1);
        if (handval > 21){
            assert(softAces == 2);
            handval -= 10;
            softAces--;
        }

        while (handval < 17 || (handval == 17 && softAces!=0)){
            handval += shoe.peek();
            hand.add(shoe.pop());
            if (handval > 21 && softAces > 0){
                handval -= 10;
                softAces--;
            }
        }
        return handval;
    }

    public static void main(String[] args){
        int shoeSize;
        if (args.length == 1) shoeSize = Integer.parseInt(args[0]);
        else shoeSize = 8;

        Dealer d = new Dealer(shoeSize);
        Player p = new NaivePlayer();
        d.players.add(p);
        int games = 0;
        Result[] result;
        while(d.shoe.size() > 10){
            d.deal();
            result = d.play();
            System.out.println(Arrays.toString(result));
            games++;
        }
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
            results[i] = getResults(pVal,p.hand.size(),dVal,this.hand.size());
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
    public static Result getResults(int pVal, int pSize, int dVal, int dSize){
        if (pVal > 21){
            return Result.PLAYERBUST; // player bust
        } else if (pVal ==21 && pSize == 2 && !(dVal==21 && dSize==2)){
            return Result.PLAYERBLACKJACK; // player BlackJack
        } else if (dVal > 21){
            return Result.DEALERBUST; // Dealer Bust
        } else if (pVal > dVal){
            return Result.PLAYERWIN; // player exceed
        } else if (dVal > pVal){
            return Result.DEALERWIN; // Dealer Exceed
        } else if (dVal == 21 && dSize==2 && pSize!=2){
            return Result.DEALERBLACKJACK; // Dealer BlackJack.
        } else
            return Result.PUSH; // push
    }
}
