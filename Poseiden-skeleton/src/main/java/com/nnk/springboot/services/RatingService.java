package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.exceptions.RatingNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class RatingService implements IRatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    JMapper<RatingDto, Rating> ratingJMapper;

    @Autowired
    JMapper<Rating, RatingDto> ratingUnJMapper;


    /**
     * Convert a RatingDto to Rating and save it in the database.
     * When it's recorded, we return here.
     *
     * @param RatingDto to save
     * @return the Rating saved and converted the RatingDto
     */
    @Override
    public RatingDto save(RatingDto ratingDto) {
        Rating rt = ratingRepository.save(ratingUnJMapper.getDestination(ratingDto));
        log.info("Service : Rating is save in Bdd : {} ", rt);
        return ratingJMapper.getDestination(rt);
    }

    /**
     * Find list Rating and Convert RatingDto
     *
     * @return the list of RatingDto
     */
    @Override
    public List<RatingDto> readAll() {
        List<RatingDto> listRatin = new ArrayList<>();
        ratingRepository.findAll()
                .forEach(rating -> listRatin.add(ratingJMapper.getDestination(rating)));
        log.debug("Service : read list ratingDto : {} ", listRatin.size());
        return listRatin;
    }

    /**
     * Check id exist, if valid update Rating
     *
     * @param RatingDto to update
     * @return the Rating update and converted the RatingDto
     */
    @Override
    public RatingDto update(Integer id, RatingDto rtDto) {
        Rating updateRating = existById(id);
        updateRating.setFitchRating(rtDto.getFitchRating());
        updateRating.setMoodysRating(rtDto.getMoodysRating());
        updateRating.setSandPRating(rtDto.getSandPRating());
        updateRating.setOrderNumber(rtDto.getOrderNumber());
        log.info("Service : update rating: {} ", updateRating.getId());
        return ratingJMapper.getDestination(ratingRepository.save(updateRating));
    }

    /**
     * Check id exist, if valid delete rating
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        ratingRepository.deleteById(existById(id).getId());
        log.info("Service delete rating id : {}", id);
    }


    /**
     * Find Rating By id
     *
     * @param id
     * @return the RatingDto find or issue IllegalArgumentException
     */
    @Override
    public RatingDto readByid(Integer id) {
        Rating findRatingListId = ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        log.info("Service : Read by Id rating - SUCCESS");
        return ratingJMapper.getDestination(findRatingListId);
    }

    /**
     * Find Rating By id
     *
     * @param id
     * @return the Rating find or issue RatingNotFoundException
     */
    public Rating existById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException("There is no rating with this id " + id));
    }

}
