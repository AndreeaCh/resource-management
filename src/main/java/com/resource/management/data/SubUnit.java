package com.resource.management.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubUnit
{
   @Id
   private String name;

   private List<Resource> resources;

   private Date lastUpdate;

}
