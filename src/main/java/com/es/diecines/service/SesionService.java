package com.es.diecines.service;

import com.es.diecines.dto.SesionDTO;
import com.es.diecines.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SesionService {

    @Autowired
    private SesionService service;
    @Autowired
    private Mapper mapper;


    public SesionDTO insert(String id) {
        return null;
    }


}
