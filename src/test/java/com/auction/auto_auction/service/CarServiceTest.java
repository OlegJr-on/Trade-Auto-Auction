package com.auction.auto_auction.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("qa")
@AutoConfigureMockMvc
@AllArgsConstructor
public class CarServiceTest {

    private MockMvc mvc;

    @MockBean
    CarService carService;

    @Test
    public void justFirstTest(){

        assertTrue(true);
    }
}
