package com.smartcar.sdk.data;

class ServiceTask {
    private int taskId;
    private String taskDescription;

    public ServiceTask(int taskId, String taskDescription) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}