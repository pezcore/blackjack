public class NaivePlayer extends Player{
    public int play(){
        return Dealer.play(dealer.shoe,hand);
    }
}
