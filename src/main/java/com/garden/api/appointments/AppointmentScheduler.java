package com.garden.api.appointments;

import com.garden.api.email.EmailDetails;
import com.garden.api.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentScheduler {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 * * * *")
    public void sendAppointmentReminders() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.plusHours(24);

        List<Appointment> upcomingAppointments =
                appointmentRepository.findByDateTimeBetween(
                        target.minusMinutes(30),
                        target.plusMinutes(30)
                );

        for (Appointment appt : upcomingAppointments) {

            String to = "fidi_der@hotmail.com";

            if (to == null || to.isBlank()) continue;

            String subject = "Appointment Reminder";
            String text = """
                    You have an appointment tomorrow at:
                    %s
                    With: %s
                    
                    Please be on time.
                    """.formatted(
                    appt.getDateTime(),
                    appt.getClientName()
            );

            EmailDetails details = new EmailDetails(to, subject, text);
            emailService.send(details);
        }
    }
}
