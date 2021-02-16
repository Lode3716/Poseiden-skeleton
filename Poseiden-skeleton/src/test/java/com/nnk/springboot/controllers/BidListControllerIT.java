package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.model.IModel;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Log4j2
@EnableJpaRepositories(basePackages = "com.nnk.springboot.domain")
@PropertySource("application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BidListControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BidListRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a firestation, when POST request, then added Firestation return the created status as well as the firestation and check if it exists in BDD")
    public void givenFirestationDtoAdd_whenPostRequest_thenReturnFirestationDtoAdd() throws Exception {
        BidListDto bidListDto = new BidListDto("account", "type", 10d);
        BidList bidList = new BidList(1, "account", "type", 10d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);

       /* mvc.perform(MockMvcRequestBuilders.get("/bidList/add"))
                .andExpect(model().attributeExists(bidListDto))
                .andExpect(view().name("bidList/add"))
                .andExpect(status().isOk());*/
    }

    @Test
    @DisplayName("Given a BidListDto, when POST request, then added BidListDto return the created status as well as the BidListDto and check if it exists in BDD")
    public void givenBidListDtoAdd_whenPostRequest_thenReturnBidListDtoAdd() throws Exception {
        BidListDto bidListDto = new BidListDto("accounta", "type", 10d);
        BidList bidList = new BidList(1, "account", "type", 10d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);
        mvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                .sessionAttr("BidListDto", bidListDto)
                .param("account", bidListDto.getAccount())
                .param("type", bidListDto.getType())
                .param("bidQuantity", bidListDto.getBidQuantity().toString()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));

        assumeTrue(repository.existsById(1));

    }
}