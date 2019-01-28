package com.resource.management.resource.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogEntryWriter
{
    private static final Logger LOG = LoggerFactory.getLogger(LogEntryWriter.class);

    @Value("${resource.status.file}")
    private String fileName;


   public void addLogToFile( final String resourceIdentifier, final String resourceLog )
   {

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

           FileUtils.writeStringToFile( file, resourceIdentifier + ": " + resourceLog + "\r\n",
                 StandardCharsets.UTF_8, true );
        }
        catch ( IOException e )
        {
            LOG.error("Could not add resource status to file.", e);
        }
    }
}
