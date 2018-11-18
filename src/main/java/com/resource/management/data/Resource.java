package com.resource.management.data;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Resource
{
   @Id
   private String plateNumber;

   private String vehicleType;

   private String captain;

   private int identificationNumber;

   private List<String> crew;

   private ResourceStatus status;

}
