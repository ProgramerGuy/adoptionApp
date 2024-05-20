package com.expeditors.adoptionapp.services;

import com.expeditors.adoptionapp.dao.jpa.JPAAdopterDao;
import com.expeditors.adoptionapp.dao.jpa.RegistrationAdopterDao;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
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
public class AdoptionRepoService implements BaseService {

    @Autowired
    private JPAAdopterDao adopterDAO;

    @Autowired
    private RegistrationAdopterDao registrationDAO;

    public List<Adopter> getAll() {
        return adopterDAO.findAll();
    }
    public List<AdoptionRegister> getAdoptions() {
        return registrationDAO.findAll();
    }

    public Adopter getById(int id) {
        return adopterDAO.findById(id);
    }

    public Adopter insert(Adopter adopter) {
        return adopterDAO.save(adopter);
    }

    public AdoptionRegister createRegistration(AdoptionRegister adoptionRegister) {
        adoptionRegister.setAdoptionDate(LocalDate.now());
        return registrationDAO.save(adoptionRegister);
    }

    public Optional<Adopter> getByName(String name) {
        return  adopterDAO.findByName(name);
    }

    public List<AdoptionRegister> getRegistrations(int idAdopter) {
        return  adopterDAO.findById(idAdopter).getAdoptionRegisters().stream().toList();
    }

    public boolean update(Adopter updateAdopter) {
        try {
            adopterDAO.save(updateAdopter);
        }
        catch(Exception ex) {
            return false;
        }
        return true;
    }

    public boolean deleteById(int id) {
        try {
            adopterDAO.deleteById(id);
        }
        catch(Exception ex) {
            return false;
        }
        return true;
    }
}
