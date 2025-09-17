//package com.garden.api.dashboard_metrics;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@AllArgsConstructor
//@RestController
//@RequestMapping("/api")
//public class DashboardController {
//
//    public static final String BASE_PATH_V1 = "/v1/dashboard";
//    private final DashboardService service;
//
//    @GetMapping(BASE_PATH_V1 + "/total-customers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DashboardMetric totalCustomers() {
//        return service.getTotalCustomers();
//    }
//
//    @GetMapping(BASE_PATH_V1 + "/active-projects")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DashboardMetric activeProjects() {
//        return service.getActiveProjects();
//    }
//
//    @GetMapping(BASE_PATH_V1 + "/appointments-this-month")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DashboardMetric appointmentsThisMonth() {
//        return service.getAppointmentsThisMonth();
//    }
//
//    @GetMapping(BASE_PATH_V1 + "/monthly-revenue")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DashboardMetric monthlyRevenue() {
//        return service.getMonthlyRevenue();
//    }
//}
