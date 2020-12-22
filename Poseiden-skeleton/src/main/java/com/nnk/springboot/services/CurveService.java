package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.exceptions.CurvePointNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class CurveService implements ICurveService {

    @Autowired
    CurvePointRepository curvePointRepository;

    @Autowired
    JMapper<CurvePointDto, CurvePoint> curvePointJMapper;

    @Autowired
    JMapper<CurvePoint, CurvePointDto> curvePointUnJMapper;


    @Override
    public CurvePointDto save(CurvePointDto curvePointDto) {
        CurvePoint curvePoint = curvePointRepository.save(curvePointUnJMapper.getDestination(curvePointDto));
        log.debug("Service : CurvePoint is save in Bdd : {} ", curvePoint);
        return curvePointJMapper.getDestination(curvePoint);
    }

    @Override
    public List<CurvePointDto> readAll() {
        List<CurvePointDto> dtoList = new ArrayList<>();
        curvePointRepository.findAll()
                .forEach(curvePoint ->
                {
                    dtoList.add(curvePointJMapper.getDestination(curvePoint));
                });
        log.debug("Service : create list curvePointDto : {} ", dtoList.size());
        return dtoList;
    }

    @Override
    public CurvePointDto update(CurvePointDto curvePointDto) {
        CurvePoint updateCurvePoint = existById(curvePointUnJMapper.getDestination(curvePointDto).getId());
        updateCurvePoint.setCurveId(curvePointDto.getCurveId());
        updateCurvePoint.setTerm(curvePointDto.getTerm());
        updateCurvePoint.setValue(curvePointDto.getValue());
        return curvePointJMapper.getDestination(curvePointRepository.save(updateCurvePoint));
    }

    @Override
    public void delete(CurvePointDto curvePointDto) {
        curvePointRepository.deleteById(curvePointDto.getId());
        log.debug("Service : delete curvePointDto : {} ", curvePointDto.getId());
    }


    public CurvePoint existById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new CurvePointNotFoundException("There is no curvePoint with this id " + id));
    }
}
