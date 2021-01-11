package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.exceptions.BidListNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class BidListService implements IBidListService {

    @Autowired
    BidListRepository bidListRepository;

    @Autowired
    JMapper<BidListDto, BidList> bidListJMapper;

    @Autowired
    JMapper<BidList, BidListDto> bidListUnJMapper;

    @Override
    public BidListDto save(BidListDto bidListDto) {
        BidList bid = bidListUnJMapper.getDestination(bidListDto);
        log.info("Affiche objet : "+bid.toString());
        BidList bidList = bidListRepository.save(bid);
        log.info("Service : BidList is save in Bdd : {} ", bidList);
        return bidListJMapper.getDestination(bidList);
    }

    @Override
    public List<BidListDto> readAll() {
        List<BidListDto> listBidListDto = new ArrayList<>();
        bidListRepository.findAll()
                .forEach(bidList ->
                {
                    listBidListDto.add(bidListJMapper.getDestination(bidList));
                });
        log.debug("Service : create list BidLIST : {} ", listBidListDto.size());
        return listBidListDto;
    }

    @Override
    public BidListDto update(BidListDto bidListDto) {
        BidList updateBidList = existById(bidListDto.getBidListId());
        updateBidList.setAccount(bidListDto.getAccount());
        updateBidList.setType(bidListDto.getType());
        updateBidList.setBidQuantity(bidListDto.getBidQuantity());
        return bidListJMapper.getDestination(bidListRepository.save(updateBidList));
    }

    @Override
    public void delete(BidListDto bidListDto) {
        bidListRepository.deleteById(bidListDto.getBidListId());
    }


    public BidList existById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new BidListNotFoundException("There is no bidList with this id " + id));
    }
}
