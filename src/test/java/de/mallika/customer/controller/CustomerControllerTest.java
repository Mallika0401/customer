package de.mallika.customer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    @DisplayName("Test Customer Api Without Token")
    public void testUnauthorizedResponseWhenNoToken() throws Exception {
        mockMvc.perform(get("/api/customers/1")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test Retrieve Customer Api")
    void testRetrieveCustomerApi() throws Exception {
        String accessToken = obtainAccessToken("1", "12345");
        mockMvc.perform(get("/api/customers/1")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Retrieve All Customers Api")
    void testRetrieveAllCustomerApi() throws Exception {
        String accessToken = obtainAccessToken("2", "12345");
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Update Customers Api")
    void testUpdateCustomerApi() throws Exception {
        String accessToken = obtainAccessToken("1", "12345");
        String customerString = "{\"firstName\":\"milli\",\"lastName\":\"pant\",\"password\":\"$2a$10$FMQOTEUiRN1L2MV2gfYas.MEDnLcEffuenRme5WdFgkwcuWA2jyh\",\"dob\":\"1990-01-01T06:47:52.62\"}";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "1");
        params.add("firstName", "milli");
        params.add("lastName", "pant");
        params.add("password", "$2a$10$FMQOTEUiRN1L2MV2gfYas.MEDnLcEffuenRme5WdFgkwcuWA2jyhG");
        params.add("dob", "1990-01-01T06:47:52.62");

        mockMvc.perform(put("/api/customers/1")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(customerString)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "client");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client", "secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
