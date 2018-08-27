package com.egoist.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.egoist.elasticsearch.document.GoodsInfo;
import com.egoist.elasticsearch.repository.GoodsRepository;
import com.egoist.parent.common.utils.http.EgoistOkHttp3Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESServiceApplicationTests {

    @Autowired
    private GoodsRepository goodsRepository;

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
    public void testElaticSearch() {
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
        GoodsInfo goodsInfo = goodsRepository.findById(1533212361216L).get();
        System.out.println("##########" + JSONObject.toJSONString(goodsInfo));
    }
}
