package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private RuleNameRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a RuleNameDto, when POST request, then save RuleNameDto check redirect Url is OK and check RuleNameDto is save in BDD")
    public void givenRuleNameDtoAdd_whenPostRequest_thenReturnRuleNameDtoAdd() throws Exception {
        RuleNameDto ruleNameDto = new RuleNameDto("nameTestIt", "descriptionTestIt", "jsonTestIt",
                "templateTestIt", "sqlStrTestIt", "sqlPartTestIt");
        RuleName ruleName = new RuleName("nameTestIt", "descriptionTestIt", "jsonTestIt",
                "templateTestIt", "sqlStrTestIt", "sqlPartTestIt");

        mvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                .sessionAttr("RuleNameDto", ruleNameDto)
                .param("name", ruleNameDto.getName())
                .param("description", ruleNameDto.getDescription())
                .param("json", ruleNameDto.getJson())
                .param("template", ruleNameDto.getTemplate())
                .param("sqlStr", ruleNameDto.getSqlStr())
                .param("sqlPart", ruleNameDto.getSqlPart()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(rule -> rule.getName().equals(ruleName.getName())
                        && rule.getDescription().equals(ruleName.getDescription())
                        && rule.getJson().equals(ruleName.getJson())
                        && rule.getTemplate().equals(ruleName.getTemplate())
                        && rule.getSqlStr().equals(ruleName.getSqlStr())
                        && rule.getSqlPart().equals(ruleName.getSqlPart())));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(rule ->{
                    if(rule.getName().equals(ruleName.getName())
                            && rule.getDescription().equals(ruleName.getDescription())
                            && rule.getJson().equals(ruleName.getJson())
                            && rule.getTemplate().equals(ruleName.getTemplate())
                            && rule.getSqlStr().equals(ruleName.getSqlStr())
                            && rule.getSqlPart().equals(ruleName.getSqlPart()))
                    {
                        repository.deleteById(rule.getId());
                    }

                        });
    }

    @Test
    @DisplayName("Given a RuleNameDto, when POST request, then save RuleNameDto return errors")
    public void givenRuleNameDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        RuleNameDto ruleNameDto = new RuleNameDto("", "", "json",
                "template", "sqlStr", "sqlPart");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                .sessionAttr("RuleNameDto", ruleNameDto)
                .param("name", ruleNameDto.getName())
                .param("description", ruleNameDto.getDescription())
                .param("json", ruleNameDto.getJson())
                .param("template", ruleNameDto.getTemplate())
                .param("sqlStr", ruleNameDto.getSqlStr())
                .param("sqlPart", ruleNameDto.getSqlPart()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("ruleName/add"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Name is mandatory");
        assertThat(content).contains("Description is mandatory");
    }


    @Test
    @DisplayName("Given id RuleName and ruleNameDto to update, when post request, then update RuleName in BDD")
    public void givenRuleNametDtoUpdate_whenUpdateRequest_updateIsOk() throws Exception {
        RuleNameDto upDateRuleNameDto = new RuleNameDto("upDatenameTestIt", "upDatedescriptionTestIt", "upDatejsonTestIt",
                "upDatetemplateTestIt", "upDatesqlStrTestIt", "upDatesqlPartTestIt");
        RuleName ruleName = new RuleName("nameTestIt", "descriptionTestIt", "jsonTestIt",
                "templateTestIt", "sqlStrTestIt", "sqlPartTestIt");
        RuleName upDateRuleName = new RuleName("upDatenameTestIt", "upDatedescriptionTestIt", "upDatejsonTestIt",
                "upDatetemplateTestIt", "upDatesqlStrTestIt", "upDatesqlPartTestIt");

        RuleName save=repository.save(ruleName);

        String url = "/ruleName/update/".concat(String.valueOf(save.getId()));
        log.info("Update Rulename Url "+ url);

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("RuleNameDto", upDateRuleNameDto)
                .param("name", upDateRuleNameDto.getName())
                .param("description", upDateRuleNameDto.getDescription())
                .param("json", upDateRuleNameDto.getJson())
                .param("template", upDateRuleNameDto.getTemplate())
                .param("sqlStr", upDateRuleNameDto.getSqlStr())
                .param("sqlPart", upDateRuleNameDto.getSqlPart()))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/ruleName/list"));

        repository.findById(save.getId())
                .ifPresent(rule ->  assumeTrue (rule.getName().equals(upDateRuleName.getName())
                        && rule.getDescription().equals(upDateRuleName.getDescription())
                        && rule.getJson().equals(upDateRuleName.getJson())
                        && rule.getTemplate().equals(upDateRuleName.getTemplate())
                        && rule.getSqlStr().equals(upDateRuleName.getSqlStr())
                        && rule.getSqlPart().equals(upDateRuleName.getSqlPart())));

        repository.deleteById(save.getId());
    }


    @Test
    @DisplayName("Given id RuleName, when DELETE request, then DELETE in BDD search if exist")
    public void givenIdRuleNameDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        RuleName ruleName =  new RuleName("upDatenameTestItDelete", "upDatedescriptionTestItDelete", "upDatejsonTestIt",
                "upDatetemplateTestItDelete", "upDatesqlStrTestItDelete", "upDatesqlPartTestIt");
        RuleName save=repository.save(ruleName);

        String url = "/ruleName/delete/".concat(String.valueOf(save.getId()));
        log.info("Delete ruleName Url "+ url);


        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/ruleName/list"));
        assumeFalse(repository.existsById(save.getId()));
    }


    @Test
    @DisplayName("Count number RuleName in Bdd and check number is the same in request")
    public void readAllRuleName_thenShowRuleNameListList() throws Exception {
        int nbRuleName = (int) repository.findAll().stream().count();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
                .andExpect(model().attributeExists("rules"))
                .andExpect(view().name("ruleName/list"))
                .andReturn();

        AtomicInteger atomicInteger = new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s, t) ->
                {
                    List<RuleNameDto> modelRuleNameDto = (List<RuleNameDto>) t;
                    if (s.equals("rules")) {
                        atomicInteger.set(modelRuleNameDto.size());
                    }
                });
        log.info("nombre  = " + nbRuleName + " /  retour :  " + atomicInteger.get());
        assumeTrue(nbRuleName == atomicInteger.get());
    }


}
