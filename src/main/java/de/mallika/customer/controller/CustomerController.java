package de.mallika.customer.controller;

import de.mallika.customer.model.CustomerDTO;
import de.mallika.customer.model.UpdateCustomerDTO;
import de.mallika.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> read(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(customerService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") String id,
                                            @Valid @RequestBody UpdateCustomerDTO customerDTO) {
        return ResponseEntity.ok().body(customerService.updateCustomer(id,customerDTO));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "dob") String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        List<CustomerDTO> list = customerService.getAllCustomers(paging);
        return ResponseEntity.ok().body(list);
    }
}
