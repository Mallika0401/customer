package de.mallika.customer.service;


import de.mallika.customer.model.CustomerDTO;
import de.mallika.customer.model.UpdateCustomerDTO;
import de.mallika.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @DisplayName("Test Retrieve Customer Service")
    @Test
    void testRetrieveCustomer() {

        CustomerDTO customerDTO = customerService.get("1");
        assertNotNull(customerDTO);
        assertNotNull(customerDTO.getFirstName());
        assertNotNull(customerDTO.getLastName());
        assertNotNull(customerDTO.getLastName());
        assertNotNull(customerDTO.getLastName());
    }

    @DisplayName("Test Retrieve All Customers Service")
    @Test
    void testRetrieveAllCustomers() {
        Pageable paging = PageRequest.of(0, 3, Sort.by("dob").descending());
        List<CustomerDTO> customerList = customerService.getAllCustomers(paging);
        assertFalse(customerList.isEmpty());
        assertEquals(customerList.size(), 3);
    }

    @DisplayName("Test Update Customer Service ")
    @Test
    void testUpdateCustomer() {

        CustomerDTO customerDTO = customerService.get("1");
        UpdateCustomerDTO updateCustomerDTO = UpdateCustomerDTO.builder().firstName("Milli")
                .lastName(customerDTO.getLastName()).password(customerDTO.getPassword())
                .dob(customerDTO.getDob()).build();
        CustomerDTO updatedCustomer = customerService.updateCustomer("1", updateCustomerDTO);
        assertNotNull(updatedCustomer);
        assertEquals(updatedCustomer.getFirstName(), updateCustomerDTO.getFirstName());
        assertEquals(updatedCustomer.getLastName(), updateCustomerDTO.getLastName());
        assertEquals(updatedCustomer.getPassword(), updateCustomerDTO.getPassword());
        assertEquals(updatedCustomer.getDob(), updateCustomerDTO.getDob());
    }

}
