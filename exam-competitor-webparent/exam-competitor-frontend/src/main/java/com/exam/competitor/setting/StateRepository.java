package com.exam.competitor.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.exam.competitor.admin.common.entity.Country;
import com.exam.competitor.admin.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
