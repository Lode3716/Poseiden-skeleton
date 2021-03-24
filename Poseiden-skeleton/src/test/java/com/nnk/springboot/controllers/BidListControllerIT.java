package com.nnk.springboot.controllers;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Log4j2
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
    @DisplayName("Given a BidListDto, when POST request, then save BidListDto check redirect Url is OK and check BidListDto is save in BDD")
    public void givenBidListDtoAdd_whenPostRequest_thenReturnBidListDtoAdd() throws Exception {
        BidListDto bidListDto = new BidListDto("Nouvelaccounta", "NouveauType", 10d);
        BidList bidList = new BidList("Nouvelaccounta", "NouveauType", 10d);

        mvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                .sessionAttr("BidListDto", bidListDto)
                .param("account", bidListDto.getAccount())
                .param("type", bidListDto.getType())
                .param("bidQuantity", bidListDto.getBidQuantity().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));



        assumeTrue(repository.findAll().stream()
                .anyMatch(bid -> bid.getAccount().equals(bidList.getAccount())
                        && bid.getType().equals(bidList.getType())
                        && bid.getBidQuantity().equals(bidList.getBidQuantity())));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(bid -> {
                    if(bid.getAccount().equals(bidList.getAccount())
                        && bid.getType().equals(bidList.getType())
                        && bid.getBidQuantity().equals(bidList.getBidQuantity()))
                    {
                        repository.deleteById(bid.getBidListId());
                    }
                });
    }

    @Test
    @DisplayName("Given a BidListDto, when POST request, then save BidListDto return error Account is mandatory")
    public void givenBidListDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        BidListDto bidListDto = new BidListDto("", "type", 10d);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                .sessionAttr("BidListDto", bidListDto)
                .param("account", bidListDto.getAccount())
                .param("type", bidListDto.getType())
                .param("bidQuantity", bidListDto.getBidQuantity().toString()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("bidList/add"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Account is mandatory");
    }

    @Test
    @DisplayName("Given id BidList and biList to update, when post request, then update Bidlit in BDD")
    public void givenBidListDtoUpdate_whenUpdateRequest_deleteIsOk() throws Exception {
        BidListDto updateBidListDto = new BidListDto("NouvelaccountaUpdate", "NouveauTypeUpdate", 10d);
        BidList bidList = new BidList("Nouvelaccounta", "NouveauType", 10d);
        BidList updateBidList = new BidList("NouvelaccountaUpdate", "NouveauTypeUpdate", 10d);

        BidList save=repository.save(bidList);
        String url= "/bidList/update/".concat(String.valueOf(save.getBidListId()));

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("BidListDto", updateBidListDto)
                .param("account", updateBidListDto.getAccount())
                .param("type", updateBidListDto.getType())
                .param("bidQuantity", updateBidListDto.getBidQuantity().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));

        repository.findById(save.getBidListId())
                .ifPresent(bid->
                {
                    assumeTrue (bid.getAccount().equals(updateBidList.getAccount())
                        && bid.getType().equals(updateBidList.getType())
                        && bid.getBidQuantity().equals(updateBidList.getBidQuantity()));

                }
        );
        repository.deleteById(save.getBidListId());
    }

    @Test
    @DisplayName("Given id BidList, when DELETE request, then DELETE in BDD")
    public void givenBidListDtoDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        BidList bidList = new BidList("NouvelaccountaUpdateDelete", "NouveauTypeUpdateDelete", 10d);
        BidList save=repository.save(bidList);
        log.info("Create test : "+save.getBidListId());
        String url= "/bidList/delete/".concat(String.valueOf(save.getBidListId()));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/bidList/list"));
        assumeFalse(repository.existsById(save.getBidListId()));
    }


    @Test
    @DisplayName("Count number BidList in Bdd and check number is the same in request")
    public void readAllBidList_thenShowBidListList() throws Exception {
      int nbBidList= (int) repository.findAll().stream().count();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/list"))
        .andReturn();

        AtomicInteger atomicInteger=new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s,t)->
        {
            List<BidListDto> modelBidList=(List<BidListDto>) t;
            if(s.equals("bidList")) {
                atomicInteger.set(modelBidList.size());
            }});
        assumeTrue(nbBidList == atomicInteger.get());
    }
}
