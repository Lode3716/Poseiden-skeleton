package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.exceptions.CurvePointNotFoundException;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


@Log
@RunWith(MockitoJUnitRunner.class)
public class CurveServiceTest {

    @InjectMocks
    CurveService curveService;

    @Mock
    CurvePointRepository curvePointRepository;

    @Mock
    JMapper<CurvePointDto, CurvePoint> curvePointJMapper;

    @Mock
    JMapper<CurvePoint, CurvePointDto> curvePointUnJMapper;

    private static CurvePoint curvePoint1;

    private static CurvePoint curvePoint2;

    private static CurvePointDto curvePointDto1;

    private static CurvePointDto curvePointDto2;

    private static List<CurvePointDto> listCurvePointDto;

    @Before
    public void setUp() throws Exception {
        curvePointDto1 = new CurvePointDto(1, 1, 10d, 101d);
        curvePointDto2 = new CurvePointDto(2, 2, 20d, 217d);
        curvePoint1 = new CurvePoint(1, 1, null, 10d, 101d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        curvePoint2 = new CurvePoint(2, 2, null, 20d, 217d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        listCurvePointDto = Arrays.asList(curvePointDto1, curvePointDto2);
    }

    @Test
    public void givenSearchListOfCurvePointDto_whenAllcurvePoint_thenReturnListOfCurvePointDto() {


        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(curvePoint1, curvePoint2));
        when(curvePointJMapper.getDestination(curvePoint1)).thenReturn(curvePointDto1);
        when(curvePointJMapper.getDestination(curvePoint2)).thenReturn(curvePointDto2);

        List<CurvePointDto> result = curveService.readAll();

        assertThat(result).isEqualTo(listCurvePointDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(curvePointRepository, curvePointJMapper);
        inOrder.verify(curvePointRepository).findAll();
        inOrder.verify(curvePointJMapper).getDestination(curvePoint1);
        inOrder.verify(curvePointJMapper).getDestination(curvePoint2);
    }

    @Test
    public void givenCurvePointDto_whenSaveCurvePoint_thenCurvePointIsSavedCorrectly() {
        CurvePointDto curvePointDto = new CurvePointDto(1, 1, 10d, 101d);
        CurvePoint curvePoint = new CurvePoint(1, 1, null, 10d, 101d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));

        when(curvePointUnJMapper.getDestination(any(CurvePointDto.class))).thenReturn(curvePoint);
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint1);
        when(curvePointJMapper.getDestination(any(CurvePoint.class))).thenReturn(curvePointDto1);

        CurvePointDto asSave = curveService.save(curvePointDto);

        assertThat(asSave).isEqualToComparingFieldByField(curvePointDto1);
        InOrder inOrder = inOrder(curvePointUnJMapper, curvePointRepository, curvePointJMapper);
        inOrder.verify(curvePointUnJMapper).getDestination(any(CurvePointDto.class));
        inOrder.verify(curvePointRepository).save(any(CurvePoint.class));
        inOrder.verify(curvePointJMapper).getDestination(any(CurvePoint.class));
    }

    @Test
    public void givenIdPointAndCurvePointDto_whenUpdateCurvePoint_thenCurvePointIsUpdateCorrectly() {
        CurvePoint curvePoint = new CurvePoint(1, 1, null, 100d, 1200d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        CurvePointDto updateDto = new CurvePointDto(1, 1, 100d, 1200d);

        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePoint1));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        when(curvePointJMapper.getDestination(any(CurvePoint.class))).thenReturn(updateDto);

        CurvePointDto result = curveService.update(1, new CurvePointDto(1, 1, 100d, 1200d));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(curvePointRepository, curvePointJMapper);
        inOrder.verify(curvePointRepository).findById(anyInt());
        inOrder.verify(curvePointRepository).save(any(CurvePoint.class));
        inOrder.verify(curvePointJMapper).getDestination(any(CurvePoint.class));
    }

    @Test
    public void givenIdPointDto_whenDeleteCurvePoint_thenBisListIsDeleteCorrectly() {
        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePoint1));

        curveService.delete(anyInt());

        InOrder inOrder = inOrder(curvePointRepository);
        inOrder.verify(curvePointRepository).findById(anyInt());
        inOrder.verify(curvePointRepository).deleteById(anyInt());
    }

    @Test(expected = CurvePointNotFoundException.class)
    public void givenUnFoundCurvePoint_whenDeleteCurvePoint_thenCurvePointNotFoundException() {
        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        curveService.delete(anyInt());
    }

    @Test
    public void givenIdPointDto_whenFoundCurvePoint_thenReturnCurvePointFound() {
        CurvePoint curvePointFind =  new CurvePoint(1, 1, null, 10d, 101d, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));;

        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.of(curvePointFind));

        CurvePoint result = curveService.existById(1);

        assertThat(result).isEqualTo(curvePoint1);
    }

    @Test(expected = CurvePointNotFoundException.class)
    public void givenUnFoundIdPointDto_whenFoundCurvePoint_thenCurvePointNotFoundException() {
        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        curveService.existById(anyInt());

    }
}