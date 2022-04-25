package com.black.train.app.service;

import com.black.train.app.entity.Personal;
import com.black.train.app.exception.DataNotFoundException;
import com.black.train.app.model.CreatePersonalRequest;
import com.black.train.app.repository.PersonalRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalService {

    private PersonalRepository personalRepository;
    private Gson gson;
    private RedisTemplate redisTemplate;


    @Autowired
    public PersonalService(PersonalRepository personalRepository, Gson gson, RedisTemplate redisTemplate) {
        this.personalRepository = personalRepository;
        this.gson = gson;
        this.redisTemplate = redisTemplate;
    }

    // ---------------- create ----------------
    public Personal createPersonal(CreatePersonalRequest personalRequest) {
        Personal personal = Personal.builder()
                .name(personalRequest.getName())
                .height(personalRequest.getHeight())
                .weight(personalRequest.getWeight())
                .age(personalRequest.getAge())
                .build();
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        return personalRepository.save(personal);
    }
    public List<Personal> createMultiplePersonal(List<CreatePersonalRequest> personalRequest) {
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
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        return personalRepository.saveAll(personals);
    }

    // ---------------- read ----------------
    public List<Personal> retrieveAllPersonal() {
        Object personals = redisTemplate.opsForValue().get("personals");
        if (personals != null) {
            System.out.println("data is exist in redis");
            List<Personal> objList = gson.fromJson(personals.toString(), List.class);
            return objList;
        } else {
            System.out.println("data not exist in redis");
            List<Personal> objList = personalRepository.findAll();
            redisTemplate.opsForValue().set("personals", gson.toJson(objList), 60, TimeUnit.SECONDS);
            return objList;
        }
    }

    public Personal retrievePersonalById(Long id) {
        return personalRepository.findById(id).orElseThrow(() -> new DataNotFoundException(("Personal id " + id + " not found")));
    }

    public List<Personal> retrievePersonalAgeUnder50() {
        Object personals = redisTemplate.opsForValue().get("personalAgeUnder50");

        if (personals != null) {
            System.out.println("data is exist in redis");
            List<Personal> objList = gson.fromJson(personals.toString(), List.class);
            return objList;
        } else {
            System.out.println("data not exist in redis");
            List<Personal> objList = personalRepository.findAll()
                    .stream()
                    .filter(p -> p.getAge() < 50)
                    .collect(Collectors.toList());
            redisTemplate.opsForValue().set("personalAgeUnder50", gson.toJson(objList), 60, TimeUnit.SECONDS);
            return objList;
        }
    }

    // ---------------- update ----------------
    public Personal updatePersonal(CreatePersonalRequest personalRequest) {
        Personal personal = personalRepository.findById(personalRequest.getId()).orElseThrow(() -> new DataNotFoundException("Personal id " + personalRequest.getId() + " not found"));
        personal.setName(personalRequest.getName());
        personal.setHeight(personalRequest.getHeight());
        personal.setWeight(personalRequest.getWeight());
        personal.setAge(personalRequest.getAge());
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        return personalRepository.save(personal);
    }

    // ---------------- delete ----------------
    public void deletePersonal(Long id) {
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Personal id " + id + " not found"));
        personalRepository.deleteById(id);
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
    }

    public void deleteAllPersonal() {
        personalRepository.deleteAll();
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
    }
}
