package com.nnk.springboot.repository;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;


    public void curvePointTest() {
        CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertNotNull(curvePoint.getId());
        Assertions.assertTrue(curvePoint.getCurveId() == 10);

        // Update
        curvePoint.setCurveId(20);
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertTrue(curvePoint.getCurveId() == 20);

        // Find
        List<CurvePoint> listResult = curvePointRepository.findAll();
        Assertions.assertTrue(listResult.size() > 0);

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        Assertions.assertFalse(curvePointList.isPresent());
    }

}