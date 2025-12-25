package com.info.repository;

import com.info.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, Long>{

	
	Mono<Employee> findById(Long employeeId);
	
	
	//@Query(value= {$gt:[{"salary":1000}]})
	Flux<Employee> findEmployeesBySalary();
	
	//db.employee.find({$gt:[{"salary":1000}]})
	
}
