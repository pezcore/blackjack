public abstract class Player extends Participant {
    int bankroll;
    Dealer dealer;
    int wager = 10;

    public abstract int play();

    public void hit(){
        hand.add(dealer.shoe.pop());
    }

    public void double_down(){
        wager *= 2;
        hit();
    }
}
