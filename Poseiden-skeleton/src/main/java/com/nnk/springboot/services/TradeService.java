package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.exceptions.TradeNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class TradeService implements ITradeService {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    JMapper<TradeDto, Trade> tradeJMapper;

    @Autowired
    JMapper<Trade, TradeDto> tradeUnJMapper;

    /**
     * Convert a TradeDto to Trade and save it in the database.
     * When it's recorded, we return here.
     *
     * @param tradeDto to save
     * @return the Trade saved and converted the TradeDto
     */
    @Override
    public TradeDto save(TradeDto tradeDto) {
        Trade trade = tradeUnJMapper.getDestination(tradeDto);
        Trade saveTrade = tradeRepository.save(trade);
        log.info("Service : Trade is save in Bdd : {} ", saveTrade.getTradeId());
        return tradeJMapper.getDestination(saveTrade);
    }

    /**
     * Find list Trade and Convert TradeDto
     *
     * @return the list of TradeDto
     */
    @Override
    public List<TradeDto> readAll() {
        List<TradeDto> listTradeDto = new ArrayList<>();
        tradeRepository.findAll()
                .forEach(tradeList ->
                {
                    listTradeDto.add(tradeJMapper.getDestination(tradeList));
                });
        log.debug("Service : read list tradeDto : {} ", listTradeDto.size());
        return listTradeDto;
    }

    /**
     * Check id exist, if valid update Trade
     *
     * @param id
     * @param tradeDto to update
     * @return the Trade update and converted the TradeDto
     */
    @Override
    public TradeDto update(Integer id, TradeDto tradeDto) {
        Trade updateTrade = existById(id);
        updateTrade.setAccount(tradeDto.getAccount());
        updateTrade.setType(tradeDto.getType());
        updateTrade.setBuyQuantity(tradeDto.getBuyQuantity());
        log.info("Service : update trade : {} ", updateTrade.getTradeId());
        return tradeJMapper.getDestination(tradeRepository.save(updateTrade));
    }


    /**
     * Check id exist, if valid delete trade
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        tradeRepository.deleteById(existById(id).getTradeId());
        log.info("Service delete trade id : {}", id);
    }

    /**
     * Find Trade By id
     *
     * @param id
     * @return the TradeDto find or issue IllegalArgumentException
     */
    @Override
    public TradeDto readByid(Integer id) {
        Trade findTradeId = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        log.info("Service : Read by Id trade - SUCCESS");
        return tradeJMapper.getDestination(findTradeId);
    }

    /**
     * Find Trade By id
     *
     * @param id
     * @return the Trade find or issue TradeNotFoundException
     */
    public Trade existById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("There is no Trade with this id " + id));
    }
}
