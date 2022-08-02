package com.exam.competitor.admin.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
