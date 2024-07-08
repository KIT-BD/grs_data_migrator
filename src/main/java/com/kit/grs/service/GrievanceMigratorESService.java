package com.kit.grs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kit.grs.common.Utils;
import com.kit.grs.entity.EsComplain;
import com.kit.grs.entity.EsMovement;
import com.kit.grs.model.ComplainHistory;
import com.kit.grs.repository.BaseEntityManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GrievanceMigratorESService {

    @Value("${grievance.min.id}")
    private Integer min;

    @Value("${grievance.max.id}")
    private Integer max;

    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;
    private final BaseEntityManager entityManager;

    public GrievanceMigratorESService(RestHighLevelClient client, ObjectMapper objectMapper, BaseEntityManager entityManager) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.entityManager = entityManager;
    }

    public void migrate() {
        for (Integer i = min; i<=max;i++) {
            log.info("======PROCESSING:{}", i);
            this.processGrievanceHistory(i);
            log.info("======DONE:{}", i);
        }
    }

    public void processGrievanceHistory(Integer i) {

        GetRequest request = new GetRequest(Utils.INDEX_COMPLAINTS);
        request.id(String.valueOf(i));
        request.fetchSourceContext(new FetchSourceContext(true, Utils.complain_fields, null));
        EsComplain complain = null;
        try {
            GetResponse response = this.client.get(request, RequestOptions.DEFAULT);
            if (response.isExists()) {
                complain = this.objectMapper.readValue(response.getSourceAsString(), EsComplain.class);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (complain == null) {
            return;
        }

        List<EsMovement> movements = this.findMovements(i);
        if (movements.size() == 0) {
            return;
        }

        List<ComplainHistory> histories = new ArrayList<>();
        for (int j=0;j<movements.size();j++) {
            if (movements.get(j) == null) {
                continue;
            }
            if (movements.get(j).getAction().equalsIgnoreCase("NEW")) {
                histories.add(getHistory(complain,"NEW", movements.get(j).getCreated_at(), null, movements.get(j).getTo_office_id()));
                continue;
            }
            if (movements.get(j).getAction().equalsIgnoreCase("FORWARD_TO_ANOTHER_OFFICE")) {

                for (ComplainHistory his : histories) {
                    if ((his.getCurrentStatus().equalsIgnoreCase("NEW")) && his.getClosedAt() == null) {
                        his.setClosedAt(movements.get(j).getModified_at());
                    }
                }
                histories.add(getHistory(complain, "FORWARDED_OUT", movements.get(j).getModified_at(), movements.get(j).getModified_at(), movements.get(j).getFrom_office_id()));
                histories.add(getHistory(complain, "NEW", movements.get(j).getModified_at(), null, movements.get(j).getTo_office_id()));
                continue;
            }

            if (Utils.isInList(movements.get(j).getAction(), "CLOSED_ACCUSATION_INCORRECT", "CLOSED_ACCUSATION_PROVED", "CLOSED_ANSWER_OK", "CLOSED_OTHERS", "REJECTED")) {
                for (ComplainHistory his : histories) {
                    if ((his.getCurrentStatus().equalsIgnoreCase("NEW")) && his.getClosedAt() == null) {
                        his.setClosedAt(movements.get(j).getModified_at());
                    }
                }

                histories.add(getHistory(complain, "CLOSED", movements.get(j).getModified_at(), movements.get(j).getModified_at(), movements.get(j).getTo_office_id()));
                continue;
            }

            if (movements.get(j).getAction().equalsIgnoreCase("APPEAL")) {
                for (ComplainHistory his : histories) {
                    if (his.getCurrentStatus().equalsIgnoreCase("NEW") && his.getClosedAt() == null) {
                        his.setClosedAt(movements.get(j).getCreated_at());
                    }
                }
                histories.add(getHistory(complain, "APPEAL", movements.get(j).getCreated_at(), null, movements.get(j).getTo_office_id()));
                continue;
            }
            if (Utils.isInList(movements.get(j).getAction(), "APPEAL_CLOSED_ACCUSATION_INCORRECT","APPEAL_CLOSED_ACCUSATION_PROVED", "APPEAL_CLOSED_OTHERS")) {
                histories.add(getHistory(complain, "CLOSED", movements.get(j).getCreated_at(), movements.get(j).getModified_at(), movements.get(j).getTo_office_id()));
                for (ComplainHistory his : histories) {
                    if ((his.getCurrentStatus().equalsIgnoreCase("NEW") || his.getCurrentStatus().equalsIgnoreCase("APPEAL")) && his.getClosedAt() == null) {
                        his.setClosedAt(movements.get(j).getModified_at());
                    }
                }
                continue;
            }
            if (movements.get(j).getAction().equalsIgnoreCase("CELL_NEW")) {
                histories.add(getHistory(complain, "NEW", movements.get(j).getCreated_at(), null, movements.get(j).getTo_office_id()));
            }
        }

        if (histories.size() >0) {
            for (ComplainHistory his : histories) {
                try {
                    entityManager.save(his);
                } catch (Throwable t) {
                    log.error("===DUPLICATE FOR ID:{} REFERENCE:{} STATUS:{} OFFICE_ID:{}", his.getComplainId(), his.getTrackingNumber(), his.getCurrentStatus(), his.getOfficeId());
                    //t.printStackTrace();
                }

            }
        }

    }

    private ComplainHistory getHistory(EsComplain complain, String status, Date createdAt, Date closedAt, Long officeId) {
        ComplainHistory history = new ComplainHistory();
        history.setGrievanceType(complain.getGrievance_type());
        history.setComplainId(complain.getId());
        history.setCreatedAt(createdAt);
        history.setClosedAt(closedAt);
        history.setCurrentStatus(status);
        history.setCustomLayer(complain.getCustom_layer());
        history.setLayerLevel(complain.getLayer_level());
        history.setMediumOfSubmission(complain.getMedium_of_submission());
        history.setOfficeId(officeId);
        history.setOfficeOrigin(complain.getOffice_origin());
        history.setSelfMotivated(complain.getSelf_motivated());
        history.setTrackingNumber(complain.getTracking_number());
        return history;
    }


    private List<EsMovement> findMovements(Integer id) {

        BoolQueryBuilder queryBuilders = new BoolQueryBuilder();
        queryBuilders.must(QueryBuilders.termQuery("complaint_id", id));

        BoolQueryBuilder should = new BoolQueryBuilder();
        should.should(QueryBuilders.matchQuery("action", "NEW"));
        should.should(QueryBuilders.matchQuery("action", "APPEAL"));
        should.should(QueryBuilders.matchQuery("action", "APPEAL_CLOSED_ACCUSATION_INCORRECT").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "APPEAL_CLOSED_ACCUSATION_PROVED").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "APPEAL_CLOSED_OTHERS").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "CELL_NEW").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "CLOSED_ACCUSATION_INCORRECT").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "CLOSED_ACCUSATION_PROVED").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "CLOSED_ANSWER_OK").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "CLOSED_OTHERS").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "FORWARD_TO_ANOTHER_OFFICE").operator(Operator.AND));
        should.should(QueryBuilders.matchQuery("action", "REJECTED"));
        should.minimumShouldMatch(1);

        queryBuilders.must(should);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilders);
        sourceBuilder.sort("id", SortOrder.ASC);

        SearchRequest request = new SearchRequest(Utils.INDEX_MOVEMENT);
        request.source(sourceBuilder);
        List<EsMovement> movements = new ArrayList<>();
        try {

            SearchResponse response = this.client.search(request, RequestOptions.DEFAULT);
            if (response != null && response.getHits() != null && response.getHits().getHits() != null) {
                for (SearchHit hit : response.getHits().getHits()) {
                    if (hit != null && hit.getSourceAsString() != null) {
                        movements.add(objectMapper.readValue(hit.getSourceAsString(), EsMovement.class));
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return movements;
    }

}
