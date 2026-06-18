### 通知 Table

```sql
CREATE TABLE notifications (
    id          SERIAL PRIMARY KEY,
    user_chat_id VARCHAR(50) NOT NULL,   -- Telegram Chat ID
    campsite_id  BIGINT REFERENCES campstores(id),
    target_date  DATE NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT NOW()
);
```