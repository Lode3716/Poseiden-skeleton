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

    @Override
    public RatingDto save(RatingDto ratingDto) {
        Rating rt = ratingRepository.save(ratingUnJMapper.getDestination(ratingDto));
        log.debug("Service : Rating is save in Bdd : {} ", rt);
        return ratingJMapper.getDestination(rt);
    }

    @Override
    public List<RatingDto> readAll() {
        List<RatingDto> listRatin = new ArrayList<>();
        ratingRepository.findAll()
                .forEach(rating -> listRatin.add(ratingJMapper.getDestination(rating)));
        log.debug("Service : create list ratingDto : {} ", listRatin.size());
        return listRatin;
    }

    @Override
    public RatingDto update(RatingDto rtDto) {
        Rating updateRating = existById(rtDto.getId());
        updateRating.setFitchRating(rtDto.getFitchRating());
        updateRating.setMoodysRating(rtDto.getMoodysRating());
        updateRating.setSandPRating(rtDto.getSandPRating());
        updateRating.setOrderNumber(rtDto.getOrderNumber());
        return ratingJMapper.getDestination(ratingRepository.save(updateRating));
    }

    @Override
    public void delete(RatingDto ratingDto) {
        ratingRepository.deleteById(ratingDto.getId());
    }

    public Rating existById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException("There is no rating with this id " + id));
    }

}
