package com.garden.api.dashboard_metrics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardMetric {
    private String name;
    private double value;
    private double percentageChange;
    private String displayValue;
}
