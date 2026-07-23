package com.polakams.demoservice.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link Book} persistence.
 * <p>
 * CRUD operations are provided by {@link JpaRepository}; no custom queries yet.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
