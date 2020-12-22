package com.nnk.springboot.dto;


import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.BidList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDTO {

    @Bean
    JMapper<BidListDto, BidList> bidListJMapper(){
        return new JMapper<>(BidListDto.class,BidList.class);
    }

    @Bean
    JMapper<BidList, BidListDto> bidListUnJMapper() {
        return new JMapper<>(BidList.class,BidListDto.class);
    }
}