package org.example.travelexpertsfx.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Agency {
    private SimpleIntegerProperty agencyId;
    private SimpleStringProperty agncyAddress;
    private SimpleStringProperty agncyCity;
    private SimpleStringProperty agncyProv;
    private SimpleStringProperty agncyPostal;
    private SimpleStringProperty agncyCountry;
    private SimpleStringProperty agncyPhone;
    private SimpleStringProperty agncyFax;

    public Agency(int agencyId,
                  String agncyAddress,
                  String agncyCity,
                  String agncyProv,
                  String agncyPostal,
                  String agncyCountry,
                  String agncyPhone,
                  String agncyFax) {
        this.agencyId = new SimpleIntegerProperty(agencyId);
        this.agncyAddress = new SimpleStringProperty(agncyAddress);
        this.agncyCity = new SimpleStringProperty(agncyCity);
        this.agncyProv = new SimpleStringProperty(agncyProv);
        this.agncyPostal = new SimpleStringProperty(agncyPostal);
        this.agncyCountry = new SimpleStringProperty(agncyCountry);
        this.agncyPhone = new SimpleStringProperty(agncyPhone);
        this.agncyFax = new SimpleStringProperty(agncyFax);
    }

    public int getAgencyId() {
        return agencyId.get();
    }

    public SimpleIntegerProperty agencyIdProperty() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId.set(agencyId);
    }

    public String getAgncyAddress() {
        return agncyAddress.get();
    }

    public SimpleStringProperty agncyAddressProperty() {
        return agncyAddress;
    }

    public void setAgncyAddress(String agncyAddress) {
        this.agncyAddress.set(agncyAddress);
    }

    public String getAgncyCity() {
        return agncyCity.get();
    }

    public SimpleStringProperty agncyCityProperty() {
        return agncyCity;
    }

    public void setAgncyCity(String agncyCity) {
        this.agncyCity.set(agncyCity);
    }

    public String getAgncyProv() {
        return agncyProv.get();
    }

    public SimpleStringProperty agncyProvProperty() {
        return agncyProv;
    }

    public void setAgncyProv(String agncyProv) {
        this.agncyProv.set(agncyProv);
    }

    public String getAgncyPostal() {
        return agncyPostal.get();
    }

    public SimpleStringProperty agncyPostalProperty() {
        return agncyPostal;
    }

    public void setAgncyPostal(String agncyPostal) {
        this.agncyPostal.set(agncyPostal);
    }

    public String getAgncyCountry() {
        return agncyCountry.get();
    }

    public SimpleStringProperty agncyCountryProperty() {
        return agncyCountry;
    }

    public void setAgncyCountry(String agncyCountry) {
        this.agncyCountry.set(agncyCountry);
    }

    public String getAgncyPhone() {
        return agncyPhone.get();
    }

    public SimpleStringProperty agncyPhoneProperty() {
        return agncyPhone;
    }

    public void setAgncyPhone(String agncyPhone) {
        this.agncyPhone.set(agncyPhone);
    }

    public String getAgncyFax() {
        return agncyFax.get();
    }

    public SimpleStringProperty agncyFaxProperty() {
        return agncyFax;
    }

    public void setAgncyFax(String agncyFax) {
        this.agncyFax.set(agncyFax);
    }
}
