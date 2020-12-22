package com.nnk.springboot.services;


import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.exceptions.RuleNameNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class RuleNameService implements IRuleNameService {

    @Autowired
    RuleNameRepository ruleNameRepository;

    @Autowired
    JMapper<RuleNameDto, RuleName> ruleNameJMapper;

    @Autowired
    JMapper<RuleName, RuleNameDto> ruleNameUnJMapper;


    @Override
    public RuleNameDto save(RuleNameDto ruleNameDto) {
        RuleName rl = ruleNameRepository.save(ruleNameUnJMapper.getDestination(ruleNameDto));
        log.debug("Service : RuleName is save in Bdd : {} ", rl);
        return ruleNameJMapper.getDestination(rl);
    }

    @Override
    public List<RuleNameDto> readAll() {
        List<RuleNameDto> ruleNameDtoList = new ArrayList<>();
        ruleNameRepository.findAll()
                .forEach(ruleName ->
                {
                    ruleNameDtoList.add(ruleNameJMapper.getDestination(ruleName));
                });
        log.debug("Service : create list RuleNameDto : {} ", ruleNameDtoList.size());

        return ruleNameDtoList;
    }

    @Override
    public RuleNameDto update(RuleNameDto ruleNameDto) {
        RuleName updateRuleName = existById(ruleNameDto.getId());
        updateRuleName.setName(ruleNameDto.getName());
        updateRuleName.setDescription(ruleNameDto.getDescription());
        updateRuleName.setJson(ruleNameDto.getJson());
        updateRuleName.setSqlPart(ruleNameDto.getSqlPart());
        updateRuleName.setSqlStr(ruleNameDto.getSqlStr());
        updateRuleName.setTemplate(ruleNameDto.getTemplate());
        return ruleNameJMapper.getDestination(ruleNameRepository.save(updateRuleName));
    }

    @Override
    public void delete(RuleNameDto ruleNameDto) {
        ruleNameRepository.deleteById(ruleNameDto.getId());
    }

    public RuleName existById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new RuleNameNotFoundException("There is no ruleName with this id " + id));
    }
}
