package com.garden.api.appointments;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AppointmentController {

    public static final String BASE_PATH_V1 = "/v1/appointments";

    private final AppointmentService service;

    @PostMapping(BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> addAppointment(@RequestBody AppointmentRequest request) {
        Long id = service.addAppointment(request);
        return ResponseEntity.ok(id);
    }

    @PutMapping(BASE_PATH_V1 + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        service.updateAppointment(id, request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(BASE_PATH_V1 + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        service.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(BASE_PATH_V1 + "/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse response = service.findAppointmentById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AppointmentResponse> getAllAppointments(
            @RequestParam(value = "status", required = false) AppointmentStatus status,
            @PageableDefault(size = 20, sort = {"date", "time"}, direction = Sort.Direction.ASC) Pageable pageable
    ){
        return service.findAllByStatus(status, pageable);
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AppointmentResponse> getAppointmentsByClient(
            @PathVariable Long clientId,
            @PageableDefault(size = 20, sort = {"date", "time"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return service.findAllByClientId(clientId, pageable);
    }
}
