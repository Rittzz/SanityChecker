package rittzz.sanitychecker.test.model;

import rittzz.sanitychecker.MustExist;
import rittzz.sanitychecker.SanityCheck;

/**
 * Created on 10/8/15.
 */
@SanityCheck
public abstract class LivingBeing {

    @MustExist
    private String dna;

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }
}
