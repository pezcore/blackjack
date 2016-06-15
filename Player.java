import java.util.*;

public abstract class Player extends Participant {
    int bankroll;
    public abstract int play(Dealer d);
}
