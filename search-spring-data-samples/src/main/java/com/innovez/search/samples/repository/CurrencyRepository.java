package com.innovez.search.samples.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovez.search.samples.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

}
