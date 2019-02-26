package com.resource.management.api.management.functions;

import com.resource.management.management.functions.model.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateFunctionsRequest {
    private List<Function> functions;
}
