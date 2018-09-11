package com.egoist.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.egoist.elasticsearch.remoteservice.AcgService;
import com.egoist.parent.common.utils.http.EgoistOkHttp3Util;
import com.egoist.parent.pojo.dto.EgoistResult;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESServiceApplicationTests {

//    @Autowired
//    private GoodsRepository goodsRepository;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    AcgService acgService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void testHttp() {
        String url = "https://blog.csdn.net/ghost_chou/phoenix/comment/list/78056852?page=1&tree_type=1";
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject json = EgoistOkHttp3Util.post(url, map);
            System.out.println("##########" + json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testElasticSearchRepository() {
        // 新增
//        GoodsInfo goodsInfo = new GoodsInfo(System.currentTimeMillis(),
//                "商品"+System.currentTimeMillis(),"这是一个测试商品");
//        goodsRepository.save(goodsInfo);
        // 删除
//        goodsRepository.delete(id);
        // 修改
//        GoodsInfo goodsInfo = new GoodsInfo(id,
//                name,description);
//        goodsRepository.save(goodsInfo);
        // 查询
//        GoodsInfo goodsInfo = goodsRepository.findById(1533212361216L).get();
//        System.out.println("##########" + JSONObject.toJSONString(goodsInfo));
    }

    @Test
    public void testRestHighLevelClient() throws IOException {
        IndexRequest request = new IndexRequest("buydeem", "order", "1");
        Map<String, Object> properties = new HashMap<>();
        properties.put("orderNo", "201807240001");
        properties.put("created", new Date());
        properties.put("amount", BigDecimal.TEN);
        request.source(properties);
        IndexResponse response = client.index(request);
        System.out.println(response.toString());
    }

    @Test
    public void testSpringCloud() {
        long idx = 10000020L;
        EgoistResult result = acgService.packageOrderSubDocByIdx(idx);
        System.out.println("###############" + result);
    }

}
