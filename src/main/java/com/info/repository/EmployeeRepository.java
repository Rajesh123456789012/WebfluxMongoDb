package com.info.repository;

import com.info.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("!local")
@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, Long>{

	
	Mono<Employee> findById(Long employeeId);
	

	Flux<Employee> findEmployeesBySalary();
	
}
