package com.expeditors.adoptionapp.repository;

import com.expeditors.adoptionapp.dao.jpa.JPAAdopterDao;
import com.expeditors.adoptionapp.domain.Adopter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdoptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JPAAdopterDao adopterDao;

    @BeforeEach
    public void getReady() {
        Adopter adopter = new Adopter(
                34,
                "Amador Hernandez",
                "8975643241");
        adopterDao.save(adopter);
    }

    @Test
    public void getAll() {
        List<Adopter> adopters = adopterDao.findAll();

        assertFalse(adopters.isEmpty());
    }

    @Test
    public void findById() {
        List<Adopter> adopters = adopterDao.findAll();
        Adopter adopter = adopterDao.findById(adopters.get(0).getAdopterId());

        assertEquals(adopter.getAdopterId(),adopters.get(0).getAdopterId());

        adopter = adopterDao.findById(-1);

        assertNull(adopter);
    }

    @Test
    public void insert() {
        Adopter adopter = new Adopter(
                34,
                "Amador Hernandez",
                "8975643241");

        Adopter savedAdopter = adopterDao.save(adopter);
        Adopter newAdopter = adopterDao.findById(savedAdopter.getAdopterId());

        assertEquals(savedAdopter.getAdopterId(),newAdopter.getAdopterId());
    }

    @Test
    public void update() {

        Adopter updateAdopter = adopterDao.findAll().get(0);

        updateAdopter.setName("Bryan Nostradamus");
        adopterDao.save(updateAdopter);

        Adopter dbAdopter = adopterDao.findById(updateAdopter.getAdopterId());

        assertEquals(dbAdopter.getName(),"Bryan Nostradamus");
    }

    @Test
    public void delete() {

        Adopter updateAdopter = adopterDao.findAll().get(0);

        adopterDao.deleteById(updateAdopter.getAdopterId());

        Adopter dbAdopter = adopterDao.findById(updateAdopter.getAdopterId());

        assertNull(dbAdopter);
    }
}
