import java.util.*;

public class NaivePlayer extends Player{
    public int play(Dealer d){
        return Dealer.play(d.shoe,hand);
    }
}
