package com.egoist.elasticsearch.service;

import com.egoist.parent.pojo.dto.EgoistResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOrderSubIndexService {

    @Autowired
    private OrderSubIndexService orderSubIndexService;

    @Test
    public void testCreateOrderSubDocByIdx() {
//        long subOrderIdx = 10000704L;
        long subOrderIdx = 10000048L;
        EgoistResult result = orderSubIndexService.createOrderSubDocByIdx(subOrderIdx);
        System.out.println("###############" + result);
    }

    @Test
    public void testCreateOrderItemDoc() {
//        long subOrderIdx = 10000704L;
//        String subOrderNo = "YTMS2018051400876621867A026";
        long subOrderIdx = 10000048L;
        String subOrderNo = "TMS2017121300876557327A1";
        EgoistResult result = orderSubIndexService.createOrderItemDoc(subOrderIdx, subOrderNo);
        System.out.println("###############" + result);
    }
}
