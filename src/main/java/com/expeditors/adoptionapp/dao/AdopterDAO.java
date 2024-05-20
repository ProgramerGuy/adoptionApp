package com.expeditors.adoptionapp.dao;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdopterDAO{
    Adopter insert(Adopter newAdopter);

    boolean delete(int id);

    boolean update(Adopter adopter);

    AdoptionRegister creatAdoptionRegister(AdoptionRegister adoptionRegister);

    List<AdoptionRegister> getAdoptions();

    Adopter findById(int id);

    Optional<Adopter> findByName(String name);

    List<Adopter> findAll();
}
