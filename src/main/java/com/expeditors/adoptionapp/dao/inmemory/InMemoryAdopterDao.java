package com.expeditors.adoptionapp.dao.inmemory;

import com.expeditors.adoptionapp.dao.AdopterDAO;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("dev")
public class InMemoryAdopterDao implements AdopterDAO {

    private Map<Integer, Adopter> adopters = new ConcurrentHashMap<>();

    @Override
    public Adopter insert(Adopter newAdopter) {
        adopters.put(newAdopter.getAdopterId(), newAdopter);
        return newAdopter;
    }

    @Override
    public boolean delete(int id) {
        return adopters.remove(id) != null;
    }

    @Override
    public boolean update(Adopter adopter) {
        Adopter original = adopters.get(adopter.getAdopterId());
        if (original == null) {
            return false;
        }
        return adopters.replace(adopter.getAdopterId(), original ,adopter);
    }

    @Override
    public AdoptionRegister creatAdoptionRegister(AdoptionRegister adoptionRegister) {
        return null;
    }

    @Override
    public List<AdoptionRegister> getAdoptions() {
        return null;
    }

    @Override
    public Adopter findById(int id) {
        return adopters.get(id);
    }

    @Override
    public Optional<Adopter> findByName(String name) {
        return (adopters.values().stream().filter(adopter -> adopter.getName().equals(name)).findFirst());
    }

    @Override
    public List<Adopter> findAll() {
        return adopters.values().stream().sorted((adopter1, adopter2) -> adopter1.getName().compareTo(adopter2.getName())).toList();
    }
}
