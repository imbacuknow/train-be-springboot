package com.black.train.app.controller;

import com.black.train.app.entity.Personal;
import com.black.train.app.model.CreatePersonalRequest;
import com.black.train.app.service.PersonalService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/personal")
public class PersonalController {

    private PersonalService personalService;

    @Autowired
    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    // ---------------- GetMapping ----------------
    @GetMapping("/getAllPersonal")
    public ResponseEntity<List<Personal>> getAllPersonal(){
        log.info("getAllPersonal()");
        return ResponseEntity.ok(personalService.retrieveAllPersonal());
    }

    @GetMapping("/getPersonal")
    public ResponseEntity<Personal> getPersonal(@RequestParam("id") Long id){
        log.info("getPersonal(), id={}", id);
        return ResponseEntity.ok(personalService.retrievePersonalById(id));
    }

    @GetMapping("/getPersonalAgeUnder50")
    public ResponseEntity<List<Personal>> getPersonalAgeUnder50(){
        log.info("getPersonalAgeUnder50()");
        List<Personal> personalList = personalService.retrievePersonalAgeUnder50();
        return ResponseEntity.ok(personalList);
    }

    // ---------------- PostMapping ----------------
    @PostMapping("/createPersonal")
    public ResponseEntity<HttpStatus> createPersonal(@RequestBody CreatePersonalRequest createPersonalRequest){
        log.info("createPersonal(), request={}", createPersonalRequest.toString());
        personalService.createPersonal(createPersonalRequest);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/createMultiPersonal")
    public ResponseEntity<HttpStatus> createMultiPersonal(@RequestBody List<CreatePersonalRequest> createPersonalRequests){
        log.info("createMultiPersonal(), request={}", createPersonalRequests.toString());
        personalService.createMultiplePersonal(createPersonalRequests);
        return ResponseEntity.created(null).build();
    }


    // ---------------- PutMapping ----------------
    @PutMapping("/updatePersonal")
    public ResponseEntity<HttpStatus> updatePersonal(@RequestBody CreatePersonalRequest createPersonalRequest){
        log.info("updatePersonal(), request={}", createPersonalRequest.toString());
        personalService.updatePersonal(createPersonalRequest);
        return ResponseEntity.accepted().build();
    }

    // ---------------- DeleteMapping ----------------
    @DeleteMapping("/deletePersonal")
    public ResponseEntity<HttpStatus> deletePersonal(@RequestParam("id") Long id){
        log.info("deletePersonal(), id={}", id);
        personalService.deletePersonal(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/deleteAllPersonal")
    public ResponseEntity<HttpStatus> deletePersonalAll(){
        log.info("deletePersonalAll()");
        personalService.deleteAllPersonal();
        return ResponseEntity.accepted().build();
    }
}
