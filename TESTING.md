# FarmStock – Testing Guide

Base URL: `http://localhost:8082`

The crop names and numbers below are **only test data** typed in Postman.
Nothing is hardcoded in the application.

## 1. REST API (Postman)

| # | Request | Body | Expected |
|---|---------|------|----------|
| 1 | `POST /api/crops` | `{"name":"Tomato","category":"Vegetable"}` | 201, crop with id 1 |
| 2 | `POST /api/crops` | `{"name":"Rice","category":"Grain"}` | 201, crop with id 2 |
| 3 | `POST /api/harvest-batches` | `{"crop":{"id":1},"quantity":100,"harvestDate":"2026-09-05"}` | 201 |
| 4 | `POST /api/harvest-batches` | `{"crop":{"id":1},"quantity":50,"harvestDate":"2026-09-06"}` | 201 |
| 5 | `GET /api/sales/stock/1` | – | `150.0` |
| 6 | `POST /api/sales` | `{"crop":{"id":1},"quantity":40,"pricePerUnit":40,"saleDate":"2026-09-10"}` | 201 |
| 7 | `GET /api/sales/stock/1` | – | `110.0` |
| 8 | `POST /api/sales` | `{"crop":{"id":1},"quantity":20,"pricePerUnit":45,"saleDate":"2026-09-20"}` | 201 |
| 9 | `GET /api/sales/stock/1` | – | `90.0` |
| 10-11 | `POST /api/sales` | `{"crop":{"id":1},"quantity":100,"pricePerUnit":45,"saleDate":"2026-09-21"}` | 400 `Insufficient stock. Available stock: 90.0` |
| 12 | `GET /api/sales/stock/1` | – | `90.0` (rejected sale not saved) |
| 13-14 | `GET /api/sales/revenue/1?from=2026-09-01&to=2026-09-30` | – | `2500.0` (40×40 + 20×45) |
| 15 | `POST /api/sales` | quantity `-5` | 400 `Quantity must be greater than zero` |
| 16 | `POST /api/sales` | pricePerUnit `0` | 400 `Price per unit must be greater than zero` |
| 17 | `GET /api/crops/99` | – | 404 `Crop not found with id: 99` |
| 18 | `GET /api/harvest-batches/99` | – | 404 `Harvest batch not found with id: 99` |
| 19 | `GET /api/sales/99` | – | 404 `Sale not found with id: 99` |

Extra cases:

| Case | Request | Expected |
|------|---------|----------|
| Crop with no harvest | `GET /api/sales/stock/2` | `0.0` |
| Crop with no sales | `GET /api/sales/revenue/2?from=2026-09-01&to=2026-09-30` | `0.0` |
| Date range with no sales | `GET /api/sales/revenue/1?from=2026-08-01&to=2026-08-31` | `0.0` |
| All stock sold | sell 90, then `GET /api/sales/stock/1` | `0.0` |
| Sell after all sold | sell 1 more | 400 `Insufficient stock. Available stock: 0.0` |
| Empty crop name | `POST /api/crops` `{"name":"","category":""}` | 400 |
| Crop does not exist | `POST /api/sales` with `{"crop":{"id":999}, ...}` | 404 `Crop not found with id: 999` |
| From date after To date | `GET /api/sales/revenue/1?from=2026-09-30&to=2026-09-01` | 400 |

## 2. Browser pages (Thymeleaf)

| Page | URL |
|------|-----|
| Dashboard | `/` |
| Crops | `/crops` |
| Harvests | `/harvests` |
| Sales | `/sales` |
| Stock | `/stock` |
| Revenue | `/revenue` |

## 3. MySQL verification

```sql
SELECT * FROM crops;
SELECT * FROM harvest_batches;
SELECT * FROM sales;

-- stock per crop, calculated directly in SQL (should match /api/sales/stock/{id})
SELECT c.name,
       (SELECT COALESCE(SUM(quantity), 0) FROM harvest_batches h WHERE h.crop_id = c.id)
     - (SELECT COALESCE(SUM(quantity), 0) FROM sales s WHERE s.crop_id = c.id) AS stock
FROM crops c;

-- revenue for a crop and date range (should match /api/sales/revenue/{id})
SELECT SUM(quantity * price_per_unit)
FROM sales
WHERE crop_id = 1 AND sale_date BETWEEN '2026-09-01' AND '2026-09-30';
```

Checked: invalid and rejected records are not inserted, and the stock and revenue from SQL match the API.
