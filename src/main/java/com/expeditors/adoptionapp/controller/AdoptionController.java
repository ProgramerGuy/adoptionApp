package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import com.expeditors.adoptionapp.services.AdoptionRepoService;
import com.expeditors.adoptionapp.services.AdoptionService;
import com.expeditors.adoptionapp.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("Adopter")
public class AdoptionController {

    @Autowired
    private BaseService adoptionService;

    @GetMapping()
    public List<Adopter> getAdopters() {
        return adoptionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        Adopter adopter = adoptionService.getById(id);
        if (adopter == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found adopter with id:" + id);
        }
        return ResponseEntity.ok(adopter);
    }

    @GetMapping("/register/{idAdopter}")
    public ResponseEntity<?> getByRegistrations(@PathVariable("idAdopter") int idAdopter) {
        List<AdoptionRegister> registers = adoptionService.getRegistrations(idAdopter);
        if (registers == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found adopter with id:" + idAdopter);
        }
        return ResponseEntity.ok(registers);
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Adopter adopter) {
        Adopter newAdopter = adoptionService.insert(adopter);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adopter.getAdopterId())
                .toUri();

        return ResponseEntity.created(newResource).build();
    }

    @GetMapping("/register")
    public List<AdoptionRegister> getAdoptions() {
        return adoptionService.getAdoptions();
    }

    @PostMapping("/register")
    public ResponseEntity<?> createRegister(@RequestBody AdoptionRegister adoptionRegister) {
        AdoptionRegister register = adoptionService.createRegistration(adoptionRegister);

        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        boolean result = adoptionService.deleteById(id);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found adopter with id: " + id);
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Adopter adopter) {
        boolean result = adoptionService.update(adopter);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found adopter with id: " + adopter.getAdopterId());
        }

        return ResponseEntity.ok(result);
    }
}
