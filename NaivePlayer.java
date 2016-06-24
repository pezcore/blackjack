public class NaivePlayer extends Player{
    public int play(Hand hand){
        return Dealer.play(dealer.shoe,hand);
    }
}
