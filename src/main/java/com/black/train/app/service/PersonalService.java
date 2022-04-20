package com.black.train.app.service;

import com.black.train.app.entity.Personal;
import com.black.train.app.exception.DataNotFoundException;
import com.black.train.app.model.CreatePersonalRequest;
import com.black.train.app.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    // ---------------- create ----------------
    public Personal createPersonal(CreatePersonalRequest personalRequest) {
        log.info("Creating personal: {}", personalRequest);
        Personal personal = Personal.builder()
                .name(personalRequest.getName())
                .height(personalRequest.getHeight())
                .weight(personalRequest.getWeight())
                .age(personalRequest.getAge())
                .build();
        return personalRepository.save(personal);
    }

    // ---------------- read ----------------
    public List<Personal> retrieveAllPersonal() {
        log.info("Retrieving all personal");
        return personalRepository.findAll();
    }

    public Personal retrievePersonalById(Long id) {
        log.info("Retrieving personal with id: {}", id);
        return personalRepository.findById(id).orElse(null);
    }

    // ---------------- update ----------------
    public Personal updatePersonal(CreatePersonalRequest personalRequest) {
        log.info("Updating personal with id: {}", personalRequest);
        Personal personal = personalRepository.findById(personalRequest.getId()).orElseThrow(() -> new DataNotFoundException("Personal not found"));
        personal.setName(personalRequest.getName());
        personal.setHeight(personalRequest.getHeight());
        personal.setWeight(personalRequest.getWeight());
        personal.setAge(personalRequest.getAge());
        return personalRepository.save(personal);
    }

    // ---------------- delete ----------------
    public void deletePersonal(Long id) {
        log.info("Deleting personal with id: {}", id);
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Personal not found"));
        personalRepository.deleteById(id);
    }
}
