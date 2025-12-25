package com.info.service;


import com.info.entity.Employee;
import com.info.exception.EmployeeException;
import com.info.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	// @Autowired
	// private MongoOperations mongoOperations;

	public Mono<Employee> saveEmployee(Employee emp) throws EmployeeException {
		System.out.println("IN Service");
		return employeeRepository.save(emp);
	}

	public Mono<Employee> findById(Long employeeId) throws EmployeeException {

		return employeeRepository.findById(employeeId);
	}

	public Flux<Employee> findAllEmployees() throws EmployeeException {

		return employeeRepository.findAll();
	}

	public Flux<Employee> fetchEmployessBySalary() {
		Query query = new Query().with(Sort.by(Collections.singletonList(Sort.Order.asc("firstName"))));
		query.addCriteria(Criteria.where("salary").gte(30000));

		return reactiveMongoTemplate.find(query, Employee.class);
	}

	public Mono<Employee> updateEmpBySalary(Long employeeId, Double salary) throws EmployeeException {

		Query query = new Query(Criteria.where("id").is(employeeId));
		Update update = new Update().set("salary", 1200);
		Mono<Employee> emp = reactiveMongoTemplate.findAndModify(query, update, Employee.class);

		return emp;

	}

}
