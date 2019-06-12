package com.resource.management.api.services;

import java.util.List;

import com.resource.management.services.model.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServicesListUpdatedNotification
{
   private List<Service> services;

   private String lastUpdateToday;

   private String lastUpdateTomorrow;
}
