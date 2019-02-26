package com.resource.management.management.functions.service;

import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.Functions;
import com.resource.management.management.functions.model.FunctionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FunctionsService {
    @Autowired
    private FunctionsRepository repository;

    @Transactional
    public List<Function> findAndUpdate(final Function function) {
        Optional<Functions> functions = repository.findById(Functions.ID);
        functions.ifPresent(f -> {
            Optional<Function> functionToUpdate = f.getFunctions().stream()
                    .filter(func -> func.getId().equals(function.getId()))
                    .findFirst();

            functionToUpdate.ifPresent(func -> {
                int index = f.getFunctions().indexOf(func);
                f.getFunctions().remove(func);
                f.getFunctions().add(index, function);
            });

            repository.save(f);
        });

        return getFunctionsList();
    }

    private List<Function> getFunctionsList() {
        List<Function> functionList = new ArrayList<>();
        Optional<Functions> functions = repository.findById(Functions.ID);
        if (functions.isPresent()) {
            functionList = functions.get().getFunctions();
        }

        return functionList;
    }
}
