package com.nnk.springboot.repository;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


public class TradeTests {

    @Autowired
    private TradeRepository tradeRepository;


    public void tradeTest() {
        Trade trade = new Trade("Trade Account", "Type");

        // Save
        trade = tradeRepository.save(trade);
        Assertions.assertNotNull(trade.getTradeId());
        Assertions.assertTrue(trade.getAccount().equals("Trade Account"));

        // Update
        trade.setAccount("Trade Account Update");
        trade = tradeRepository.save(trade);
        Assertions.assertTrue(trade.getAccount().equals("Trade Account Update"));

        // Find
        List<Trade> listResult = tradeRepository.findAll();
        Assertions.assertTrue(listResult.size() > 0);

        // Delete
        Integer id = trade.getTradeId();
        tradeRepository.delete(trade);
        Optional<Trade> tradeList = tradeRepository.findById(id);
        Assertions.assertFalse(tradeList.isPresent());
    }
}
