package com.egoist.elasticsearch.service;

import com.egoist.elasticsearch.common.constants.ESIndexNameConstant;
import com.egoist.elasticsearch.common.constants.ESTypeNameConstant;
import com.egoist.elasticsearch.component.ElasticSearchUtil;
import com.egoist.elasticsearch.remoteservice.AcgService;
import com.egoist.parent.common.constants.EgoistResultStatusConstants;
import com.egoist.parent.pojo.dto.EgoistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderSubIndexService {
    @Autowired
    private AcgService acgService;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public EgoistResult createOrderSubDocByIdx(Long idx) {
        EgoistResult packageResult = acgService.packageOrderSubDocByIdx(idx);
        if (packageResult.getStatus() != EgoistResultStatusConstants.STATUS_200) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "创建拆单文档异常", null);
        }
        Map<String, Object> properties = (Map<String, Object>) packageResult.getData();
        String index = ESIndexNameConstant.INDEX_ORDER_SUB;
        String type = ESTypeNameConstant.TYPE_ORDER_SUB;
        return elasticSearchUtil.createDoc(index, type, "orderSub" + idx, properties, null);
    }

    public EgoistResult createOrderItemDoc(Long subOrderIdx, String subOrderNo) {
        EgoistResult packageResult = acgService.packageOrderItemDoc(subOrderIdx, subOrderNo);
        if (packageResult.getStatus() != EgoistResultStatusConstants.STATUS_200) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "创建拆单商品文档异常", null);
        }
        String index = ESIndexNameConstant.INDEX_ORDER_SUB;
        String type = ESTypeNameConstant.TYPE_ORDER_SUB;
        List<Map<String, Object>> list = (List<Map<String, Object>>) packageResult.getData();
        for (Map<String, Object> properties : list) {
            long idx = Long.parseLong("" + properties.get("idx"));
            elasticSearchUtil.createDoc(index, type, "orderItem" + idx, properties, "orderSub" + subOrderIdx);
        }
        return EgoistResult.ok();
    }
}
