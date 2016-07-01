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
        int dec;

        while (!hand.done){
            // get decision
            if (hand.isSplitable())
                dec = splitLUT[getIndex(hand)][dealerUp-2];
            else if (hand.softAces != 0)
                dec = softLUT[getIndex(hand)][dealerUp-2];
            else if (hand.softAces == 0 && !hand.isSplitable())
                dec = hardLUT[getIndex(hand)][dealerUp-2];
            else
                return -1;

            //execute decision
            switch (dec){
                case 0: hand.done = true; break;
                case 1: hit(hand); break;
                case 2: double_down(hand); break;
                case 3: surrender(hand); break;
                case 4: double_down(hand); break;
                case 5: split(hand); break;
            }
        }

        return hand.value;
    }
}
