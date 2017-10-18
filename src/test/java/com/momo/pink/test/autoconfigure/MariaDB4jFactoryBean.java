package com.momo.pink.test.autoconfigure;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.beans.factory.annotation.Value;

public class MariaDB4jFactoryBean extends MariaDB4jSpringService {

    @Value("${spring.test.maria.database:test}")
    private String database;

    @Override
    public void start() {
        super.start();
        DB db = getDB();

        try {
            if (this.database != null) {
                db.createDB(this.database);
            }
        } catch (ManagedProcessException e) {
            lastException = e;
            throw new IllegalStateException("MariaDB4jSpringService start() failed", e);
        }
    }
}


