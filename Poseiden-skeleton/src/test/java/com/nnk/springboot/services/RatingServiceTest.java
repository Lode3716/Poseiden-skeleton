package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.exceptions.RatingNotFoundException;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@Log
@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {


    @InjectMocks
    private RatingService ratingService;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    JMapper<RatingDto, Rating> ratingJMapper;

    @Mock
    JMapper<Rating, RatingDto> ratingUnJMapper;

    private static Rating rating1;

    private static Rating rating2;

    private static RatingDto ratingDto1;

    private static RatingDto ratingDto2;

    private static List<RatingDto> listRatingDto;

    @Before
    public void setUp() throws Exception {
        ratingDto1 = new RatingDto(1, "moodys", "standPRating", "fitchRating", 2);
        ratingDto2 = new RatingDto(2, "moodys", "standPRating", "fitchRating", 3);
        rating1 = new Rating(1, "moodys", "standPRating", "fitchRating", 2);
        rating2 = new Rating(2, "moodys", "standPRating", "fitchRating", 3);
        listRatingDto = Arrays.asList(ratingDto1, ratingDto2);
    }

    @Test
    public void givenSearchListOfRatingDto1_whenAllRating_thenReturnListOfRatingDto1() {

        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating1, rating2));
        when(ratingJMapper.getDestination(rating1)).thenReturn(ratingDto1);
        when(ratingJMapper.getDestination(rating2)).thenReturn(ratingDto2);

        List<RatingDto> result = ratingService.readAll();

        assertThat(result).isEqualTo(listRatingDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(ratingRepository, ratingJMapper);
        inOrder.verify(ratingRepository).findAll();
        inOrder.verify(ratingJMapper).getDestination(rating1);
        inOrder.verify(ratingJMapper).getDestination(rating2);
    }

    @Test
    public void givenRatingDto_whenSaveRating_thenRatingIsSavedCorrectly() {
        RatingDto ratingDto = new RatingDto( "moodys", "standPRating", "fitchRating", 2);
        Rating rating = new Rating("moodys", "standPRating", "fitchRating", 2);

        when(ratingUnJMapper.getDestination(any(RatingDto.class))).thenReturn(rating);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating1);
        when(ratingJMapper.getDestination(any(Rating.class))).thenReturn(ratingDto1);

        RatingDto asSave = ratingService.save(ratingDto);

        assertThat(asSave).isEqualToComparingFieldByField(ratingDto1);
        InOrder inOrder = inOrder(ratingUnJMapper, ratingRepository, ratingJMapper);
        inOrder.verify(ratingUnJMapper).getDestination(any(RatingDto.class));
        inOrder.verify(ratingRepository).save(any(Rating.class));
        inOrder.verify(ratingJMapper).getDestination(any(Rating.class));
    }

    @Test
    public void givenIRatingAndRatingDto_whenUpdateRating_thenRatingIsUpdateCorrectly() {
        Rating updateRating = new Rating(1, "moodys", "standPRatingUpdate", "fitchRatingUpdate", 25);
        RatingDto updateDto = new RatingDto(1, "moodys", "standPRatingUpdate", "fitchRatingUpdate", 25);

        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(rating1));
        when(ratingRepository.save(any(Rating.class))).thenReturn(updateRating);
        when(ratingJMapper.getDestination(any(Rating.class))).thenReturn(updateDto);

        RatingDto result = ratingService.update(1, new RatingDto(1, "moodys", "standPRatingUpdate", "fitchRatingUpdate", 25));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(ratingRepository, ratingJMapper);
        inOrder.verify(ratingRepository).findById(anyInt());
        inOrder.verify(ratingRepository).save(any(Rating.class));
        inOrder.verify(ratingJMapper).getDestination(any(Rating.class));
    }

    @Test
    public void givenIdRatingDto_whenDeleteRating_thenBisListIsDeleteCorrectly() {
        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(rating1));

        ratingService.delete(anyInt());

        InOrder inOrder = inOrder(ratingRepository);
        inOrder.verify(ratingRepository).findById(anyInt());
        inOrder.verify(ratingRepository).deleteById(anyInt());
    }

    @Test(expected = RatingNotFoundException.class)
    public void givenUnFoundRating_whenDeleteRating_thenRatingNotFoundException() {
        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        ratingService.delete(anyInt());
    }

    @Test
    public void givenIdRatingDto_whenFoundRating_thenReturnRatingFound() {
        Rating rating = new Rating(1, "moodys", "standPRating", "fitchRating", 2);

        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.of(rating));

        Rating result = ratingService.existById(1);

        assertThat(result).isEqualTo(rating1);
    }

    @Test(expected = RatingNotFoundException.class)
    public void givenUnFoundIdRatingDto_whenFoundRating_thenRatingNotFoundException() {
        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        ratingService.existById(anyInt());

    }
}