package com.kit.grs.component;

import com.kit.grs.service.GrievanceMigratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final GrievanceMigratorService service;

    public Runner(GrievanceMigratorService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.migrate();
    }
}
