package com.expeditors.adoptionapp.service;

import com.expeditors.adoptionapp.dao.AdopterDAO;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.services.AdoptionRepoService;
import com.expeditors.adoptionapp.services.AdoptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles({"jdbc"})
class PetAdoptionApplicationTests {

	@Mock
	private AdopterDAO adopterDAO;

	@InjectMocks
	private AdoptionService service;

	List<Adopter> adopters = List.of(
			new Adopter(
				1,
				"Francisco",
                    "8677566534"),
			new Adopter(
				2,
				"Amador Hernandez",
				"8975643241"));

	@Test
	public void serviceInsert() {
//		Adopter adopter = new Adopter(
//				1,
//				"Francisco",
//				new Pet.PetBuilder(Pet.PetType.Dog).setBreed("Poodle").build());

		Mockito.when(adopterDAO.insert(adopters.get(0))).thenReturn(adopters.get(0));
		Mockito.when(adopterDAO.findById(1)).thenReturn(adopters.get(0));

		Adopter newAdopter = service.insert(adopters.get(0));

		Mockito.verify(adopterDAO).insert(adopters.get(0));


		assertEquals(newAdopter,service.getById(1));
	}

	@Test
	public void serviceUpdate() {
		Adopter adopter = new Adopter(
				1,
				"Francisco",
                "8677566534");

		Mockito.when(adopterDAO.insert(adopter)).thenReturn(adopter);

		service.insert(adopter);

		Mockito.verify(adopterDAO).insert(adopter);

		adopter.setName("Rogelio");

		Mockito.when(adopterDAO.update(adopter)).thenReturn(true);
		Mockito.when(adopterDAO.findById(1)).thenReturn(adopter);

		service.update(adopter);
		Mockito.verify(adopterDAO).update(adopter);
		assertEquals("Rogelio",service.getById(1).getName());


		Mockito.when(adopterDAO.delete(1)).thenReturn(true);
		service.deleteById(1);

		Mockito.when(adopterDAO.update(adopter)).thenReturn(false);

		Mockito.verify(adopterDAO).delete(1);
		Mockito.verify(adopterDAO).update(adopter);

		assertFalse(service.update(adopter));
	}

	@Test
	public void serviceDelete() {
		Adopter adopter = new Adopter(
				1,
				"Francisco",
                "8677566534");

		Mockito.when(adopterDAO.insert(adopter)).thenReturn(adopter);
		Mockito.when(adopterDAO.delete(1)).thenReturn(true);
		Mockito.when(adopterDAO.findById(1)).thenReturn(null);

		service.insert(adopter);

		Mockito.verify(adopterDAO).insert(adopter);

		service.deleteById(1);

		Mockito.verify(adopterDAO).delete(1);

		assertNull(service.getById(1));
		Mockito.verify(adopterDAO).findById(1);
	}


	@Test
	public void serviceSearch() {

		Adopter newAdopter1 = new Adopter(
						1,
						"Francisco",
                "8677566534");
		Mockito.when(adopterDAO.insert(newAdopter1)).thenReturn(newAdopter1);
		service.insert(newAdopter1);
		Mockito.verify(adopterDAO).insert(newAdopter1);

		Adopter newAdopter2 = new Adopter(
				2,
				"Amador Hernandez",
                "8677566534");
		Mockito.when(adopterDAO.insert(newAdopter2)).thenReturn(newAdopter2);
		service.insert(newAdopter2);
		Mockito.verify(adopterDAO).insert(newAdopter2);

		Mockito.when(adopterDAO.findById(100)).thenReturn(null);

		assertNull(service.getById(100));
		Mockito.verify(adopterDAO).findById(100);

		Mockito.when(adopterDAO.findByName("Francisco")).thenReturn(Optional.ofNullable(newAdopter1));
		assertEquals("Francisco", service.getByName("Francisco").orElse(new Adopter(0,"","")).getName());
		Mockito.verify(adopterDAO).findByName("Francisco");
	}

}
