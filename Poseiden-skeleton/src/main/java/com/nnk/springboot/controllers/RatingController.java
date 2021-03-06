package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.services.IRatingService;
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
public class RatingController {


    @Autowired
    private IRatingService ratingService;

    /**
     * Send ratingDto list.
     *
     * @param model
     * @return The URI to the rating/list
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        log.debug("GET : /rating/list");
        model.addAttribute("ratings", ratingService.readAll());
        log.debug("GET : /rating/list - SUCCESS");
        return "rating/list";
    }


    /**
     * Send ratingDto to save.
     *
     * @param ratingDto
     * @return The URI to the rating/add
     */
    @GetMapping("/rating/add")
    public String addRatingForm(RatingDto ratingDto) {
        log.debug("GET : /rating/add");
        return "rating/add";
    }


    /**
     * Save a new ratingDto
     *
     * @param ratingDto new Rating new curvePoint to save
     * @param result check entity validation
     * @param model the entity
     * @returnThe URI to the rating/add if result has errors.
     * Else, redirects to /rating/list endpoint
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model) {
        log.debug("POST : /rating/validate");
        if (!result.hasErrors()) {
            ratingService.save(ratingDto);
            log.info("POST : /rating/add - SUCCES");
            model.addAttribute("rating", ratingService.readAll());
            return "redirect:/rating/list";
        }
        log.error("POST : rating/add - ERROR");
        return "rating/add";
    }

    /**
     * Send to update form an existing ratingDto
     *
     * @param id to update rating
     * @param model the entity
     * @return the URI to the rating/update
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET : /rating/update/{}", id);
        RatingDto dto = ratingService.readByid(id);
        model.addAttribute("rating", dto);
        return "rating/update";
    }

    /***
     *
     * RatingDTO is update
     *
     * @param id to update rating
     * @param RatingDto the entity update
     * @param result check validation
     * @param model the entity
     * @return The URI to the rating/update, if result has errors.
     * Else, redirects to /rating/list endpoint
     *
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto ratingDto,
                               BindingResult result, Model model) {
        log.debug("POST : /rating/update/{}", id);

        if (result.hasErrors()) {
            log.error("POST : /rating/update/{} - ERROR", id);
            model.addAttribute("rating", ratingDto);
            return "rating/update";
        }
        ratingService.update(id, ratingDto);
        model.addAttribute("rating", ratingService.readAll());
        log.info("POST : /rating/update/{} - SUCCESS", id);
        return "redirect:/rating/list";
    }


    /**
     * Find rating by Id and delete the rating
     *
     * @param id to delete rating
     * @param model list entity
     * @return The URI to the rating/list
     *
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /rating/delete/{}", id);
        ratingService.delete(id);
        log.info("DELETE : /rating/delete/{} - SUCCESS", id);
        model.addAttribute("rating", ratingService.readAll());
        return "redirect:/rating/list";
    }
}
