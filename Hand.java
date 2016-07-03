import java.math.*;
import java.util.*;

public class Hand extends ArrayList<Byte>{
    byte value = 0;
    byte softAces = 0;
    BigDecimal wager = BigDecimal.TEN;
    boolean done = false;
    boolean surrendered = false;
    boolean hasSplit = false;
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

    @Override
    public Byte remove(int idx){
        // you can only remove a card from a splittable hand
        assert(isSplitable());
        hasSplit = true;
        Byte removed = super.remove(idx);
        if (removed == (byte) 11)
            value --;
        else
            value -= removed;
        return removed;
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

    public boolean isSplitable(){
        if (size() == 2 && get(0) == get(1))
            return true;
        else
            return false;
    }
}
