package rittzz.sanitychecker.test.model;

import java.util.ArrayList;
import java.util.List;

import rittzz.sanitychecker.MustExist;
import rittzz.sanitychecker.SanityCheck;

/**
 * Created on 10/8/15.
 */
@SanityCheck
public class Person extends LivingBeing {

    @MustExist
    private String name;

    private Person spouse;

    private String job;

    private List<Pet> pets = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<Pet> getPets() {
        return pets;
    }
}
