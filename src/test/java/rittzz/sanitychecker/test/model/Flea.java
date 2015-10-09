package rittzz.sanitychecker.test.model;

import rittzz.sanitychecker.SanityCheck;
import rittzz.sanitychecker.SanityCheckable;
import rittzz.sanitychecker.SanityException;

/**
 * Created on 10/8/15.
 */
@SanityCheck
public class Flea implements SanityCheckable {

    private int itchLevel;

    public int getItchLevel() {
        return itchLevel;
    }

    public void setItchLevel(int itchLevel) {
        this.itchLevel = itchLevel;
    }

    @Override
    public void sanityCheck() throws SanityException  {
        if (itchLevel < 5) {
            throw new SanityException("Needs more itch! :: " + itchLevel);
        }
    }
}
