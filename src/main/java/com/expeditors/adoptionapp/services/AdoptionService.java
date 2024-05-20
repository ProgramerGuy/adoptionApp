package com.expeditors.adoptionapp.services;

import com.expeditors.adoptionapp.dao.AdopterDAO;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile({"jdbc","dev"})
public class AdoptionService implements BaseService{

    @Autowired
    private AdopterDAO adopterDAO;

    public void setAdopterDAO(AdopterDAO adopterDAO) {
        this.adopterDAO = adopterDAO;
    }

    public List<Adopter> getAll() {
        return adopterDAO.findAll();
    }
    public List<AdoptionRegister> getAdoptions() {
        return adopterDAO.getAdoptions();
    }

    public Adopter getById(int id) {
        return adopterDAO.findById(id);
    }

    public Adopter insert(Adopter adopter) {
        return adopterDAO.insert(adopter);
    }

    public AdoptionRegister createRegistration(AdoptionRegister adoptionRegister) {
        return adopterDAO.creatAdoptionRegister(adoptionRegister);
    }

    public Optional<Adopter> getByName(String name) {
        return  adopterDAO.findByName(name);
    }

    public boolean update(Adopter adopter) {
        return adopterDAO.update(adopter);
    }

    public boolean deleteById(int id) {
        return adopterDAO.delete(id);
    }
    public List<AdoptionRegister> getRegistrations(int idAdopter) {
        return  adopterDAO.findById(idAdopter).getAdoptionRegisters().stream().toList();
    }
}
