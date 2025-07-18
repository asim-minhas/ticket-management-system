package com.ticket_management_system.ticketing_service.sequence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class TicketKeyGenerator {
  private final MongoTemplate mongo;
  private final TenantCodeUtil      codeUtil;


  public TicketKeyGenerator(MongoTemplate mongo, TenantCodeUtil codeUtil) {
    this.mongo = mongo;
      this.codeUtil = codeUtil;
  }

  public String nextTicketId(String tenantId) {
    // find the counter doc for this tenant, increment seq by 1, return the new value
    String tenantCode = codeUtil.deriveCode(tenantId);

    var query = Query.query(Criteria.where("_id").is(tenantCode));
    var update = new Update().inc("seq", 1);
    var opts   = FindAndModifyOptions.options().returnNew(true).upsert(true);
    Counter counter = mongo.findAndModify(query, update, opts, Counter.class, "counters");
    return tenantCode + "-" + counter.getSeq();
  }

  // helper DTO for the counters collection
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Counter {
    @Id
    private String id;
    private long   seq;
  }
}
