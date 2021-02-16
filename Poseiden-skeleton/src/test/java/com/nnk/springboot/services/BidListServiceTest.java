package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.exceptions.BidListNotFoundException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@Log
@ExtendWith(MockitoExtension.class)
public class BidListServiceTest {

    @InjectMocks
    private BidListService bidListService;

    @Mock
    private BidListRepository bidListRepository;

    @Mock
    JMapper<BidListDto, BidList> bidListJMapper;

    @Mock
    JMapper<BidList, BidListDto> bidListUnJMapper;

    private static BidList bidList1;

    private static BidList bidList2;

    private static BidListDto bidListDto1;

    private static BidListDto bidListDto2;

    private static List<BidListDto> listBidListDto;

    @BeforeEach
    public void setUp() throws Exception {
        bidListDto1 = new BidListDto(1, "account", "type", 10d);
        bidListDto2 = new BidListDto(2, "account2", "type2", 102d);
        bidList1 = new BidList(1, "account", "type", 10d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);
        bidList2 = new BidList(2, "account2", "type2", 102d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);
        listBidListDto = Arrays.asList(bidListDto1, bidListDto2);
    }

    @Test
    public void givenSearchListOfBidListDto_whenAllBidList_thenReturnListOfBidListDto() {


        when(bidListRepository.findAll()).thenReturn(Arrays.asList(bidList1, bidList2));
        when(bidListJMapper.getDestination(bidList1)).thenReturn(bidListDto1);
        when(bidListJMapper.getDestination(bidList2)).thenReturn(bidListDto2);

        List<BidListDto> result = bidListService.readAll();

        assertThat(result).isEqualTo(listBidListDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(bidListRepository, bidListJMapper);
        inOrder.verify(bidListRepository).findAll();
        inOrder.verify(bidListJMapper).getDestination(bidList1);
        inOrder.verify(bidListJMapper).getDestination(bidList2);
    }

    @Test
    public void givenBidListDto_whenSaveBidList_thenBidListIsSavedCorrectly() {
        BidListDto bidListDto = new BidListDto("account", "type", 10d);
        BidList bidList = new BidList("account", "type", 10d);

        when(bidListUnJMapper.getDestination(any(BidListDto.class))).thenReturn(bidList);
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList1);
        when(bidListJMapper.getDestination(any(BidList.class))).thenReturn(bidListDto1);

        BidListDto asSave = bidListService.save(bidListDto);

        assertThat(asSave).isEqualToComparingFieldByField(bidListDto1);
        InOrder inOrder = inOrder(bidListUnJMapper, bidListRepository, bidListJMapper);
        inOrder.verify(bidListUnJMapper).getDestination(any(BidListDto.class));
        inOrder.verify(bidListRepository).save(any(BidList.class));
        inOrder.verify(bidListJMapper).getDestination(any(BidList.class));
    }

    @Test
    public void givenIdBidAndbidListDto_whenUpdateBidList_thenBidListIsUpdateCorrectly() {
        BidList updateBidList = new BidList(1, "account1", "type1", 25d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);
        BidListDto updateDto = new BidListDto(1, "account1", "type1", 25d);

        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(bidList1));
        when(bidListRepository.save(any(BidList.class))).thenReturn(updateBidList);
        when(bidListJMapper.getDestination(any(BidList.class))).thenReturn(updateDto);

        BidListDto result = bidListService.update(1, new BidListDto(1, "account1", "type1", 25d));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(bidListRepository, bidListJMapper);
        inOrder.verify(bidListRepository).findById(anyInt());
        inOrder.verify(bidListRepository).save(any(BidList.class));
        inOrder.verify(bidListJMapper).getDestination(any(BidList.class));
    }

    @Test
    public void givenIdBidDto_whenDeleteBidList_thenBisListIsDeleteCorrectly() {
        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(bidList1));

        bidListService.delete(anyInt());

        InOrder inOrder = inOrder(bidListRepository);
        inOrder.verify(bidListRepository).findById(anyInt());
        inOrder.verify(bidListRepository).deleteById(anyInt());
    }

    @Test
    public void givenUnFoundBidList_whenDeleteBidList_thenBidListNotFoundException() {
        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(BidListNotFoundException.class,()->bidListService.delete(anyInt()));
    }

    @Test
    public void givenIdBidDto_whenFoundBidList_thenReturnBidlistFound() {
        BidList bidListFind = new BidList(1, "account1", "type1", 10d, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null);

        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.of(bidListFind));

        BidList result = bidListService.existById(1);

        assertThat(result).isEqualTo(bidList1);
    }

    @Test
    public void givenUnFoundIdBidDto_whenFoundBidList_thenBidListNotFoundException() {
        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(BidListNotFoundException.class,()->bidListService.existById(anyInt()));
    }
}