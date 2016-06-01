import java.util.*;

public abstract class Player{
    //needs only hold 21 cards
    public ArrayList<Byte> hand = new ArrayList<Byte>(21);
    public abstract void play(Dealer d);
}
