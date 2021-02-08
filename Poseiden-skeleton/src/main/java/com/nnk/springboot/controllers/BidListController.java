package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.IBidListService;
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
public class BidListController {

    @Autowired
    private IBidListService bidListService;


    /**
     * Send bidListDto list.
     *
     * @param model
     * @return The URI to the bidList/list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        log.debug("GET : /bidList/list");
        model.addAttribute("bidList", bidListService.readAll());
        log.debug("GET : /bidList/list - SUCCESS");
        return "bidList/list";
    }

    /**
     * Send bidListDto to save.
     *
     * @param bidListDto
     * @return The URI to the bidList/add
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidListDto bidListDto) {
        log.info("GET : /bidList/add");
        return "bidList/add";
    }

    /**
     * Save a new BidListDto
     *
     * @param bidListDto
     * @param result
     * @param model
     * @return The URI to the bidList/add if result has errors.
     * Else, redirects to /bidList/list endpoint
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidListDto bidListDto, BindingResult result, Model model) {
        log.debug("POST : /bidList/validate");
        if (!result.hasErrors()) {
            bidListService.save(bidListDto);
            log.info("POST : /bidList/add - SUCCES");
            model.addAttribute("bidLists", bidListService.readAll());
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    /**
     * Send to update form an existing BidListDto
     *
     * @param id
     * @param model
     * @return the URI to the bidList/update
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET : /bidList/update/{}", id);
        BidListDto dto = bidListService.readByid(id);
        model.addAttribute("bidList", dto);
        log.info("GET : /bidList/update/" + id + " - SUCCES");
        return "bidList/update";
    }

    /**
     * BidListDto is update
     *
     * @param id
     * @param bidListDto
     * @param result
     * @param model
     * @return The URI to the bidList/update, if result has errors.
     * Else, redirects to /bidList/list endpoint
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidListDto,
                            BindingResult result, Model model) {
        log.debug("POST : /bidList/update/{}", id);

        if (result.hasErrors()) {
            log.info("POST : /bidList/update/{} - ERROR", id);
            return "bidList/update";
        }
        bidListService.update(id, bidListDto);
        model.addAttribute("bidList", bidListService.readAll());
        log.info("POST : /bidList/update/{} - SUCCESS", id);
        return "redirect:/bidList/list";
    }

    /**
     * Find BidList by Id and delete the BidList
     *
     * @param id
     * @param model
     * @return The URI to the bidList/list
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /bidList/delete/{}", id);
        bidListService.delete(id);
        log.info("DELETE : /bidList/delete/{} - SUCCESS", id);
        model.addAttribute("bidList", bidListService.readAll());
        return "redirect:/bidList/list";
    }
}
