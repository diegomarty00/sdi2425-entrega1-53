package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntry, Long> {

}