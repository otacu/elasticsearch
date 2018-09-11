package com.egoist.elasticsearch.repository;

import org.springframework.stereotype.Component;

// 由于springboot还没支持elasticsearch 6.x，所以先不用elasticsearchRepository
@Component
public interface GoodsRepository
       // extends ElasticsearchRepository<GoodsInfo,Long>
{
}
