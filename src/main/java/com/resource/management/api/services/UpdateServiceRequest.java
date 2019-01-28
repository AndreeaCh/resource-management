package com.resource.management.api.services;

import com.resource.management.api.resources.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateServiceRequest {
    Service service;
}
