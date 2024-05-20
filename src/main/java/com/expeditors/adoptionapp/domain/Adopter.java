package com.expeditors.adoptionapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;


@Entity
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adopterId;
    private String name;
    private String phoneNumber;
    //private LocalDate adoptionDate;
    private String email;

    public Adopter(){}

    public Adopter(int id, String name, String phoneNumber, String email) {
        this.adopterId = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Adopter(int id, String adopterName, String adopterPhoneNumber) {
        this(id,adopterName,adopterPhoneNumber, null);
    }
    public int getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }

    @OneToMany(mappedBy = "adopter")
    @JsonIgnore
    private Collection<AdoptionRegister> adoptionRegisters;

    public Collection<AdoptionRegister> getAdoptionRegisters() {
        return adoptionRegisters;
    }

    public void setAdoptionRegisters(Collection<AdoptionRegister> adoptionRegisters) {
        this.adoptionRegisters = adoptionRegisters;
    }
}
