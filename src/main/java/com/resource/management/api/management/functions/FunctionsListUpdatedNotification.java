package com.resource.management.api.management.functions;

import com.resource.management.management.functions.model.Function;
import com.resource.management.services.model.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FunctionsListUpdatedNotification {
    private List<Function> functions;
}
