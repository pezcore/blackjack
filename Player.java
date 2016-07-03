import java.util.*;
import java.math.*;

public abstract class Player extends Participant {
    BigDecimal bankroll = BigDecimal.ZERO;
    Dealer dealer;
    ArrayList<PlayerHand> hands = new ArrayList<>(4);

    public void play(){
        play(hands.get(0));
        for (PlayerHand h : hands)
            assert(h.done);
    }

    public abstract int play(Hand hand);

    public void hit(Hand hand){
        hand.add(dealer.shoe.pop());
    }

    public void double_down(Hand hand){
        hand.wager = hand.wager.multiply(new BigDecimal(2));
        hit(hand);
    }

    void split(Hand hand){
        PlayerHand newHand = new PlayerHand();      // create new hand
        hands.add(newHand);             // add new hand to list of hands
        newHand.add(hand.remove(1));    // split the original hand
        newHand.add(dealer.shoe.pop()); // deal another card to new hand
        hand.add(dealer.shoe.pop());    // deal another card to original hand
        newHand.wager = hand.wager;     // copy wager
        play(hand);
        play(newHand);
    }

    void surrender(Hand hand){
        hand.done = true;
        hand.surrendered = true;
        hand.wager = hand.wager.divide(new BigDecimal(2));
    }
}
