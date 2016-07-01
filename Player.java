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
        Hand newHand = new Hand();      // create new hand
        hands.add(newHand);             // add new hand to list of hands
        newHand.add(hand.remove(1));    // split the original hand
        newHand.add(dealer.shoe.pop()); // deal another card to new hand
        hand.add(dealer.shoe.pop());    // deal another card to original hand
        hand.wager /= 2;                // split the wager
        newHand.wager = hand.wager;
        play(hand);
        play(newHand);
    }

    void surrender(Hand hand){
        hand.done = true;
        hand.surrendered = true;
        hand.wager /= 2;
    }
}
