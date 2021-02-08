package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.exceptions.TradeNotFoundException;
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
public class TradeServiceTest {

    @InjectMocks
    TradeService tradeService;

    @Mock
    TradeRepository tradeRepository;

    @Mock
    JMapper<TradeDto, Trade> tradeJMapper;

    @Mock
    JMapper<Trade, TradeDto> tradeUnJMapper;

    private static TradeDto tradeDto1;

    private static TradeDto tradeDto2;

    private static Trade trade1;

    private static Trade trade2;

    private static List<TradeDto> listTradeDto;

    @Before
    public void setUp() throws Exception {

        tradeDto1 = new TradeDto(1, "account1", "type1", 10d);
        tradeDto2 = new TradeDto(2, "account2", "type2", 20d);
        trade1 = new Trade(1, "account1", "type1", 10d, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
        trade2 = new Trade(2, "account2", "type2", 20d, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
        listTradeDto = Arrays.asList(tradeDto1, tradeDto2);
    }


    @Test
    public void givenSearchListOfTradeDto_whenAllTrade_thenReturnListOfTradeDto() {


        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1, trade2));
        when(tradeJMapper.getDestination(trade1)).thenReturn(tradeDto1);
        when(tradeJMapper.getDestination(trade2)).thenReturn(tradeDto2);

        List<TradeDto> result = tradeService.readAll();

        assertThat(result).isEqualTo(listTradeDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(tradeRepository, tradeJMapper);
        inOrder.verify(tradeRepository).findAll();
        inOrder.verify(tradeJMapper).getDestination(trade1);
        inOrder.verify(tradeJMapper).getDestination(trade2);
    }

    @Test
    public void givenTradeDto_whenSaveTrade_thenTradeIsSavedCorrectly() {
        TradeDto tradeDto = new TradeDto("account1", "type1", 10d);
        Trade trade = new Trade("account", "type", 10d);

        when(tradeUnJMapper.getDestination(any(TradeDto.class))).thenReturn(trade);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade1);
        when(tradeJMapper.getDestination(any(Trade.class))).thenReturn(tradeDto1);

        TradeDto asSave = tradeService.save(tradeDto);

        assertThat(asSave).isEqualToComparingFieldByField(tradeDto1);
        InOrder inOrder = inOrder(tradeUnJMapper, tradeRepository, tradeJMapper);
        inOrder.verify(tradeUnJMapper).getDestination(any(TradeDto.class));
        inOrder.verify(tradeRepository).save(any(Trade.class));
        inOrder.verify(tradeJMapper).getDestination(any(Trade.class));
    }

    @Test
    public void givenIdTradeAndTradeDto_whenUpdateTrade_thenTradeIsUpdateCorrectly() {
        Trade updateTrade = new Trade(1, "account10", "type10", 350d, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
        TradeDto updateDto = new TradeDto(1, "account10", "type10", 350d);

        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(trade1));
        when(tradeRepository.save(any(Trade.class))).thenReturn(updateTrade);
        when(tradeJMapper.getDestination(any(Trade.class))).thenReturn(updateDto);

        TradeDto result = tradeService.update(1, new TradeDto(1, "account10", "type10", 350d));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(tradeRepository, tradeJMapper);
        inOrder.verify(tradeRepository).findById(anyInt());
        inOrder.verify(tradeRepository).save(any(Trade.class));
        inOrder.verify(tradeJMapper).getDestination(any(Trade.class));
    }

    @Test
    public void givenIdTradeDto_whenDeleteTrade_thenBisListIsDeleteCorrectly() {
        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(trade1));

        tradeService.delete(anyInt());

        InOrder inOrder = inOrder(tradeRepository);
        inOrder.verify(tradeRepository).findById(anyInt());
        inOrder.verify(tradeRepository).deleteById(anyInt());
    }

    @Test(expected = TradeNotFoundException.class)
    public void givenUnFoundTrade_whenDeleteTrade_thenTradeNotFoundException() {
        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        tradeService.delete(anyInt());
    }

    @Test
    public void givenIdTradeDto_whenFoundTrade_thenReturnTradeFound() {
        Trade trade = new Trade(1, "account10", "type10", 350d, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);

        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.of(trade));

        Trade result = tradeService.existById(1);

        assertThat(result).isEqualTo(trade1);
    }

    @Test(expected = TradeNotFoundException.class)
    public void givenUnFoundIdBidDto_whenFoundTrade_thenTradeNotFoundException() {
        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        tradeService.existById(anyInt());
    }
}