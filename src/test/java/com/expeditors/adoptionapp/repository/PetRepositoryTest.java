package com.expeditors.adoptionapp.repository;

import com.expeditors.adoptionapp.dao.jpa.JPAAdopterDao;
import com.expeditors.adoptionapp.dao.jpa.PetDao;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PetRepositoryTest {

    @Autowired
    private PetDao petDao;

    @BeforeEach
    public void getReady() {
        Pet pet = new Pet.PetBuilder(Pet.PetType.Cat).setName("Kittier").setBreed("Syamess").build();
        petDao.save(pet);
    }

    @Test
    public void getAll() {
        List<Pet> pets = petDao.findAll();

        assertFalse(pets.isEmpty());
    }

    @Test
    public void findById() {

        List<Pet> pets = petDao.findAll();
        Optional<Pet> pet = petDao.findById(pets.get(0).getPetId());

        assertEquals(pet.get().getPetId(),pets.get(0).getPetId());

        pet = petDao.findById(-1);

        assertTrue(pet.isEmpty());
    }

    @Test
    public void insert() {
        Pet pet = new Pet.PetBuilder(Pet.PetType.Cat).setName("Mr. Kitty").setBreed("Syamess").build();

        Pet savedPet = petDao.save(pet);
        Optional<Pet> newPet = petDao.findById(savedPet.getPetId());

        assertEquals(savedPet.getPetId(),newPet.get().getPetId());
    }

    @Test
    public void update() {

        Pet updateAdopter = petDao.findAll().get(0);

        updateAdopter.setName("Mini Catty");
        petDao.save(updateAdopter);

        Optional<Pet> dePet = petDao.findById(updateAdopter.getPetId());

        assertEquals(dePet.get().getName(),"Mini Catty");
    }

    @Test
    public void delete() {

        Pet pet = petDao.findAll().get(0);

        petDao.deleteById(pet.getPetId());

        Optional<Pet> dbPet = petDao.findById(pet.getPetId());

        assertTrue(dbPet.isEmpty());
    }
}
