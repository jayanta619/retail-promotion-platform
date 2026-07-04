# Kafka Event Flow

## Topics

| Topic | Producer | Consumer | Purpose |
|---|---|---|---|
| promotion.created.v1 | promotion-command-service | promotion-query-service | Propagate new promotions to the read model |

## Event Schema: PromotionCreatedEvent

```json
{
  "promotionId": "uuid",
  "name": "string",
  "type": "PERCENTAGE_DISCOUNT | FIXED_AMOUNT_DISCOUNT | BUY_ONE_GET_ONE | BUNDLE",
  "discountValue": "decimal",
  "zoneCode": "string",
  "startDate": "ISO-8601",
  "endDate": "ISO-8601",
  "occurredAt": "ISO-8601"
}
```

## Delivery Guarantees

The command service publishes the event synchronously after the database commit succeeds within the same request thread. In a future phase, this should be hardened using the Transactional Outbox pattern to guarantee exactly-once delivery in case of a Kafka broker outage between DB commit and publish.
