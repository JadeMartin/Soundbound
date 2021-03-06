package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Model class to hold a possible passport country for a profile
 */
@Entity
public class PassportCountry {

    @Id
    @Constraints.Required
    private int passport_country_id;
    @Constraints.Required
    private String passport_name;

    public static final Finder<String, PassportCountry> find = new Finder<>(PassportCountry.class);


    public PassportCountry(int passport_country_id, String passport_name) {
        this.passport_country_id = passport_country_id;
        this.passport_name = passport_name;
    }


    public PassportCountry(String passport_name) {
        this.passport_name = passport_name;
    }

    public int getPassportId() {
        return passport_country_id;
    }

    public String getPassportName() {
        return passport_name;
    }
}