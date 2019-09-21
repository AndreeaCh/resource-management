package com.resource.management.api.resources;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Service
{

   @Id
   private String id;

   private String name;

   private String title;

   private String role;

   private String subUnit;

   private String contact;

   private String day;
}
