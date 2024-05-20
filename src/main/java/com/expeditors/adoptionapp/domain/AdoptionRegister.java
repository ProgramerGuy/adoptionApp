package com.expeditors.adoptionapp.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

@Entity
@Transactional
public class AdoptionRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adoptionRegisterId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id")
    private Adopter adopter;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private LocalDate adoptionDate;

    public AdoptionRegister(int adoptionRegisterId, Adopter adopter, Pet pet, LocalDate adoptionDate) {
        this.adoptionRegisterId = adoptionRegisterId;
        this.adopter = adopter;
        this.pet = pet;
        this.adoptionDate = adoptionDate;
    }

    public AdoptionRegister() {

    }

    public Adopter getAdopter() {
        return adopter;
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }

    public int getAdoptionRegisterId() {
        return adoptionRegisterId;
    }

    public void setAdoptionRegisterId(int adoptionRegisterId) {
        this.adoptionRegisterId = adoptionRegisterId;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }
}
