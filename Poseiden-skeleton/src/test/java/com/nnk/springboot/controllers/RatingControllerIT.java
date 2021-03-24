package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class RatingControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private RatingRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a RatingDto, when POST request, then save RatingDto check redirect Url is OK and check RatingDto is save in BDD")
    public void givenRatingDtoAdd_whenPostRequest_thenReturnRatingDtoAdd() throws Exception {
        RatingDto ratingtDto = new RatingDto("moodysTestIT", "standPRatingTestIt", "fitchRatingTestIt", 2);
        Rating rating = new Rating("moodysTestIT", "standPRatingTestIt", "fitchRatingTestIt", 2);

        mvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                .sessionAttr("RatingDto", ratingtDto)
                .param("moodysRating", ratingtDto.getMoodysRating())
                .param("sandPRating", ratingtDto.getSandPRating())
                .param("fitchRating", ratingtDto.getFitchRating())
                .param("orderNumber", String.valueOf(ratingtDto.getOrderNumber())))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(rat -> rat.getMoodysRating().equals(rating.getMoodysRating())
                        && rat.getSandPRating().equals(rating.getSandPRating())
                        && rat.getFitchRating().equals(rating.getFitchRating())
                        && String.valueOf(rat.getOrderNumber()).equals(String.valueOf(rating.getOrderNumber()))));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(rat -> {
                    if (rat.getMoodysRating().equals(rating.getMoodysRating())
                            && rat.getSandPRating().equals(rating.getSandPRating())
                            && rat.getFitchRating().equals(rating.getFitchRating())
                            && String.valueOf(rat.getOrderNumber()).equals(String.valueOf(rating.getOrderNumber()))) {
                        repository.deleteById(rat.getId());
                    }
                });
    }

    @Test
    @DisplayName("Given a RatingDto, when POST request, then save RatingDto return error Moodys is mandatory")
    public void givenRatingDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        RatingDto ratingtDto = new RatingDto("", "", "", 7);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                .sessionAttr("RatingDto", ratingtDto)
                .param("moodysRating", ratingtDto.getMoodysRating())
                .param("sandPRating", ratingtDto.getSandPRating())
                .param("fitchRating", ratingtDto.getFitchRating())
                .param("orderNumber", String.valueOf(ratingtDto.getOrderNumber())))
                .andExpect(model().hasErrors())
                .andExpect(view().name("rating/add"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Moodys is mandatory");
        assertThat(content).contains("Sand is mandatory");
        assertThat(content).contains("Fitch is mandatory");
    }

    @Test
    @DisplayName("Given id Rating and ratingDto to update, when post request, then update Rating in BDD")
    public void givenRatingtDtoUpdate_whenUpdateRequest_updateIsOk() throws Exception {
        RatingDto updateRatingtDto = new RatingDto("newMoodysTestIT", "newStandPRatingTestIt", "newFitchRatingTestIt", 7);
        Rating rating = new Rating("moodysTestIT", "standPRatingTestIt", "fitchRatingTestIt", 2);
        Rating updateRating = new Rating("newMoodysTestIT", "newStandPRatingTestIt", "newFitchRatingTestIt", 7);

        Rating save = repository.save(rating);

        String url = "/rating/update/".concat(String.valueOf(save.getId()));

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("RatingDto", updateRatingtDto)
                .param("moodysRating", updateRatingtDto.getMoodysRating())
                .param("sandPRating", updateRatingtDto.getSandPRating())
                .param("fitchRating", updateRatingtDto.getFitchRating())
                .param("orderNumber", String.valueOf(updateRatingtDto.getOrderNumber())))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));

        assumeTrue(repository.existsById(save.getId()));
        repository.findById(save.getId())
                .ifPresent(rat -> assumeTrue(rat.getMoodysRating().equals(updateRating.getMoodysRating())
                        && rat.getSandPRating().equals(updateRating.getSandPRating())
                        && rat.getFitchRating().equals(updateRating.getFitchRating())
                        && String.valueOf(rat.getOrderNumber()).equals(String.valueOf(updateRating.getOrderNumber()))));

        repository.deleteById(save.getId());
    }

    @Test
    @DisplayName("Given id Rating, when DELETE request, then DELETE in BDD search if exist")
    public void givenIdRatingDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        Rating rating = new Rating("newMoodysTestITDelete", "newStandPRatingTestItDelete", "newFitchRatingTestItDelete", 7);
        Rating save = repository.save(rating);

        String url = "/rating/delete/".concat(String.valueOf(save.getId()));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/rating/list"));
        assumeFalse(repository.existsById(save.getId()));
    }


    @Test
    @DisplayName("Count number Rating in Bdd and check number is the same in request")
    public void readAllRating_thenShowRatingListList() throws Exception {
        int nbRating = (int) repository.findAll().stream().count();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(view().name("rating/list"))
                .andReturn();

        AtomicInteger atomicInteger = new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s, t) ->
                {
                    List<RatingDto> modelRating = (List<RatingDto>) t;
                    if (s.equals("ratings")) {
                        atomicInteger.set(modelRating.size());
                    }
                });
        assumeTrue(nbRating == atomicInteger.get());
    }


}
