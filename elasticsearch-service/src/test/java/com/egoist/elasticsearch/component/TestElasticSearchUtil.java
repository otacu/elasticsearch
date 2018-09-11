package com.egoist.elasticsearch.component;

import com.alibaba.fastjson.JSONObject;
import com.egoist.elasticsearch.common.constants.ESIndexNameConstant;
import com.egoist.elasticsearch.common.constants.ESTemplateConstant;
import com.egoist.elasticsearch.common.constants.ESTypeNameConstant;
import com.egoist.parent.pojo.dto.EgoistResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 测试es工具类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestElasticSearchUtil {
    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    @Test
    public void testCreateIndex() {
        String index = ESIndexNameConstant.INDEX_ORDER_SUB;
        String type = ESTypeNameConstant.TYPE_ORDER_SUB;
        String template = ESTemplateConstant.TEMPLATE_ORDER_SUB;
        EgoistResult result = elasticSearchUtil.createIndex(index, type, template);
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void testCreateOrderSubDoc() {
        String index = ESIndexNameConstant.INDEX_ORDER_SUB;
        String type = ESTypeNameConstant.TYPE_ORDER_SUB;
        Map<String, Object> properties = new HashMap<>();
        elasticSearchUtil.createDoc(index, type, "orderSub123", properties, null);
    }

    @Test
    public void testDeleteOrderSubDoc() {
        String index = ESIndexNameConstant.INDEX_ORDER_SUB;
        String type = ESTypeNameConstant.TYPE_ORDER_SUB;
        elasticSearchUtil.deleteDoc(index, type, "orderItem10000957");
    }
}
