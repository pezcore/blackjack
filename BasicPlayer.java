public class BasicPlayer extends Player{
    public int play(Hand hand){
        byte dealerHole = dealer.hand.get(0);
        while (hand.value < 19){
            // ALWAYS hit under 11
            if (hand.value <= 11)
                hit(hand);
            else if (hand.value == 12 && dealerHole >= 4 && dealerHole <= 6)
                return hand.value;
            else if(hand.value == 12)
                hit(hand);
            else if (hand.softAces == 0)
                if (hand.value >= 13 && hand.value <=16)
                    if (dealerHole <= 6)
                        return hand.value;
                    else
                        hit(hand);
                else if (hand.value >= 17)
                    return hand.value;
                else
                System.out.printf("%d\n",hand.value);
            else if (hand.softAces != 0)
                if (hand.value >= 13 && hand.value <=17)
                    hit(hand);
                else if (hand.value == 18)
                    if (dealerHole <= 8)
                        return hand.value;
                    else
                        hit(hand);
                else if (hand.value >= 19)
                    return hand.value;
                else
                System.out.println("HARD ERROR");
        }
        return hand.value;
    }
}
