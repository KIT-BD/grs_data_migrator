package com.kit.grs.component;

import com.kit.grs.service.GrievanceMigratorESService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final GrievanceMigratorESService service;

    public Runner(GrievanceMigratorESService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.migrate();
    }
}
