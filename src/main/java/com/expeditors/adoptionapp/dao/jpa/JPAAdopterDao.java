package com.expeditors.adoptionapp.dao.jpa;

import com.expeditors.adoptionapp.dao.AdopterDAO;
import com.expeditors.adoptionapp.dao.BaseDAO;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Transactional
@Profile("jpa")
public interface JPAAdopterDao extends JpaRepository<Adopter, Integer> {
    public Adopter findById(int id) ;
    public Optional<Adopter> findByName(String name) ;
}
