package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        int productionHouseId=webSeriesEntryDto.getProductionHouseId();
        ProductionHouse productionHouse=productionHouseRepository.findById(productionHouseId).get();
        List<WebSeries> webSeriesList=new ArrayList<>();
        //converting DTO to Entity
        WebSeries webSeries1=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(!webSeries1.equals(null)){
            throw new Exception("Series is already present");
        }

        WebSeries webSeries=new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setProductionHouse(productionHouse);
        webSeriesList.add(webSeries);
        productionHouse.setWebSeriesList(webSeriesList);
        //Saving
        productionHouseRepository.save(productionHouse);
        webSeriesRepository.save(webSeries);
        return webSeries.getId();
    }
}
