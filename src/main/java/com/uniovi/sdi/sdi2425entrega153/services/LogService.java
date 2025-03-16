package com.uniovi.sdi.sdi2425entrega153.services;

import com.uniovi.sdi.sdi2425entrega153.entities.LogEntry;
import com.uniovi.sdi.sdi2425entrega153.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void saveLog(String username, String action, String endpoint) {
        LogEntry log = new LogEntry(username, action, endpoint);
        logRepository.save(log);
    }


}