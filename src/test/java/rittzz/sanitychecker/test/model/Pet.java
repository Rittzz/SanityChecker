package rittzz.sanitychecker.test.model;

import rittzz.sanitychecker.SanityCheck;

/**
 * Created on 10/8/15.
 */
@SanityCheck
public class Pet extends LivingBeing {
    private int fleaCount = 0;
    private String name;

    private Flea[] fleas;

    public int getFleaCount() {
        return fleaCount;
    }

    public void setFleaCount(int fleaCount) {
        this.fleaCount = fleaCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Flea[] getFleas() {
        return fleas;
    }

    public void setFleas(Flea[] fleas) {
        this.fleas = fleas;
    }
}
