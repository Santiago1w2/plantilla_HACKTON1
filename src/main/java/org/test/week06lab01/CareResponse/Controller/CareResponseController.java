package org.test.week06lab01.CareResponse.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.week06lab01.CareResponse.CareResponse;
import org.test.week06lab01.CareResponse.service.CareResponseService;

@RestController
@RequestMapping("/api/v1/signals")
public class CareResponseController {
    private final CareResponseService careResponseService;

    public CareResponseController(CareResponseService careResponseService) {
        this.careResponseService = careResponseService;
    }

    @GetMapping("/{id}/care-response")
    ResponseEntity<CareResponse>getCareResponses(@PathVariable Long id){
        CareResponse careCreated = careResponseService.findById(id);
        return ResponseEntity.ok(careCreated);
    }

}
