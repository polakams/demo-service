INSERT INTO books (id, title, author, isbn, published_year) VALUES
  (1, 'The Hitchhiker''s Guide to the Galaxy', 'Douglas Adams', '9780345391803', 1979),
  (2, 'Clean Code', 'Robert C. Martin', '9780132350884', 2008),
  (3, 'Domain-Driven Design', 'Eric Evans', '9780321125217', 2003),
  (4, 'Effective Java', 'Joshua Bloch', '9780134685991', 2018),
  (5, 'Designing Data-Intensive Applications', 'Martin Kleppmann', '9781449373320', 2017);

ALTER TABLE books ALTER COLUMN id RESTART WITH 6;
