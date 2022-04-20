package com.black.train.app.controller;

import com.black.train.app.entity.Personal;
import com.black.train.app.model.CreatePersonalRequest;
import com.black.train.app.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/getAllPersonal")
    public ResponseEntity<List<Personal>> getAllPersonal(){
        return ResponseEntity.ok(personalService.retrieveAllPersonal());
    }

    @GetMapping("/getPersonal")
    public ResponseEntity<Personal> getPersonal(@RequestParam("id") Long id){
        return ResponseEntity.ok(personalService.retrievePersonalById(id));
    }

    @PostMapping("/createPersonal")
    public ResponseEntity<HttpStatus> createPersonal(@RequestBody CreatePersonalRequest createPersonalRequest){
        personalService.createPersonal(createPersonalRequest);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/updatePersonal")
    public ResponseEntity<HttpStatus> updatePersonal(@RequestBody CreatePersonalRequest createPersonalRequest){
        personalService.updatePersonal(createPersonalRequest);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/deletePersonal")
    public ResponseEntity<HttpStatus> deletePersonal(@RequestParam("id") Long id){
        personalService.deletePersonal(id);
        return ResponseEntity.accepted().build();
    }
}
