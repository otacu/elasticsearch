package com.egoist.elasticsearch.component;

import com.egoist.elasticsearch.common.constants.ESConstant;
import com.egoist.elasticsearch.common.enums.ESDocOperateTypeEnum;
import com.egoist.parent.common.constants.EgoistResultStatusConstants;
import com.egoist.parent.pojo.dto.EgoistResult;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Description: es封装
 */
@Component
public class ElasticSearchUtil {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ResourceLoader resourceLoader;

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建索引
     *
     * @param index    index_order_sub
     * @param type     type_order_sub
     * @param template template_order_sub
     * @return 结果
     */
    public EgoistResult createIndex(String index, String type, String template) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);//创建索引
            Resource resource = resourceLoader.getResource(ESConstant.TEMPLATE_FILE_PATH + template);
            File file = resource.getFile();
            String fileContent = FileUtils.readFileToString(file, "UTF-8");
            //创建索引时创建文档类型映射
            request.mapping(type,//类型定义
                    fileContent,//类型映射，需要的是一个JSON字符串
                    XContentType.JSON);
            //同步执行
            CreateIndexResponse createIndexResponse = client.indices().create(request);
            boolean acknowledged = createIndexResponse.isAcknowledged();//指示是否所有节点都已确认请求
            if (!acknowledged) {
                return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "创建索引异常", null);
            }
            return EgoistResult.ok();
        } catch (Exception e) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "创建索引异常", null);
        }
    }

    /**
     * 创建索引文档
     *
     * @param index
     * @param type
     * @param docId
     * @param routing    路由（取父节点的id）
     * @param properties
     * @return
     */
    public EgoistResult createDoc(String index, String type, String docId,
                                  Map<String, Object> properties, String routing) {
        try {
            IndexRequest indexRequest = buildIndexRequest(index, type, docId, properties, routing);
            IndexResponse indexResponse = client.index(indexRequest);
            return EgoistResult.ok();
        } catch (Exception e) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "创建文档异常", null);
        }
    }

    private IndexRequest buildIndexRequest(String index, String type, String docId, Map<String, Object> properties,
                                           String routing) {
        IndexRequest indexRequest = new IndexRequest(index, type, docId);
        indexRequest.source(properties);
        indexRequest.routing(routing);//设置路由值
        indexRequest.opType(DocWriteRequest.OpType.CREATE);//DocWriteRequest.OpType方式
        return indexRequest;
    }

    /**
     * 删除文档
     *
     * @param index
     * @param type
     * @param docId
     * @return
     */
    public EgoistResult deleteDoc(String index, String type, String docId) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, type, docId);
            //同步执行
            DeleteResponse deleteResponse = client.delete(deleteRequest);
            return EgoistResult.ok();
        } catch (Exception e) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "删除文档异常", null);
        }
    }

    /**
     * 更新文档
     *
     * @param index
     * @param type
     * @param docId
     * @param properties
     * @return
     */
    public EgoistResult updateDoc(String index, String type, String docId, Map<String, Object> properties) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, type, docId);
            updateRequest.doc(properties);
            //同步执行
            UpdateResponse updateResponse = client.update(updateRequest);
            return EgoistResult.ok();
        } catch (Exception e) {
            return new EgoistResult(EgoistResultStatusConstants.STATUS_400, "更新文档异常", null);
        }
    }


    /**
     * 批量操作文档
     *
     * @param paramList
     * @return
     */
    public EgoistResult bulkOperateDoc(List<Map<String, Object>> paramList) {
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> param : paramList) {
            ESDocOperateTypeEnum operateType = (ESDocOperateTypeEnum) param.get("operateType");
            String index = (String) param.get("index");
            String type = (String) param.get("type");
            String docId = (String) param.get("docId");
            if (operateType == ESDocOperateTypeEnum.CREATE) {
                Map<String, Object> properties = (Map<String, Object>) param.get("properties");
                String routing = (String) param.get("routing");
                IndexRequest indexRequest = this.buildIndexRequest(index, type, docId, properties, routing);
                bulkRequest.add(indexRequest);
            }
        }

        // 异步处理
        client.bulkAsync(bulkRequest, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                        IndexResponse indexResponse = (IndexResponse) itemResponse;

                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;

                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    }
                    // 失败的返回
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();

                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        return EgoistResult.ok();
    }

}
