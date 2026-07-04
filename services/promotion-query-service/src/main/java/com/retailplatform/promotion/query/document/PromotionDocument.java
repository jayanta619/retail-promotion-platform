package com.retailplatform.promotion.query.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document(indexName = "promotions")
public class PromotionDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Double)
    private BigDecimal discountValue;

    @Field(type = FieldType.Keyword)
    private String zoneCode;

    @Field(type = FieldType.Date)
    private Instant startDate;

    @Field(type = FieldType.Date)
    private Instant endDate;
}
