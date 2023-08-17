package com.smartcar.sdk.data;

public class ConnectionsFilter {
   private String userId;
   private String vehicleId;

   private ConnectionsFilter(Builder builder) {
      this.userId = builder.userId;
      this.vehicleId = builder.vehicleId;
   }

   public String getUserId() {
      return userId;
   }

   public String getVehicleId() {
      return vehicleId;
   }

   public static class Builder {
      private String userId;
      private String vehicleId;

      public ConnectionsFilter.Builder userId(String userId) {
         this.userId = userId;
         return this;
      }

      public ConnectionsFilter.Builder vehicleId(String vehicleId) {
         this.vehicleId = vehicleId;
         return this;
      }

      public ConnectionsFilter build() {
         return new ConnectionsFilter(this);
      }
   }

   @Override
   public String toString() {
      return "ConnectionsFilter{" +
              "userId='" + userId + '\'' +
              ", vehicleId='" + vehicleId + '\'' +
              '}';
   }
}
