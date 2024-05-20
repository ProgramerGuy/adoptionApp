package com.expeditors.adoptionapp.dao.jpa;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Profile("jpa")
public interface RegistrationAdopterDao extends JpaRepository<AdoptionRegister, Integer>  {

}
