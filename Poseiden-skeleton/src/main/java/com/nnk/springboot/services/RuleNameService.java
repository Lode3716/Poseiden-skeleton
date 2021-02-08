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


    /**
     *
     * Convert a RuleNameDto to RuleName and save it in the database.
     * When it's recorded, we return here.
     *
     * @param ruleNameDto
     * @return
     */
    @Override
    public RuleNameDto save(RuleNameDto ruleNameDto) {
        RuleName rl = ruleNameRepository.save(ruleNameUnJMapper.getDestination(ruleNameDto));
        log.debug("Service : RuleName is save in Bdd : {} ", rl);
        return ruleNameJMapper.getDestination(rl);
    }


    /**
     * Find list RuleName and Convert RuleNameDto
     *
     * @return the list of RuleNameDto
     */
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

    /**
     * Check id exist, if valid update RuleName
     *
     * @param id
     * @param ruleNameDto to update
     * @return the RuleName update and converted the RuleNameDto
     */
    @Override
    public RuleNameDto update(Integer id,RuleNameDto ruleNameDto) {
        log.info("Affiche : {}",ruleNameDto);
        RuleName updateRuleName = existById(id);
        updateRuleName.setName(ruleNameDto.getName());
        updateRuleName.setDescription(ruleNameDto.getDescription());
        updateRuleName.setJson(ruleNameDto.getJson());
        updateRuleName.setSqlPart(ruleNameDto.getSqlPart());
        updateRuleName.setSqlStr(ruleNameDto.getSqlStr());
        updateRuleName.setTemplate(ruleNameDto.getTemplate());
        log.debug("Service : update ruleName : {} ", updateRuleName.getId());
        return ruleNameJMapper.getDestination(ruleNameRepository.save(updateRuleName));
    }

    /**
     * Check id exist, if valid delete ruleName
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        ruleNameRepository.deleteById(existById(id).getId());
        log.info("Service delete ruleName id : {}",id);
    }

    /**
     * Find Rule  By id
     * @param id
     * @return the RuleNameDto find or issue IllegalArgumentException
     */
    @Override
    public RuleNameDto readByid(Integer id) {
        RuleName findRuleNameId= ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));
        log.info("Service : Read by Id ruleName - SUCCESS");
        return ruleNameJMapper.getDestination(findRuleNameId);
    }

    /**
     * Find RuleName By id
     * @param id
     * @return the RuleName find or issue RuleNameNotFoundException
     */
    public RuleName existById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new RuleNameNotFoundException("There is no ruleName with this id " + id));
    }
}
