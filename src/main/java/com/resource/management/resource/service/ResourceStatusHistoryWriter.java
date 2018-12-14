package com.resource.management.resource.service;

import com.resource.management.resource.model.ResourceLog;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ResourceStatusHistoryWriter {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceStatusHistoryWriter.class);

    @Value("${resource.status.file}")
    private String fileName;

    public void addLogToFile(final String plateNumber, final ResourceLog resourceLog) {

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileUtils.writeStringToFile(file, plateNumber + ": " + resourceLog.toString() + '\n', StandardCharsets.UTF_8, true);
        } catch (IOException e) {
            LOG.error("Could not add resource status to file.", e);
        }
    }
}
