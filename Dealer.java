import java.util.*;

public class Dealer extends Participant{
    ArrayList<Player> players;
    ArrayList<Byte> shoe;

    public Dealer(int shoeSize){
        // initialize the shoe size
        shoe = new ArrayList<Byte>(52*shoeSize);
        players = new ArrayList<Player>();
        hand = new ArrayList<Byte>(21);

        // Build the shoe
        byte[] ranks = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        for(int i = 0; i < shoeSize; i++)
            for(byte j = 0; j < 4; j++)
                for(byte rankptr = 0; rankptr < 13; rankptr++)
                    shoe.add(ranks[rankptr]);

        // shuffle the shoe
        Collections.shuffle(shoe);
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    void printShoe(){
        for(int i = 0; i < shoe.size();i++)
            System.out.println(shoe.get(i));
    }

    void deal(){
        // deal exactly 2 cards to each player at the table
        for(Player p : players){
            p.hand.clear();
            p.hand.add(shoe.remove(0)); p.hand.add(shoe.remove(0));
        }
        // deal exactly 2 cards to self.
        hand.clear();
        hand.add(shoe.remove(0)); hand.add(shoe.remove(0));
    }

    public static int play(ArrayList<Byte> shoe,ArrayList<Byte> hand){
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
            handval += shoe.get(0);
            if (handval > 21 && softAces > 0){
                handval -= 10;
                softAces--;
            }
            hand.add(shoe.remove(0));
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
        while(d.shoe.size() > 10){
            d.deal();
            int playerval = p.play(d);
            int dealerval = play(d.shoe,d.hand);
            games++;
        }
    }
}
