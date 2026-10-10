package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}