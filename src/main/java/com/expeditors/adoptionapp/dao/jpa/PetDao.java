package com.expeditors.adoptionapp.dao.jpa;

import com.expeditors.adoptionapp.domain.AdoptionRegister;
import com.expeditors.adoptionapp.domain.Pet;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@Profile("jpa")
public interface PetDao extends JpaRepository<Pet, Integer>  {

}
