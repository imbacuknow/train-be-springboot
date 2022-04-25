package com.black.train.app.controller;

import com.black.train.app.entity.Person;
import com.black.train.app.model.CreatePersonRequest;
import com.black.train.app.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/personal")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // ---------------- GetMapping ----------------
    @GetMapping("/getAllPersonal")
    public ResponseEntity<List<Person>> getAllPersonal(){
        log.info("getAllPersonal()");
        return ResponseEntity.ok(personService.retrieveAllPersonal());
    }

    @GetMapping("/getPersonal")
    public ResponseEntity<Person> getPersonal(@RequestParam("id") Long id){
        log.info("getPersonal(), id={}", id);
        return ResponseEntity.ok(personService.retrievePersonalById(id));
    }

    @GetMapping("/getPersonalAgeUnder50")
    public ResponseEntity<List<Person>> getPersonalAgeUnder50(){
        log.info("getPersonalAgeUnder50()");
        List<Person> personList = personService.retrievePersonalAgeUnder50();
        return ResponseEntity.ok(personList);
    }

    // ---------------- PostMapping ----------------
    @PostMapping("/createPersonal")
    public ResponseEntity<HttpStatus> createPersonal(@RequestBody CreatePersonRequest createPersonRequest){
        log.info("createPersonal(), request={}", createPersonRequest.toString());
        personService.createPersonal(createPersonRequest);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/createMultiPersonal")
    public ResponseEntity<HttpStatus> createMultiPersonal(@RequestBody List<CreatePersonRequest> createPersonRequests){
        log.info("createMultiPersonal(), request={}", createPersonRequests.toString());
        personService.createMultiplePersonal(createPersonRequests);
        return ResponseEntity.created(null).build();
    }


    // ---------------- PutMapping ----------------
    @PutMapping("/updatePersonal")
    public ResponseEntity<HttpStatus> updatePersonal(@RequestBody CreatePersonRequest createPersonRequest){
        log.info("updatePersonal(), request={}", createPersonRequest.toString());
        personService.updatePersonal(createPersonRequest);
        return ResponseEntity.accepted().build();
    }

    // ---------------- DeleteMapping ----------------
    @DeleteMapping("/deletePersonal")
    public ResponseEntity<HttpStatus> deletePersonal(@RequestParam("id") Long id){
        log.info("deletePersonal(), id={}", id);
        personService.deletePersonal(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/deleteAllPersonal")
    public ResponseEntity<HttpStatus> deletePersonalAll(){
        log.info("deletePersonalAll()");
        personService.deleteAllPersonal();
        return ResponseEntity.accepted().build();
    }
}
