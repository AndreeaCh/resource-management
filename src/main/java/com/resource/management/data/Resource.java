package com.resource.management.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import lombok.Data;

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

   private int identificationNumber;

   private List<String> crew;

   private ResourceStatus status;

}
