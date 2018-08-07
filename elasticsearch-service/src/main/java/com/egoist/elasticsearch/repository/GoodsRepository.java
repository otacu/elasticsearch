package com.egoist.elasticsearch.repository;

import com.egoist.elasticsearch.document.GoodsInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsInfo,Long> {
}
