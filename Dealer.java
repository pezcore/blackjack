import java.util.*;

public class Dealer{
    ArrayList<Byte> hand;
    ArrayList<Player> players;
    private ArrayList<Byte> shoe;

    public Dealer(int shoeSize){
        // initialize the shoe size
        shoe = new ArrayList<Byte>(52*shoeSize);

        // Build the shoe
        byte[] ranks = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        int shoeptr = 0;
        for(int i = 0; i < shoeSize; i++)
            for(byte j = 0; j < 4; j++)
                for(byte rankptr = 0; rankptr < 13; rankptr++, shoeptr++)
                    shoe.add(ranks[rankptr]);

        shoeptr = 0;
        // shuffle the shoe
        Collections.shuffle(shoe);
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void printShoe(){
        for(int i = 0; i < shoe.size();i++)
            System.out.println(shoe.get(i));
    }

    public static void main(String[] args){
        Dealer d = new Dealer(1);
        d.printShoe();
    }
}
