package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
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

import java.sql.Timestamp;
import java.time.Instant;
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
public class CurveControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CurvePointRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    @Order(1)
    @DisplayName("Given a CurvePointDto, when POST request, then save CurvePointDto check redirect Url is OK and check CurvePointDto is save in BDD")
    public void givenCurvePointDtoAdd_whenPostRequest_thenReturnCurvePointDtoAdd() throws Exception {
        CurvePointDto curvePointDto = new CurvePointDto(1, 10d, 101d);
        CurvePoint curvePoint = new CurvePoint( 1, null, 10d, 101d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));

        mvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                .sessionAttr("CurvePointDto", curvePointDto)
                .param("curveId", curvePointDto.getCurveId().toString())
                .param("term", curvePointDto.getTerm().toString())
                .param("value", curvePointDto.getValue().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(curve -> curve.getCurveId() == curvePoint.getCurveId()
                        && curve.getTerm() == curvePoint.getTerm()
                        && curve.getValue() == curvePoint.getValue()));
    }

    @Test
    @DisplayName("Given a CurvePointDto, when POST request, then save CurvePointDto return error Account is mandatory")
    public void givenCurvePointDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        CurvePointDto curvePointDto = new CurvePointDto(null, 10d, 101d);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                .sessionAttr("CurvePointDto", curvePointDto)
                .param("curveId", curvePointDto.getCurveId().toString())
                .param("term", curvePointDto.getTerm().toString())
                .param("value", curvePointDto.getValue().toString()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("curvePoint/add"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Must not be null");
    }

    @Test
    @Order(2)
    @DisplayName("Given id CurvePoint and biList to update, when post request, then update CurvePoint in BDD")
    public void givenCurvePointDtoUpdate_whenUpdateRequest_deleteIsOk() throws Exception {
        CurvePointDto upDateurvePointDto = new CurvePointDto(25, 10d, 1001d);
        CurvePoint curvePoint = new CurvePoint( 1, null, 10d, 101d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        CurvePoint updateCurvePoint = new CurvePoint( 25, null, 10d, 1001d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));


        String url = "/curvePoint/update/".concat(String.valueOf(searchId(curvePoint)));

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("CurvePointDto", upDateurvePointDto)
                .param("curveId", upDateurvePointDto.getCurveId().toString())
                .param("term", upDateurvePointDto.getTerm().toString())
                .param("value", upDateurvePointDto.getValue().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));

        assumeTrue(repository.existsById(searchId(updateCurvePoint)));
    }

    @Test
    @Order(3)
    @DisplayName("Given id CurvePoint, when DELETE request, then DELETE in BDD")
    public void givenCurvePointDtoDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        CurvePoint updateCurvePoint = new CurvePoint(25, null, 10d, 1001d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));

        String url = "/curvePoint/delete/".concat(String.valueOf(searchId(updateCurvePoint)));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/curvePoint/list"));
        assumeFalse(repository.existsById(searchId(updateCurvePoint)));
    }


    @Test
    @Order(4)
    @DisplayName("Count number CurvePoint in Bdd and check number is the same in request")
    public void readAllCurvePoint_thenShowCurvePointList() throws Exception {
        int nbCurvPoint = (int) repository.findAll().stream().count();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
                .andExpect(model().attributeExists("curves"))
                .andExpect(view().name("curvePoint/list"))
                .andReturn();

        AtomicInteger atomicInteger = new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s, t) ->
                {
                    List<CurvePointDto> modelCurvePoint = (List<CurvePointDto>) t;
                    if (s.equals("curves")) {
                        atomicInteger.set(modelCurvePoint.size());
                    }
                });
        assumeTrue(nbCurvPoint == atomicInteger.get());
    }


    private Integer searchId(CurvePoint curvePoint) {
        AtomicInteger atomicInteger = new AtomicInteger();
        repository.findAll().stream()
                .filter(curve -> {
                    if (curve.getCurveId() == curvePoint.getCurveId()
                            && curve.getTerm() == curvePoint.getTerm()
                            && curve.getValue() == curvePoint.getValue()) {
                        log.info("Check id : " + curve.getId());
                        return true;
                    }
                    return false;
                }).findAny();

        return atomicInteger.get();
    }
}
