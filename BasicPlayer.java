public class BasicPlayer extends Player{

    public static final int[][] hardLUT = {
        {1,1,1,1,1,1,1,1,1,1},
        {1,2,2,2,2,1,1,1,1,1},
        {2,2,2,2,2,2,2,2,1,1},
        {2,2,2,2,2,2,2,2,2,1},
        {1,1,0,0,0,1,1,1,1,1},
        {0,0,0,0,0,1,1,1,1,1},
        {0,0,0,0,0,1,1,1,3,1},
        {0,0,0,0,0,1,1,3,3,3},
        {0,0,0,0,0,0,0,0,0,0}};

    public static final int[][] softLUT = {
        {1,1,1,2,2,1,1,1,1,1},
        {1,1,2,2,2,1,1,1,1,1},
        {1,2,2,2,2,1,1,1,1,1},
        {0,4,4,4,4,0,0,1,1,1},
        {0,0,0,0,0,0,0,0,0,0}};

    public static final int[][] splitLUT = {
        {5,5,5,5,5,5,1,1,1,1},
        {1,1,1,5,5,1,1,1,1,1},
        {2,2,2,2,2,2,2,2,1,1},
        {5,5,5,5,5,1,1,1,1,1},
        {5,5,5,5,5,5,1,1,1,1},
        {5,5,5,5,5,5,5,5,5,5},
        {5,5,5,5,5,0,5,5,0,0},
        {0,0,0,0,0,0,0,0,0,0},
        {5,5,5,5,5,5,5,5,5,5}};

    private int getIndex(Hand hand){
        if (hand.isSplitable())
            if (hand.get(0) == (byte) 11)
                return 8;
            else if (hand.value == (byte) 4 || hand.value == (byte) 6)
                return 0;
            else
                return hand.get(0) - 3;
        else if (hand.softAces != 0)
            if (hand.value == 19 || hand.value == 20)
                return 4;
            else if (hand.value == 18)
                return 3;
            else if (hand.value == 17)
                return 2;
            else if (hand.value == 16 || hand.value == 15)
                return 1;
            else if (hand.value == 13 || hand.value == 14)
                return 0;
            else
                return -1;
        else if (hand.softAces == 0 && !hand.isSplitable())
            if (hand.value >= 5 && hand.value <= 8)
                return 0;
            else if (hand.value >= 9 && hand.value <= 13)
                return hand.value - 8;
            else if (hand.value >= 14 && hand.value <= 17)
                return hand.value - 9;
            else if (hand.value >= 18 && hand.value <= 20)
                return 8;
            else
                return -1;
        else
            return -1;
    }

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
