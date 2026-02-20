package com.info.service;


import com.info.entity.Employee;
import com.info.exception.ApplicationError;
import com.info.exception.ApplicationException;
import com.info.exception.ErrorCode;
import com.info.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@Profile("!local")
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;


	public Mono<Employee> saveEmployee(Employee emp)  {
		System.out.println("IN Service");
		return employeeRepository.save(emp);
	}

	public Mono<Employee> findById(Long employeeId)  {

		return employeeRepository.findById(employeeId)
				.switchIfEmpty(Mono.defer(() -> {
					System.out.println("employee id not found.");
					ApplicationError error = new ApplicationError();
					error.setCode(ErrorCode.ERR_400_01.getCode());
					error.setMessage(ErrorCode.ERR_400_01.getMessage());
					error.setDetail(ErrorCode.ERR_400_01.getMessage());

					return Mono.error(new  ApplicationException(HttpStatus.BAD_REQUEST, "validation failed",error));
				}))
				.onErrorResume(throwable -> {
					throwable.printStackTrace();
					ApplicationError error = new ApplicationError();
					error.setCode(ErrorCode.ERR_500_00.getCode());
					error.setMessage(ErrorCode.ERR_500_00.getMessage());
					error.setDetail(ErrorCode.ERR_500_00.getMessage());

					return Mono.error(new  ApplicationException(HttpStatus.BAD_REQUEST, "validation failed",error));
				})
				.map(employee -> {
					System.out.println("Emp id is found:");
					return employee;
				});
	}

	public Flux<Employee> findAllEmployees()  {

		return employeeRepository.findAll();
	}

	public Flux<Employee> fetchEmployessBySalary() {
		Query query = new Query().with(Sort.by(Collections.singletonList(Sort.Order.asc("firstName"))));
		query.addCriteria(Criteria.where("salary").gte(30000));

		return reactiveMongoTemplate.find(query, Employee.class);
	}

	public Mono<Employee> updateEmpBySalary(Long employeeId, Double salary) {

		Query query = new Query(Criteria.where("id").is(employeeId));
		Update update = new Update().set("salary", 1200);
		Mono<Employee> emp = reactiveMongoTemplate.findAndModify(query, update, Employee.class);

		return emp;

	}

}
