package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.services.ITradeService;
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
public class TradeController {


    @Autowired
    private ITradeService tradeService;

    /**
     * Send tradeDto list.
     *
     * @param model
     * @return The URI to the trade/list
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        log.debug("GET : /trade/list");
        model.addAttribute("trades", tradeService.readAll());
        log.debug("GET : /trade/list - SUCCESS");
        return "trade/list";
    }

    /**
     * Send TradeDto to save.
     *
     * @param tradeDto
     * @return The URI to the trade/add
     */
    @GetMapping("/trade/add")
    public String addTrade(TradeDto tradeDto) {
        log.debug("GET : /trade/add");
        return "trade/add";
    }

    /**
     * Save a new TradeDto
     *
     * @param tradeDto
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid TradeDto tradeDto, BindingResult result, Model model) {
        log.debug("POST : /trade/validate");
        if (!result.hasErrors()) {
            tradeService.save(tradeDto);
            log.info("POST : /trade/add - SUCCES");
            model.addAttribute("trade", tradeService.readAll());
            return "redirect:/trade/list";
        }
        return "trade/add";
    }


    /**
     * Send to update form an existing tradeDto
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET : /trade/update/{}", id);
        TradeDto dto = tradeService.readByid(id);
        model.addAttribute("trade", dto);
        return "trade/update";
    }

    /**
     * TradeDto is update
     *
     * @param id
     * @param tradeDto
     * @param result
     * @param model
     * @return The URI to the trade/update, if result has errors.
     * Else, redirects to /trade/list endpoint
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto tradeDto,
                              BindingResult result, Model model) {
        log.debug("POST : /trade/update/{}", id);

        if (result.hasErrors()) {
            log.info("POST : /trade/update/{} - ERROR", id);
            model.addAttribute("trade", tradeDto);
            return "trade/update";
        }
        tradeService.update(id, tradeDto);
        model.addAttribute("trade", tradeService.readAll());
        log.info("POST : /trade/update/{} - SUCCESS", id);
        return "redirect:/trade/list";
    }

    /**
     * Find Trade by Id and delete the trade
     *
     * @param id
     * @param model
     * @return The URI to the trade/list
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /trade/delete/{}", id);
        tradeService.delete(id);
        log.info("DELETE : /trade/delete/{} - SUCCESS", id);
        model.addAttribute("trade", tradeService.readAll());
        return "redirect:/trade/list";
    }
}
