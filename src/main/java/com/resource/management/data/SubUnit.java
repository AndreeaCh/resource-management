package com.resource.management.data;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SubUnit
{
   @Id
   private String name;

   private List<Resource> resources;

}
