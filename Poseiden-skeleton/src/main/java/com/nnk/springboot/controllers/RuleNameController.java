package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.services.IRuleNameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Log4j2
@Controller
public class RuleNameController {


    @Autowired
    private IRuleNameService ruleNameService;

    /**
     * Send RatingDto list.
     *
     * @param model
     * @return The URI to the ruleName/list
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        log.debug("GET : /ruleName/list");
        model.addAttribute("rules", ruleNameService.readAll());
        log.debug("GET : /ruleName/list - SUCCESS");
        return "ruleName/list";
    }

    /**
     * Send ruleNameDto to save.
     *
     * @param ruleNameDto
     * @return The URI to the ruleName/add
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleNameDto ruleNameDto) {
        log.debug("GET : /ruleName/add");
        return "ruleName/add";
    }

    /**
     * Send ruleNameDto to save.
     *
     * @param ruleNameDto
     * @param result
     * @param model
     * @return The URI to the ruleName/add
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleNameDto ruleNameDto, BindingResult result, Model model) {
        log.debug("POST : /ruleName/validate");
        if (!result.hasErrors()) {
            ruleNameService.save(ruleNameDto);
            log.info("POST : /ruleName/add - SUCCES");
            model.addAttribute("rule", ruleNameService.readAll());
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    /**
     * Send to update form an existing ruleNameDto
     *
     * @param id
     * @param model
     * @return the URI to the ruleName/update
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET : /ruleName/update/{}", id);
        RuleNameDto dto = ruleNameService.readByid(id);
        model.addAttribute("rule", dto);
        return "ruleName/update";
    }

    /**
     * RuleNameDto is update
     *
     * @param id
     * @param ruleNameDto
     * @param result
     * @param model
     * @return The URI to the ruleName/update, if result has errors.
     * Else, redirects to /ruleName/list endpoint
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameDto ruleNameDto,
                                 BindingResult result, Model model) {
        log.debug("POST : /ruleName/update/{}", id);

        if (result.hasErrors()) {
            log.info("POST : /ruleName/update/{} - ERROR", id);
            model.addAttribute("rule", ruleNameDto);
            return "ruleName/update";
        }
        ruleNameService.update(id, ruleNameDto);
        model.addAttribute("rule", ruleNameService.readAll());
        log.info("POST : /ruleName/update/{} - SUCCESS", id);
        return "redirect:/ruleName/list";
    }

    /**
     * Find RuleName by Id and delete the RuleName
     *
     * @param id
     * @param model
     * @return The URI to the ruleName/list
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /ruleName/delete/{}", id);
        ruleNameService.delete(id);
        log.info("DELETE : /ruleName/delete/{} - SUCCESS", id);
        model.addAttribute("rule", ruleNameService.readAll());
        return "redirect:/ruleName/list";
    }
}
