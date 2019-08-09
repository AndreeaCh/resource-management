package com.resource.management.api.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddServiceRequest
{
   private String name;

   private String title;

   private String role;

   private String subUnit;

   private String contact;

   private String day;
}
