package rittzz.sanitychecker.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rittzz.sanitychecker.SanityChecker;
import rittzz.sanitychecker.SanityException;
import rittzz.sanitychecker.test.model.Flea;
import rittzz.sanitychecker.test.model.Person;
import rittzz.sanitychecker.test.model.Pet;

/**
 * Created on 10/8/15.
 */
public class SanityCheckerTests {

    private SanityChecker sanityChecker;

    @Before
    public void setup() {
        sanityChecker = new SanityChecker();
    }

    @After
    public void teardown() {
        sanityChecker = null;
    }

    @Test
    public void basic() throws SanityException {
        final Person person = new Person();
        person.setName("Bob");
        person.setDna("ACTATAC");

        sanityChecker.check(person);
    }

    @Test
    public void badData() throws SanityException {
        try {
            final Person person = new Person();
            person.setName("Bob");
            person.setDna(null);

            sanityChecker.check(person);

            Assert.fail("Should of caught the null DNA");
        }
        catch (SanityException ex) {
            // Good
        }
    }

    @Test
    public void badNestedData() throws SanityException {
        try {
            final Person person = new Person();
            person.setName("Bob");
            person.setDna("ACTACTA");

            final Pet pet = new Pet();
            pet.setDna(null);
            person.getPets().add(pet);

            sanityChecker.check(person);

            Assert.fail("Should of caught the null DNA in pet");
        }
        catch (SanityException ex) {
            // Good
        }
    }

    @Test
    public void badNestedData2() throws SanityException {
        try {
            final Person person = new Person();
            person.setName("Bob");
            person.setDna("ACTACTA");
            person.setSpouse(new Person());

            sanityChecker.check(person);

            Assert.fail("Should of caught the null name for spouse");
        }
        catch (SanityException ex) {
            // Good
        }
    }

    @Test
    public void badNestedData3() throws SanityException {
        try {
            final Pet pet = new Pet();
            pet.setName("Meepo");

            final Flea[] fleas = new Flea[2];
            fleas[0] = new Flea();
            fleas[0].setItchLevel(99);
            fleas[1] = new Flea();
            fleas[0].setItchLevel(1);

            pet.setFleas(fleas);

            sanityChecker.check(pet);

            Assert.fail("Should of caught the flea custom sanity check");
        }
        catch (SanityException ex) {
            // Good
        }
    }
}
