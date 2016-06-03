import java.util.*;

public class NaivePlayer extends Player{
    public int play(Dealer d){
        assert(hand.size() == 2 && d.hand.size() == 2);
        
        // number of aces in hand
        int softAces = Collections.frequency(hand,(byte)11);
        int handval = hand.get(0) + hand.get(1);
        if (handval > 21){
            assert(softAces == 2);
            handval -= 10;
            softAces--;
        }

        while (handval < 17 || (handval == 17 && softAces!=0)){
            handval += d.shoe.get(0);
            if (handval > 21 && softAces > 0){
                handval -= 10;
                softAces--;
            }
            hand.add(d.shoe.remove(0));
        }

        for (byte c: hand)
            System.out.format("%s,",c);
        System.out.format("\t%d\n",handval);
        return handval;
    }
}
