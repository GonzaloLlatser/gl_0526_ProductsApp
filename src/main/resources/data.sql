INSERT INTO products (id, name, description)
VALUES
  (1, 'Zapatillas deportivas', 'Modelo 2025 edicion limitada'),
  (2, 'Camiseta tecnica', 'Camiseta transpirable para running'),
  (3, 'Mochila urbana', 'Mochila impermeable de 20 litros'),
  (4, 'Chaqueta ligera', 'Chaqueta cortavientos de entretiempo'),
  (5, 'Pantalon denim', 'Pantalon vaquero regular fit'),
  (6, 'Producto con historial largo', 'Producto seed para probar paginacion y ordenacion');

INSERT INTO prices (id, product_id, price_value, currency, init_date, end_date)
VALUES
  (1, 1, 99.99, 'EUR', DATE '2024-01-01', DATE '2024-06-30'),
  (2, 1, 149.99, 'EUR', DATE '2024-07-01', DATE '2024-12-31'),
  (3, 1, 199.99, 'EUR', DATE '2025-01-01', DATE '2025-06-30'),
  (4, 2, 19.99, 'USD', DATE '2024-01-01', DATE '2024-03-31'),
  (5, 2, 24.99, 'USD', DATE '2024-04-01', DATE '2024-12-31'),
  (6, 2, 29.99, 'USD', DATE '2025-01-01', NULL),
  (7, 3, 39.99, 'GBP', DATE '2024-02-01', DATE '2024-08-31'),
  (8, 3, 34.99, 'GBP', DATE '2024-09-01', DATE '2024-11-30'),
  (9, 3, 44.99, 'GBP', DATE '2024-12-01', NULL),
  (10, 4, 59.99, 'EUR', DATE '2024-01-15', DATE '2024-05-31'),
  (11, 4, 49.99, 'EUR', DATE '2024-06-01', DATE '2024-08-31'),
  (12, 4, 69.99, 'EUR', DATE '2024-09-01', NULL),
  (13, 5, 45.99, 'USD', DATE '2024-01-01', DATE '2024-12-31'),
  (14, 5, 54.99, 'GBP', DATE '2025-01-01', NULL),
  (15, 6, 10.99, 'EUR', DATE '2022-01-01', DATE '2022-01-31'),
  (16, 6, 11.99, 'EUR', DATE '2022-02-01', DATE '2022-02-28'),
  (17, 6, 12.99, 'EUR', DATE '2022-03-01', DATE '2022-03-31'),
  (18, 6, 13.99, 'EUR', DATE '2022-04-01', DATE '2022-04-30'),
  (19, 6, 14.99, 'EUR', DATE '2022-05-01', DATE '2022-05-31'),
  (20, 6, 15.99, 'EUR', DATE '2022-06-01', DATE '2022-06-30'),
  (21, 6, 16.99, 'EUR', DATE '2022-07-01', DATE '2022-07-31'),
  (22, 6, 17.99, 'EUR', DATE '2022-08-01', DATE '2022-08-31'),
  (23, 6, 18.99, 'EUR', DATE '2022-09-01', DATE '2022-09-30'),
  (24, 6, 19.99, 'EUR', DATE '2022-10-01', DATE '2022-10-31'),
  (25, 6, 20.99, 'EUR', DATE '2022-11-01', DATE '2022-11-30'),
  (26, 6, 21.99, 'EUR', DATE '2022-12-01', DATE '2022-12-31'),
  (27, 6, 22.99, 'EUR', DATE '2023-01-01', DATE '2023-01-31'),
  (28, 6, 23.99, 'EUR', DATE '2023-02-01', DATE '2023-02-28'),
  (29, 6, 24.99, 'EUR', DATE '2023-03-01', DATE '2023-03-31'),
  (30, 6, 25.99, 'EUR', DATE '2023-04-01', DATE '2023-04-30'),
  (31, 6, 26.99, 'EUR', DATE '2023-05-01', DATE '2023-05-31'),
  (32, 6, 27.99, 'EUR', DATE '2023-06-01', DATE '2023-06-30'),
  (33, 6, 28.99, 'EUR', DATE '2023-07-01', DATE '2023-07-31'),
  (34, 6, 29.99, 'EUR', DATE '2023-08-01', DATE '2023-08-31'),
  (35, 6, 30.99, 'EUR', DATE '2023-09-01', DATE '2023-09-30'),
  (36, 6, 31.99, 'EUR', DATE '2023-10-01', DATE '2023-10-31'),
  (37, 6, 32.99, 'EUR', DATE '2023-11-01', DATE '2023-11-30'),
  (38, 6, 33.99, 'EUR', DATE '2023-12-01', NULL);

ALTER TABLE products ALTER COLUMN id RESTART WITH 7;
ALTER TABLE prices ALTER COLUMN id RESTART WITH 39;
