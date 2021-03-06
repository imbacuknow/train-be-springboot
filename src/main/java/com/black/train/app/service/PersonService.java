package com.black.train.app.service;

import com.black.train.app.entity.Person;
import com.black.train.app.exception.DataNotFoundException;
import com.black.train.app.model.CreatePersonRequest;
import com.black.train.app.repository.PersonRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private PersonRepository personRepository;
    private Gson gson;
    private RedisTemplate redisTemplate;
    private KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public PersonService(PersonRepository personRepository, Gson gson, RedisTemplate redisTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.personRepository = personRepository;
        this.gson = gson;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    // ---------------- create ----------------
    public Person createPersonal(CreatePersonRequest personalRequest) {
        Person person = Person.builder()
                .name(personalRequest.getName())
                .height(personalRequest.getHeight())
                .weight(personalRequest.getWeight())
                .age(personalRequest.getAge())
                .email(personalRequest.getEmail())
                .build();
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        // send to kafka
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("activation-email", personalRequest.getEmail());
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Kafka send failed: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Kafka send success: {}", result.toString());
            }
        });

        return personRepository.save(person);
    }
    public List<Person> createMultiplePersonal(List<CreatePersonRequest> personalRequest) {
        List<Person> people = new ArrayList<>();
        personalRequest.forEach(personal -> {
            Person _person = Person.builder()
                    .name(personal.getName())
                    .height(personal.getHeight())
                    .weight(personal.getWeight())
                    .age(personal.getAge())
                    .email(personal.getEmail())
                    .build();
            people.add(_person);
        });
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        return personRepository.saveAll(people);
    }

    // ---------------- read ----------------
    public List<Person> retrieveAllPersonal() {
        Object personals = redisTemplate.opsForValue().get("personals");
        if (personals != null) {
            System.out.println("data exist in redis");
            List<Person> objList = gson.fromJson(personals.toString(), List.class);
            return objList;
        } else {
            System.out.println("no data exist in redis");
            List<Person> objList = personRepository.findAll();
            redisTemplate.opsForValue().set("personals", gson.toJson(objList), 60, TimeUnit.SECONDS);
            return objList;
        }
    }

    public Person retrievePersonalById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new DataNotFoundException(("Personal id " + id + " not found")));
    }

    public List<Person> retrievePersonalAgeUnder50() {
        Object personals = redisTemplate.opsForValue().get("personalAgeUnder50");

        if (personals != null) {
            System.out.println("data exist in redis");
            List<Person> objList = gson.fromJson(personals.toString(), List.class);
            return objList;
        } else {
            System.out.println("no data exist in redis");
            List<Person> objList = personRepository.findAll()
                    .stream()
                    .filter(p -> p.getAge() < 50)
                    .collect(Collectors.toList());
            redisTemplate.opsForValue().set("personalAgeUnder50", gson.toJson(objList), 60, TimeUnit.SECONDS);
            return objList;
        }
    }

    // ---------------- update ----------------
    public Person updatePersonal(CreatePersonRequest personalRequest) {
        Person person = personRepository.findById(personalRequest.getId()).orElseThrow(() -> new DataNotFoundException("Personal id " + personalRequest.getId() + " not found"));
        person.setName(personalRequest.getName());
        person.setHeight(personalRequest.getHeight());
        person.setWeight(personalRequest.getWeight());
        person.setAge(personalRequest.getAge());
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
        return personRepository.save(person);
    }

    // ---------------- delete ----------------
    public void deletePersonal(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Personal id " + id + " not found"));
        personRepository.deleteById(id);
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
    }

    public void deleteAllPersonal() {
        personRepository.deleteAll();
        redisTemplate.keys("personal*").forEach(key -> redisTemplate.delete(key));
    }
}
