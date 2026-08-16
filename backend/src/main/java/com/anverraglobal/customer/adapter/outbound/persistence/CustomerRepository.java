package com.anverraglobal.customer.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, UUID>, PagingAndSortingRepository<CustomerEntity, UUID> {
}
