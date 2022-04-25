package com.black.train.app.service;

import com.black.train.app.entity.Personal;
import com.black.train.app.exception.DataNotFoundException;
import com.black.train.app.model.CreatePersonalRequest;
import com.black.train.app.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalService {

    private PersonalRepository personalRepository;

    @Autowired
    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

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
    public List<Personal> createMultiplePersonal(List<CreatePersonalRequest> personalRequest) {
        log.info("Creating multiple personal: {}", personalRequest);
        List<Personal> personals = new ArrayList<>();
        personalRequest.forEach(personal -> {
            Personal _personal = Personal.builder()
                    .name(personal.getName())
                    .height(personal.getHeight())
                    .weight(personal.getWeight())
                    .age(personal.getAge())
                    .build();
            personals.add(_personal);
        });
        return personalRepository.saveAll(personals);
    }

    // ---------------- read ----------------
    public List<Personal> retrieveAllPersonal() {
        log.info("Retrieving all personal");
        return personalRepository.findAll();
    }

    @Cacheable(value = "personal", key = "#id", unless = "#result == null")
    public Personal retrievePersonalById(Long id) {
        log.info("Retrieving personal with id: {}", id);
        return personalRepository.findById(id).orElse(null);
    }

    public List<Personal> retrievePersonalAgeUnder50() {
        log.info("Retrieving personal age under 50");
        List<Personal> personalAgeUnder50 = personalRepository.findAll()
                .stream()
                .filter(p -> p.getAge() < 50)
                .collect(Collectors.toList());
        return personalAgeUnder50;
    }

    // ---------------- update ----------------
    @CachePut(value = "personal", key = "#personalRequest.id")
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
    @CacheEvict(value = "personal", key = "#id")
    public void deletePersonal(Long id) {
        log.info("Deleting personal with id: {}", id);
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Personal not found"));
        personalRepository.deleteById(id);
    }

    @CacheEvict(value = "personal", allEntries = true)
    public void deleteAllPersonal() {
        log.info("Deleting all personal");
        personalRepository.deleteAll();
    }
}
