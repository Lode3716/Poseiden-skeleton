package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.exceptions.RuleNameNotFoundException;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@Log
@RunWith(MockitoJUnitRunner.class)
public class RuleNameServiceTest {

    @InjectMocks
    RuleNameService ruleNameService;

    @Mock
    RuleNameRepository ruleNameRepository;

    @Mock
    JMapper<RuleNameDto, RuleName> ruleNameJMapper;

    @Mock
    JMapper<RuleName, RuleNameDto> ruleNameUnJMapper;

    private static RuleNameDto ruleNameDto1;

    private static RuleNameDto ruleNameDto2;

    private static RuleName ruleName1;

    private static RuleName ruleName2;

    private static List<RuleNameDto> listRuleNameDto;

    @Before
    public void setUp() throws Exception {
        ruleNameDto1 = new RuleNameDto(1, "name", "description", "json",
                "template", "sqlStr", "sqlPart");
        ruleNameDto2 = new RuleNameDto(2, "name", "description", "json",
                "template", "sqlStr", "sqlPart");
        ruleName1 = new RuleName(1, "name", "description", "json",
                "template", "sqlStr", "sqlPart");
        ruleName2 = new RuleName(2, "name", "description", "json",
                "template", "sqlStr", "sqlPart");
        listRuleNameDto = Arrays.asList(ruleNameDto1, ruleNameDto2);
    }

    @Test
    public void givenSearchListOfRuleNameDto_whenAllRuleName_thenReturnListOfRuleNameDto() {


        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(ruleName1, ruleName2));
        when(ruleNameJMapper.getDestination(ruleName1)).thenReturn(ruleNameDto1);
        when(ruleNameJMapper.getDestination(ruleName2)).thenReturn(ruleNameDto2);

        List<RuleNameDto> result = ruleNameService.readAll();

        assertThat(result).isEqualTo(listRuleNameDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(ruleNameRepository, ruleNameJMapper);
        inOrder.verify(ruleNameRepository).findAll();
        inOrder.verify(ruleNameJMapper).getDestination(ruleName1);
        inOrder.verify(ruleNameJMapper).getDestination(ruleName2);
    }

    @Test
    public void givenRuleNameDto_whenSaveRuleName_thenRuleNameIsSavedCorrectly() {
        RuleNameDto ruleNameDto = new RuleNameDto("name", "description", "json",
                "template", "sqlStr", "sqlPart");
        RuleName ruleName = new RuleName("name", "description", "json",
                "template", "sqlStr", "sqlPart");

        when(ruleNameUnJMapper.getDestination(any(RuleNameDto.class))).thenReturn(ruleName);
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName1);
        when(ruleNameJMapper.getDestination(any(RuleName.class))).thenReturn(ruleNameDto1);

        RuleNameDto asSave = ruleNameService.save(ruleNameDto);

        assertThat(asSave).isEqualToComparingFieldByField(ruleNameDto1);
        InOrder inOrder = inOrder(ruleNameUnJMapper, ruleNameRepository, ruleNameJMapper);
        inOrder.verify(ruleNameUnJMapper).getDestination(any(RuleNameDto.class));
        inOrder.verify(ruleNameRepository).save(any(RuleName.class));
        inOrder.verify(ruleNameJMapper).getDestination(any(RuleName.class));
    }

    @Test
    public void givenIdRuleAndRuleNameDto_whenUpdateRuleName_thenRuleNameIsUpdateCorrectly() {
        RuleName updateRuleName = new RuleName(1, "nameUpdate", "descriptionUpdate", "json",
                "template", "sqlStrUpdate", "sqlPartUpdate");
        RuleNameDto updateDto = new RuleNameDto(1, "nameUpdate", "descriptionUpdate", "json",
                "template", "sqlStrUpdate", "sqlPartUpdate");

        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ruleName1));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(updateRuleName);
        when(ruleNameJMapper.getDestination(any(RuleName.class))).thenReturn(updateDto);

        RuleNameDto result = ruleNameService.update(1, new RuleNameDto(1, "nameUpdate", "descriptionUpdate", "json",
                "template", "sqlStrUpdate", "sqlPartUpdate"));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(ruleNameRepository, ruleNameJMapper);
        inOrder.verify(ruleNameRepository).findById(anyInt());
        inOrder.verify(ruleNameRepository).save(any(RuleName.class));
        inOrder.verify(ruleNameJMapper).getDestination(any(RuleName.class));
    }

    @Test
    public void givenIdRuleDto_whenDeleteRuleName_thenBisListIsDeleteCorrectly() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ruleName1));

        ruleNameService.delete(anyInt());

        InOrder inOrder = inOrder(ruleNameRepository);
        inOrder.verify(ruleNameRepository).findById(anyInt());
        inOrder.verify(ruleNameRepository).deleteById(anyInt());
    }

    @Test(expected = RuleNameNotFoundException.class)
    public void givenUnFoundRuleName_whenDeleteRuleName_thenRuleNameNotFoundException() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        ruleNameService.delete(anyInt());
    }

    @Test
    public void givenIdRuleDto_whenFoundRuleName_thenReturnRuleNameFound() {
        RuleName RuleNameFind = new RuleName(1, "name", "description", "json",
                "template", "sqlStr", "sqlPart");

        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.of(RuleNameFind));

        RuleName result = ruleNameService.existById(1);

        assertThat(result).isEqualTo(ruleName1);
    }

    @Test(expected = RuleNameNotFoundException.class)
    public void givenUnFoundIdRuleDto_whenFoundRuleName_thenRuleNameNotFoundException() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        ruleNameService.existById(anyInt());

    }
}