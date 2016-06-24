public abstract class Player extends Participant {
    int bankroll;
    Dealer dealer;

    public abstract int play();

    public void hit(){
        hand.add(dealer.shoe.pop());
    }

    public void double_down(){
        hand.wager *= 2;
        hit();
    }
}
