//package com.garden.api.dashboard_metrics;
//
//import com.garden.api.clients.ClientRepository;
//import com.garden.api.projects.ProjectRepository;
//import com.garden.api.appointments.AppointmentRepository;
//import com.garden.api.projects.ProjectStatus;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.YearMonth;
//
//@AllArgsConstructor
//@Service
//public class DashboardService {
//
//    private final ClientRepository clientRepository;
//    private final ProjectRepository projectRepository;
//    private final AppointmentRepository appointmentRepository;
//
//    public DashboardMetric getTotalCustomers() {
//        long thisMonth = clientRepository.countByCreatedAtBetween(
//                YearMonth.now().atDay(1),
//                LocalDate.now()
//        );
//
//        long lastMonth = clientRepository.countByCreatedAtBetween(
//                YearMonth.now().minusMonths(1).atDay(1),
//                YearMonth.now().minusMonths(1).atEndOfMonth()
//        );
//
//
//        long total = clientRepository.count();
//        double change = lastMonth == 0 ? 100.0 : ((double) (thisMonth - lastMonth) / lastMonth) * 100;
//
//        return new DashboardMetric("Total Customers", total, change, String.valueOf(total));
//    }
//
//    public DashboardMetric getActiveProjects() {
//        long thisMonth = projectRepository.countByStatusAndCreatedAtBetween(
//                ProjectStatus.Scheduled,
//                YearMonth.now().atDay(1),
//                LocalDate.now()
//        );
//
//        long lastMonth = projectRepository.countByStatusAndCreatedAtBetween(
//                ProjectStatus.Scheduled,
//                YearMonth.now().minusMonths(1).atDay(1),
//                YearMonth.now().minusMonths(1).atEndOfMonth()
//        );
//
//        long total = projectRepository.countByStatus(ProjectStatus.Scheduled);
//        double change = lastMonth == 0 ? 100.0 : ((double) (thisMonth - lastMonth) / lastMonth) * 100;
//
//        return new DashboardMetric("Active Projects", total, change, String.valueOf(total));
//    }
//
//    public DashboardMetric getAppointmentsThisMonth() {
//        LocalDate startOfMonth = YearMonth.now().atDay(1);
//        LocalDate today = LocalDate.now();
//        long thisMonthCount = appointmentRepository.countByDateBetween(startOfMonth, today);
//
//        LocalDate startOfLastMonth = YearMonth.now().minusMonths(1).atDay(1);
//        LocalDate endOfLastMonth = YearMonth.now().minusMonths(1).atEndOfMonth();
//        long lastMonthCount = appointmentRepository.countByDateBetween(startOfLastMonth, endOfLastMonth);
//
//        double change = lastMonthCount == 0 ? 100.0 : ((double) (thisMonthCount - lastMonthCount) / lastMonthCount) * 100;
//
//        return new DashboardMetric("This Month Appointments", thisMonthCount, change, String.valueOf(thisMonthCount));
//    }
//
//    public DashboardMetric getMonthlyRevenue() {
//        LocalDate startOfMonth = YearMonth.now().atDay(1);
//        LocalDate today = LocalDate.now();
//
//        BigDecimal thisMonthRevenue = projectRepository.sumRevenueByCreatedAtBetween(startOfMonth, today);
//        BigDecimal lastMonthRevenue = projectRepository.sumRevenueByCreatedAtBetween(
//                YearMonth.now().minusMonths(1).atDay(1),
//                YearMonth.now().minusMonths(1).atEndOfMonth()
//        );
//
//        double change = lastMonthRevenue.compareTo(BigDecimal.ZERO) == 0 ? 100.0 :
//                (thisMonthRevenue.subtract(lastMonthRevenue).doubleValue() / lastMonthRevenue.doubleValue()) * 100;
//
//        return new DashboardMetric(
//                "Monthly Revenue",
//                thisMonthRevenue.doubleValue(),
//                change,
//                "$" + thisMonthRevenue
//        );
//    }
//}
