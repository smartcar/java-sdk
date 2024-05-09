package com.smartcar.sdk.data;

import java.util.List;

public class ServiceRecord {
    private int serviceId;
    private String serviceDate;
    private int odometerDistance;
    private List<ServiceTask> serviceTasks;
    private ServiceDetails serviceDetails;
    private ServiceCost serviceCost;

    public ServiceRecord(int serviceId, String serviceDate, int odometerDistance,
            List<ServiceTask> serviceTasks, ServiceDetails serviceDetails,
            ServiceCost serviceCost) {
        this.serviceId = serviceId;
        this.serviceDate = serviceDate;
        this.odometerDistance = odometerDistance;
        this.serviceTasks = serviceTasks;
        this.serviceDetails = serviceDetails;
        this.serviceCost = serviceCost;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public int getOdometerDistance() {
        return odometerDistance;
    }

    public void setOdometerDistance(int odometerDistance) {
        this.odometerDistance = odometerDistance;
    }

    public List<ServiceTask> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(List<ServiceTask> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }

    public ServiceDetails getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(ServiceDetails serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public ServiceCost getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(ServiceCost serviceCost) {
        this.serviceCost = serviceCost;
    }
}
