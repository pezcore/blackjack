import java.util.*;

public abstract class Player extends Participant {
    int bankroll;
    Dealer dealer;
    ArrayList<Hand> hands = new ArrayList<>(4);

    public void play(){
        for (Hand h : hands)
            play(h);
    }

    public abstract int play(Hand hand);

    public void hit(Hand hand){
        hand.add(dealer.shoe.pop());
    }

    public void double_down(Hand hand){
        hand.wager *= 2;
        hit(hand);
    }

    void split(Hand hand){
        assert(hand.get(0) == hand.get(1));
        Hand newHand = new Hand();
        hands.add(newHand);
        newHand.add(hand.remove(1));
        hand.wager /= 2;
        newHand.wager = hand.wager;
        play(hand);
        play(newHand);
    }
}
