# Cassandra Timeseries Data Modeling

## Table Definition

```
CREATE TABLE Events ( 
  user_id uuid,
  timestamp timestamp,
  event_id uuid,
  body text,
  PRIMARY KEY (user_id, timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);
```

This project uses Http4s and Alpakka Cassandra connector.
