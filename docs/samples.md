# Request samples


## Success request

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"amount": 250.0, "documentId": "12345678-X", "customerName": "John Doe"}'

```

## Error request
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000.0, "documentId": "98765432-Z", "customerName": "Jane Doe"}'
```
