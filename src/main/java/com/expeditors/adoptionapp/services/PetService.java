package com.expeditors.adoptionapp.services;

import com.expeditors.adoptionapp.dao.jpa.JPAAdopterDao;
import com.expeditors.adoptionapp.dao.jpa.PetDao;
import com.expeditors.adoptionapp.dao.jpa.RegistrationAdopterDao;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import com.expeditors.adoptionapp.domain.Pet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Profile("jpa")
public class PetService {

    @Autowired
    private PetDao petDao;

    public List<Pet> getAll() {
        return petDao.findAll();
    }

    public Optional<Pet> getById(int id) {
        return petDao.findById(id);
    }

    public Pet insert(Pet pet) {
        return petDao.save(pet);
    }

    public boolean update(Pet pet) {
        try {
            if(!petDao.existsById(pet.getPetId())) {
                return false;
            }
            petDao.save(pet);
        }
        catch(Exception ex) {
            return false;
        }
        return true;
    }

    public boolean deleteById(int id) {
        try {
            if(!petDao.existsById(id)) {
                return false;
            }
            petDao.deleteById(id);
        }
        catch(Exception ex) {
            return false;
        }
        return true;
    }
}
