package com.resource.management.api;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.resource.management.model.ResourceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Resource
{
   @Id
   private String plateNumber;

   private String vehicleType;

   private String captain;

   private String identificationNumber;

   private List<String> crew;

   private ResourceStatus status;
}
