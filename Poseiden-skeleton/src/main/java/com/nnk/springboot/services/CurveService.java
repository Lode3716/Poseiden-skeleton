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


    /**
     * Convert a CurvePointDto to CurvePoint and save it in the database.
     * When it's recorded, we return here.
     *
     * @param bidListDto to save
     * @return the CurvePoint saved and converted the CurvePointDto
     */
    @Override
    public CurvePointDto save(CurvePointDto curvePointDto) {
        CurvePoint curvePoint = curvePointRepository.save(curvePointUnJMapper.getDestination(curvePointDto));
        log.debug("Service : CurvePoint is save in Bdd : {} ", curvePoint.getCurveId());
        return curvePointJMapper.getDestination(curvePoint);
    }


    /**
     * Find list CurvePoint and Convert CurvePointDto
     *
     * @return the list of CurvePointDto
     */
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


    /**
     * Check id exist, if valid update CurvePoint
     *
     * @param curvePointDto to update
     * @return the CurvePoint update and converted the CurvePointDto
     */
    @Override
    public CurvePointDto update(Integer id,CurvePointDto curvePointDto) {
        CurvePoint updateCurvePoint = existById(id);
        updateCurvePoint.setCurveId(curvePointDto.getCurveId());
        updateCurvePoint.setTerm(curvePointDto.getTerm());
        updateCurvePoint.setValue(curvePointDto.getValue());
        log.debug("Service : update list curvePointDto : {} ", updateCurvePoint.getId());
        return curvePointJMapper.getDestination(curvePointRepository.save(updateCurvePoint));
    }

    /**
     * Check id exist, if valid delete CurvePoint
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        curvePointRepository.deleteById(existById(id).getId());
        log.debug("Service : delete curvePointDto : {} ", id);
    }

    /**
     * Find CurvePoint By id
     * @param id
     * @return the curvePointDto find or issue IllegalArgumentException
     */
    @Override
    public CurvePointDto readByid(Integer id) {
        CurvePoint curvePoint=curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        log.info("Service : Read by Id  CurvePointDto - SUCCESS");
        return curvePointJMapper.getDestination(curvePoint);
    }


    /**
     * Find CurvePoint By id
     * @param id
     * @return the curvePoint find or issue CurvePointNotFoundException
     */
    public CurvePoint existById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new CurvePointNotFoundException("There is no curvePoint with this id " + id));
    }
}
