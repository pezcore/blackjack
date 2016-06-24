public class BasicPlayer extends Player{
    public int play(Hand hand){
        byte dealerUp = dealer.getUpCard();
        while (hand.value < 19){
            // ALWAYS hit under 11
            if (hand.value <= 11)
                hit(hand);
            else if (hand.value == 12 && dealerUp >= 4 && dealerUp <= 6){
                hand.done = true;
                return hand.value;
            } else if(hand.value == 12)
                hit(hand);
            else if (hand.softAces == 0)
                if (hand.value >= 13 && hand.value <=16)
                    if (dealerUp <= 6){
                        hand.done = true;
                        return hand.value;
                    } else
                        hit(hand);
                else if (hand.value >= 17){
                    hand.done = true;
                    return hand.value;
                } else
                System.out.printf("%d\n",hand.value);
            else if (hand.softAces != 0)
                if (hand.value >= 13 && hand.value <=17)
                    hit(hand);
                else if (hand.value == 18)
                    if (dealerUp <= 8){
                        hand.done = true;
                        return hand.value;
                    } else
                        hit(hand);
                else if (hand.value >= 19){
                    hand.done = true;
                    return hand.value;
                } else
                System.out.println("HARD ERROR");
        }
        hand.done = true;
        return hand.value;
    }
}
