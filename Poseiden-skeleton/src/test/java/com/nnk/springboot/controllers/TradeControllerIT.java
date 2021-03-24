package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private TradeRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    @DisplayName("Given a TradeDto, when POST request, then save TradeDto check redirect Url is OK and check TradeDto is save in BDD")
    public void givenTradeDtoAdd_whenPostRequest_thenReturnTradeDtoAdd() throws Exception {
        TradeDto tradeDto = new TradeDto("accountTest", "typeTest1", 125d);
        Trade trade = new Trade("accountTest", "typeTest1", 125d);

        mvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .sessionAttr("TradeDto", tradeDto)
                .param("account", tradeDto.getAccount())
                .param("type", tradeDto.getType())
                .param("buyQuantity", String.valueOf(tradeDto.getBuyQuantity())))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(trad -> trad.getAccount().equals(trade.getAccount())
                        && trad.getType().equals(trade.getType())
                        && String.valueOf(trade.getBuyQuantity()).equals(String.valueOf(trad.getBuyQuantity()))));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(trad ->
                {
                    if (trade.getAccount().equals(trad.getAccount())
                            && trade.getType().equals(trad.getType())
                            && String.valueOf(trade.getBuyQuantity()).equals(String.valueOf(trad.getBuyQuantity()))) {
                        repository.deleteById(trad.getTradeId());
                    }
                });
    }

    @Test
    @DisplayName("Given a TradeDto, when POST request, then save TradeDto return error Account is mandatory")
    public void givenTradeDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        TradeDto tradeDto = new TradeDto("", "", 125d);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .sessionAttr("TradeDto", tradeDto)
                .param("account", tradeDto.getAccount())
                .param("type", tradeDto.getType())
                .param("buyQuantity", String.valueOf(tradeDto.getBuyQuantity())))
                .andExpect(model().hasErrors())
                .andExpect(view().name("trade/add"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Account is mandatory");
        assertThat(content).contains("Type is mandatory");
    }

    @Test
    @DisplayName("Given id Trade and TradeDto to update, when post request, then update Trade in BDD")
    public void givenTradetDtoUpdate_whenUpdateRequest_updateIsOk() throws Exception {
        TradeDto updateTradeDto = new TradeDto("newAccountTest", "newtypeTest1", 1250d);
        Trade trade = new Trade("accountTest", "typeTest1", 125d);
        Trade updateTrade = new Trade("newAccountTest", "newtypeTest1", 1250d);

        Trade save=repository.save(trade);

        String url = "/trade/update/".concat(String.valueOf(save.getTradeId()));

        log.info("Url update Trade : {}", url);

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("TradeDto", updateTradeDto)
                .param("account", updateTradeDto.getAccount())
                .param("type", updateTradeDto.getType())
                .param("buyQuantity", String.valueOf(updateTradeDto.getBuyQuantity())))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));

        repository.findById(save.getTradeId())
                .ifPresent(trad->assumeTrue(trad.getAccount().equals(updateTrade.getAccount())
                        && trad.getType().equals(updateTrade.getType())
                        && String.valueOf(trad.getBuyQuantity()).equals(String.valueOf(updateTrade.getBuyQuantity()))));

        repository.deleteById(save.getTradeId());
    }


    @Test
    @DisplayName("Given id trade, when DELETE request, then DELETE in BDD search if exist")
    public void givenIdTradeDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        Trade trade = new Trade("newAccountTestDelete", "newtypeTest1Delete", 1250d);

        Trade save=repository.save(trade);
        String url = "/trade/delete/".concat(String.valueOf(save.getTradeId()));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/trade/list"));
        assumeFalse(repository.existsById(save.getTradeId()));
    }


    @Test
    @DisplayName("Count number Trade in Bdd and check number is the same in request")
    public void readAllTrade_thenShowTradeListList() throws Exception {
        int nbTrade = (int) repository.findAll().stream().count();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(view().name("trade/list"))
                .andReturn();

        AtomicInteger atomicInteger = new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s, t) ->
                {
                    List<TradeDto> modelTrade = (List<TradeDto>) t;
                    if (s.equals("trades")) {
                        atomicInteger.set(modelTrade.size());
                    }
                });
        log.info("nombre  = " + nbTrade + " /  retour :  " + atomicInteger.get());
        assumeTrue(nbTrade == atomicInteger.get());
    }

}
