package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.ICurveService;
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
public class CurveController {

    @Autowired
    private ICurveService curveService;

    /**
     * Send CurvePointDto list.
     *
     * @param model
     * @return
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        log.debug("GET : /curvePoint/list");
        model.addAttribute("curves", curveService.readAll());
        log.debug("GET : /curvePoint/list - SUCCESS");
        return "curvePoint/list";
    }

    /**
     * Send CurvePointDto to save.
     *
     * @param curvePoint
     * @return The URI to the curvePoint/add
     */
    @GetMapping("/curvePoint/add")
    public String addcurvePointForm(CurvePointDto curvePointDto) {
        log.debug("GET : curvePoint/add");
        return "curvePoint/add";
    }

    /**
     * Save a new CurvePointDto
     *
     * @param curvePoint
     * @param result
     * @param model
     * @return The URI to the curvePoint/add if result has errors.
     * Else, redirects to /curvePoint/list endpoint
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePointDto curvePointDto, BindingResult result, Model model) {
        log.debug("POST : /curvePoint/validate");
        if (!result.hasErrors()) {
            curveService.save(curvePointDto);
            log.info("POST : /curvePoint/add - SUCCES");
            model.addAttribute("curves", curveService.readAll());
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    /**
     * Send to update form an existing curvePointDto
     *
     * @param id
     * @param model
     * @return the URI to the curvePoint/update
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET : /curvePoint/update/{}", id);
        CurvePointDto dto = curveService.readByid(id);
        model.addAttribute("curvePoint", dto);
        log.info("GET : /curvePoint/update/" + id + " - SUCCES");
        return "curvePoint/update";
    }

    /**
     * CurveListDto is update
     *
     * @param id
     * @param curvePoint
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePointDto curvePointDto,
                                   BindingResult result, Model model) {
        log.debug("POST : /curvePoint/update/{}", id);

        if (result.hasErrors()) {
            log.info("POST : /curvePoint/update/{} - ERROR", id);
            return "curvePoint/update";
        }
        curveService.update(id, curvePointDto);
        model.addAttribute("curvePoint", curveService.readAll());
        log.info("POST : /curvePoint/update/{} - SUCCESS", id);
        return "redirect:/curvePoint/list";
    }

    /**
     * Find curve by Id and delete the curve
     *
     * @param id
     * @param model
     * @return The URI to the curvePoint/list
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /curvePoint/delete/{}", id);
        curveService.delete(id);
        log.info("DELETE : /curvePoint/delete/{} - SUCCESS", id);
        model.addAttribute("curve", curveService.readAll());
        return "redirect:/curvePoint/list";
    }
}
