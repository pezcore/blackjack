import java.util.*;

public class Hand extends ArrayList<Byte>{
    byte value = 0;
    byte softAces = 0;
    int wager = 10;
    boolean done = false;
    private static final long serialVersionUID = 42L;


    public Hand(){
        super(21);
    }

    public void clear(){
        super.clear();
        value = 0;
        softAces = 0;
        done = false;
    }


    @Override
    public boolean add(Byte card){
        boolean changed = super.add(card);
        value += card;
        if (card == (byte) 11)
            softAces++;
        if ((value > 21) && (softAces > 0)){
            value -= 10;
            softAces--;
        }
        return changed;
    }

    public String toString(){
        char[] str = new char[size()];
        byte card;
        Iterator<Byte> iter = iterator();
        for (int i = 0; i < size(); i++){
            card = iter.next();
            if (card < 10)
                str[i] = (char) (card + 48);
            else if (card == (byte) 10)
                str[i] = 'T';
            else if (card == (byte) 11)
                str[i] = 'A';
        }
        return String.format("%10s|%d",new String(str),value);

    }
}
