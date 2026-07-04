CREATE TABLE promotions (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    discount_value NUMERIC(10,2),
    zone_code VARCHAR(20),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT
);

CREATE TABLE promotion_rules (
    id UUID PRIMARY KEY,
    promotion_id UUID REFERENCES promotions(id),
    rule_key VARCHAR(100) NOT NULL,
    rule_value VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT
);
