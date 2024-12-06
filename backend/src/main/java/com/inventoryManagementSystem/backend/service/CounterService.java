package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {
    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        Counter counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("sequenceValue", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );
        return counter != null ? counter.getSequenceValue() : 1;
    }
}
