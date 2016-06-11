import java.util.*;

public class Participant{
    int wins, losses;
    ArrayList<Byte> hand = new ArrayList<Byte>(21); // needs only hold 21 cards
    public void printHand(){
        byte counter = 0;
        System.out.print("[");
        for (byte card : hand){
            if (card != (byte) 11)
                System.out.printf("%d",card);
            else
                System.out.print("A");
            if (counter++ < hand.size()-1)
                System.out.print(",");
        }
        System.out.printf("]");
    }
}
