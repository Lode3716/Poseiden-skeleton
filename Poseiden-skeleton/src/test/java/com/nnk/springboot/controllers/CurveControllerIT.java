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
    @DisplayName("Given a CurvePointDto, when POST request, then save CurvePointDto check redirect Url is OK and check CurvePointDto is save in BDD")
    public void givenCurvePointDtoAdd_whenPostRequest_thenReturnCurvePointDtoAdd() throws Exception {
        CurvePointDto curvePointDto = new CurvePointDto(1, 10d, 101d);
        CurvePoint curvePoint = new CurvePoint( 1, null, 10d, 101d, null);

        mvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                .sessionAttr("CurvePointDto", curvePointDto)
                .param("curveId", curvePointDto.getCurveId().toString())
                .param("term", curvePointDto.getTerm().toString())
                .param("value", curvePointDto.getValue().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(curve -> String.valueOf(curve.getCurveId()).equals(String.valueOf(curvePoint.getCurveId()))
                        && String.valueOf(curve.getTerm()).equals(String.valueOf(curvePoint.getTerm()))
                        && String.valueOf(curve.getValue()).equals(String.valueOf(curvePoint.getValue()))));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(curve -> {
                    if (String.valueOf(curve.getCurveId()).equals(String.valueOf(curvePoint.getCurveId()))
                            && String.valueOf(curve.getTerm()).equals(String.valueOf(curvePoint.getTerm()))
                            && String.valueOf(curve.getValue()).equals(String.valueOf(curvePoint.getValue()))) {
                        log.info("Check id : " + curve.getId());
                        repository.deleteById(curve.getId());
                    }

                });


    }


    @Test
    @DisplayName("Given a CurvePointDto, when POST request, then save CurvePointDto return error Account is mandatory")
    public void givenCurvePointDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        CurvePointDto curvePointDto = new CurvePointDto(null, 1023d, 101d);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                .sessionAttr("CurvePointDto", curvePointDto)
                .param("curveId", "")
                .param("term", curvePointDto.getTerm().toString())
                .param("value", curvePointDto.getValue().toString()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("curvePoint/add"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Must not be null");
    }

    @Test
    @DisplayName("Given id CurvePoint and biList to update, when post request, then update CurvePoint in BDD")
    public void givenCurvePointDtoUpdate_whenUpdateRequest_deleteIsOk() throws Exception {
        CurvePointDto upDateurvePointDto = new CurvePointDto(25, 10d, 1001d);
        CurvePoint curvePoint = new CurvePoint( 1, null, 10d, 101d, null);
        CurvePoint updateCurvePoint = new CurvePoint( 25, null, 10d, 1001d, null);

        CurvePoint save=repository.save(curvePoint);

        String url = "/curvePoint/update/".concat(String.valueOf(save.getId()));

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("CurvePointDto", upDateurvePointDto)
                .param("curveId", upDateurvePointDto.getCurveId().toString())
                .param("term", upDateurvePointDto.getTerm().toString())
                .param("value", upDateurvePointDto.getValue().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));

        repository.findById(save.getId())
                .ifPresent(curvePoint1->
        {
            assumeTrue (String.valueOf(curvePoint1.getCurveId()).equals(String.valueOf(updateCurvePoint.getCurveId()))
                    && String.valueOf(curvePoint1.getTerm()).equals(String.valueOf(updateCurvePoint.getTerm()))
                    && String.valueOf(curvePoint1.getValue()).equals(String.valueOf(updateCurvePoint.getValue())));

        });

        repository.deleteById(save.getId());
    }


    @Test
    @DisplayName("Given id CurvePoint, when DELETE request, then DELETE in BDD")
    public void givenCurvePointDtoDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        CurvePoint curvePoint = new CurvePoint(253, null, 1056d, 1001d, null);

        CurvePoint save=repository.save(curvePoint);

        String url = "/curvePoint/delete/".concat(String.valueOf(save.getId()));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/curvePoint/list"));
        assumeFalse(repository.existsById(save.getId()));
    }

    @Test
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

}
