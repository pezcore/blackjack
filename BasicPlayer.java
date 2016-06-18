public class BasicPlayer extends Player{
    public int play(Dealer d){
        byte dealerHole = d.hand.get(1);
        while (hand.value < 19){
            // ALWAYS hit under 11
            if (hand.value <= 11)
                hand.add(d.shoe.pop());
            else if (hand.value == 12 && dealerHole >= 4 && dealerHole <= 6)
                return hand.value;
            else if(hand.value == 12)
                hand.add(d.shoe.pop());
            else if (hand.softAces == 0)
                if (hand.value >= 13 && hand.value <=16)
                    if (dealerHole <= 6)
                        return hand.value;
                    else
                        hand.add(d.shoe.pop());
                else if (hand.value >= 17)
                    return hand.value;
                else
                System.out.printf("%d\n",hand.value);
            else if (hand.softAces != 0)
                if (hand.value >= 13 && hand.value <=17)
                    hand.add(d.shoe.pop());
                else if (hand.value == 18)
                    if (dealerHole <= 8)
                        return hand.value;
                    else
                        hand.add(d.shoe.pop());
                else if (hand.value >= 19)
                    return hand.value;
                else
                System.out.println("HARD ERROR");
        }
        return hand.value;
    }
}
