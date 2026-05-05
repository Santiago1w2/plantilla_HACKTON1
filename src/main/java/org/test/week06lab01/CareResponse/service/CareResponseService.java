package org.test.week06lab01.CareResponse.service;

import org.springframework.stereotype.Service;
import org.test.week06lab01.CareResponse.CareResponse;
import org.test.week06lab01.CareResponse.repository.careResponseRepository;

@Service
public class CareResponseService {
    private final careResponseRepository careResponseRepository;

    public CareResponseService(careResponseRepository careResponseRepository) {
        this.careResponseRepository = careResponseRepository;
    }

    public CareResponse findById(Long id){
        return careResponseRepository.findById(id).orElseThrow(()->new RuntimeException("no encontradp"));
    }
}
