INSERT INTO products (id, name, description)
VALUES
  (1, 'Zapatillas deportivas', 'Modelo 2025 edicion limitada'),
  (2, 'Camiseta tecnica', 'Camiseta transpirable para running'),
  (3, 'Mochila urbana', 'Mochila impermeable de 20 litros'),
  (4, 'Chaqueta ligera', 'Chaqueta cortavientos de entretiempo'),
  (5, 'Pantalon denim', 'Pantalon vaquero regular fit');

INSERT INTO prices (id, product_id, price_value, init_date, end_date)
VALUES
  (1, 1, 99.99, DATE '2024-01-01', DATE '2024-06-30'),
  (2, 1, 149.99, DATE '2024-07-01', DATE '2024-12-31'),
  (3, 1, 199.99, DATE '2025-01-01', DATE '2025-06-30'),
  (4, 2, 19.99, DATE '2024-01-01', DATE '2024-03-31'),
  (5, 2, 24.99, DATE '2024-04-01', DATE '2024-12-31'),
  (6, 2, 29.99, DATE '2025-01-01', NULL),
  (7, 3, 39.99, DATE '2024-02-01', DATE '2024-08-31'),
  (8, 3, 34.99, DATE '2024-09-01', DATE '2024-11-30'),
  (9, 3, 44.99, DATE '2024-12-01', NULL),
  (10, 4, 59.99, DATE '2024-01-15', DATE '2024-05-31'),
  (11, 4, 49.99, DATE '2024-06-01', DATE '2024-08-31'),
  (12, 4, 69.99, DATE '2024-09-01', NULL),
  (13, 5, 45.99, DATE '2024-01-01', DATE '2024-12-31'),
  (14, 5, 54.99, DATE '2025-01-01', NULL);

ALTER TABLE products ALTER COLUMN id RESTART WITH 6;
ALTER TABLE prices ALTER COLUMN id RESTART WITH 15;
