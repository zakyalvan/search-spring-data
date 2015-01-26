package com.innovez.search.samples.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovez.search.samples.entity.Person;

public interface PersonRespository extends JpaRepository<Person, Integer> {

}
