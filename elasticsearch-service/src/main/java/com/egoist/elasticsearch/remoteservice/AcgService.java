package com.egoist.elasticsearch.remoteservice;

import com.egoist.parent.pojo.dto.EgoistResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: AcgService
 * @Description: 远程服务
 */
@FeignClient(name = "acg")
public interface AcgService {
    @GetMapping("/orderSub/packageIndexDocByIdx")
    EgoistResult packageOrderSubDocByIdx(@RequestParam(value = "idx") Long idx);

    @GetMapping("/orderItem/packageIndexDoc")
    EgoistResult packageOrderItemDoc(@RequestParam(value = "subOrderIdx") Long subOrderIdx,
                                     @RequestParam(value = "subOrderNo") String subOrderNo);
}
