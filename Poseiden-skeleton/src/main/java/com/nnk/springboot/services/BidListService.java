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

    /**
     * Convert a BidListDto to BidList and save it in the database.
     * When it's recorded, we return here.
     *
     * @param bidListDto to save
     * @return the BidList saved and converted the BidListDto
     */
    @Override
    public BidListDto save(BidListDto bidListDto) {
        BidList bid = bidListUnJMapper.getDestination(bidListDto);
        BidList bidList = bidListRepository.save(bid);
        log.info("Service : BidList is save in Bdd : {} ", bidList.getBidListId());
        return bidListJMapper.getDestination(bidList);
    }

    /**
     * Find list BidList and Convert BidListDto
     *
     * @return the list of BidListDto
     */
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


    /**
     * Check id exist, if valid update BidList
     *
     * @param bidListDto to update
     * @return the BidList update and converted the BidListDto
     */
    @Override
    public BidListDto update(Integer id,BidListDto bidListDto) {
        BidList updateBidList = existById(id);
        updateBidList.setAccount(bidListDto.getAccount());
        updateBidList.setType(bidListDto.getType());
        updateBidList.setBidQuantity(bidListDto.getBidQuantity());
        log.debug("Service : update list updateBidList : {} ", updateBidList.getBidListId());
        return bidListJMapper.getDestination(bidListRepository.save(updateBidList));
    }

    /**
     * Check id exist, if valid delete BidList
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        bidListRepository.deleteById(existById(id).getBidListId());
        log.info("Service delete bidList id : {}",id);
    }

    /**
     * Find BidList By id
     * @param id
     * @return the BidListDto find or issue IllegalArgumentException
     */
    @Override
    public BidListDto readByid(Integer id) {
        BidList findBidListId= bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid BidLsit Id:" + id));
        log.info("Service : Read by Id bidList - SUCCESS");
        return bidListJMapper.getDestination(findBidListId);
    }

    /**
     * Find BidList By id
     * @param id
     * @return the BidList find or issue BidListNotFoundException
     */
    public BidList existById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new BidListNotFoundException("There is no bidList with this id " + id));
    }
}
