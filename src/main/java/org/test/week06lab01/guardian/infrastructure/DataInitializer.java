package org.test.week06lab01.guardian.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.test.week06lab01.guardian.domain.Guardian;

import java.time.Instant;

    @Component
    public class DataInitializer implements CommandLineRunner {

        private final GuardianRepository guardianRepository;

        // Inyección de las variables de entorno/properties
        @Value("${ADMIN_NAME}")
        private String adminName;

        @Value("${ADMIN_EMAIL}")
        private String adminEmail;

        @Value("${ADMIN_NOTIFICATION_EMAIL}")
        private String adminNotificationEmail;

        public DataInitializer(GuardianRepository guardianRepository) {
            this.guardianRepository = guardianRepository;
        }

        @Override
        public void run(String... args) throws Exception {
            // Verificar si ya existe un guardián con ese email
            if (!guardianRepository.existsByEmail(adminEmail)) {

                Guardian admin = new Guardian();
                admin.setDisplayName(adminName);
                admin.setEmail(adminEmail);
                admin.setNotificationEmail(adminNotificationEmail);
                admin.setCreatedAt(Instant.now());

                guardianRepository.save(admin);

                System.out.println(">>> DataInitializer: Guardián administrador '" + adminName + "' creado exitosamente.");
            } else {
                System.out.println(">>> DataInitializer: El guardián administrador ya existe. Omitiendo creación.");
            }
        }
    }

