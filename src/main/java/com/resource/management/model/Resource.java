package com.resource.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.resource.management.model.status.ResourceStatus;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Resource
{
   @Id
   private String plateNumber;

   private String vehicleType;

   private String captain;

   private String identificationNumber;

   private List<String> crew;

   @EqualsAndHashCode.Exclude
   private ResourceStatus status;

   @EqualsAndHashCode.Exclude
   private List<ResourceLog> resourceLogs;
}
