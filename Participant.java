import java.util.*;

public class Participant{
    int wins, losses;
    ArrayList<Byte> hand = new ArrayList<Byte>(21); // needs only hold 21 cards
    public void printHand(){
        Iterator<Byte> iter = hand.iterator();
        System.out.print("[");
        while (iter.hasNext()){
            byte card = iter.next();
            if (card != (byte) 11)
                System.out.printf("%d",card);
            else
                System.out.print("A");
            if (iter.hasNext())
                System.out.print(",");
        }
        System.out.printf("]");
    }
}
