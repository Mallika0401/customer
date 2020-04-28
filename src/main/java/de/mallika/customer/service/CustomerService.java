package de.mallika.customer.service;

import de.mallika.customer.entity.Customer;
import de.mallika.customer.exception.CustomerNotFoundException;
import de.mallika.customer.model.CustomerDTO;
import de.mallika.customer.model.UpdateCustomerDTO;
import de.mallika.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDTO get(String id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new CustomerNotFoundException();
        }
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = optionalCustomer.get();
        log.info("Found customer with id: {}", customer.getId());
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public CustomerDTO updateCustomer(String id, UpdateCustomerDTO updateCustomer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new CustomerNotFoundException();
        }
        Customer customer = optionalCustomer.get();
        BeanUtils.copyProperties(updateCustomer, customer);
        customerRepository.save(customer);
        log.info("Update customer with id: {}", customer.getId());
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public List<CustomerDTO> getAllCustomers(Pageable paging) {

        Page<Customer> pagedResult = customerRepository.findAll(paging);
        List<CustomerDTO> customers = new ArrayList<>();
        CustomerDTO customerDTO;
        for (Customer loan : pagedResult.getContent()) {
            customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(loan, customerDTO);
            customers.add(customerDTO);
        }
        return customers;
    }

}

