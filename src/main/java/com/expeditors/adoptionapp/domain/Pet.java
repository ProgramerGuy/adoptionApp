package com.expeditors.adoptionapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;

@Entity
public class Pet {

    public Pet() {}

    public enum PetType {
        Cat,
        Dog,
        Turtles,
        DOG,
        CAT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;
    private String name;
    @Enumerated(EnumType.STRING)
    @Nullable
    private PetType type;
    private String breed;

    @OneToMany(mappedBy = "pet")
    @JsonIgnore
    private Collection<AdoptionRegister> adoptionRegisters;

    public Pet(PetBuilder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.breed = builder.breed;
    }

    public String getName() {
        return name;
    }


    public PetType getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }


    public Collection<AdoptionRegister> getAdoptionRegisters() {
        return adoptionRegisters;
    }

    public void setAdoptionRegisters(Collection<AdoptionRegister> adoptionRegisters) {
        this.adoptionRegisters = adoptionRegisters;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(@Nullable PetType type) {
        this.type = type;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }



    public static class PetBuilder {
        private String name;
        private PetType type;
        private String breed;

        public PetBuilder(PetType type) {
            this.type = type;
        }

        public PetBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PetBuilder setType(PetType type) {
            this.type = type;
            return this;
        }

        public PetBuilder setBreed(String breed) {
            this.breed = breed;
            return this;
        }

        public Pet build() {
            return new Pet(this);
        }

        @Override
        public String toString () {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

}
