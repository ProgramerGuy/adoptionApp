package com.expeditors.adoptionapp.services;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;

import java.util.List;
import java.util.Optional;

public interface BaseService {
    public List<Adopter> getAll();
    public List<AdoptionRegister> getAdoptions();

    public Adopter getById(int id);

    public Adopter insert(Adopter adopter);

    public AdoptionRegister createRegistration(AdoptionRegister adoptionRegister);

    public Optional<Adopter> getByName(String name);

    public boolean update(Adopter adopter);

    public boolean deleteById(int id);
    public List<AdoptionRegister> getRegistrations(int idAdopter);

}
