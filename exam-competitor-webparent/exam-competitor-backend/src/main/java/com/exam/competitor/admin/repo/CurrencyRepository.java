package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.exam.competitor.admin.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
	
	public List<Currency> findAllByOrderByNameAsc();
}
