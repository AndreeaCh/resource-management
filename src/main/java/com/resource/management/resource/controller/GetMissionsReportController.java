package com.resource.management.resource.controller;

import static com.resource.management.resource.service.MissionsXlsCreator.MISSIONS_REPORT_FILE_NAME;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.service.MissionsXlsCreator;

@Controller
public class GetMissionsReportController
{
   private static final Logger LOG = LoggerFactory.getLogger( GetMissionsReportController.class );

   @Autowired
   private SimpMessagingTemplate messagingTemplate;

   @Autowired
   private SubUnitsRepository repository;

   @Autowired
   private MissionsXlsCreator xlsCreator;


   @MessageMapping("/getMissionsReport")
   public void getMissionsReportRequest(
         @Payload final LockSubUnitRequest request,
         final SimpMessageHeaderAccessor headerAccessor )
   {

      final List<SubUnit> subUnits = this.repository.findAll();
       this.xlsCreator.createXls( subUnits );
      final String xlsFileContentAsBase64 = getXLSFileContentAsBase64();
       this.messagingTemplate.convertAndSendToUser(
            headerAccessor.getSessionId(), "/queue/missionsReport", xlsFileContentAsBase64,
            headerAccessor.getMessageHeaders() );
   }


   private String getXLSFileContentAsBase64()
   {
      try
      {
         final byte[] input_file = Files.readAllBytes( Paths.get( MISSIONS_REPORT_FILE_NAME ) );
         final byte[] encodedBytes = Base64.encodeBase64( input_file );
         return new String( encodedBytes );
      }
      catch ( final IOException ex )
      {
         LOG.info( "Error writing file to output stream. Filename was '{}'", MISSIONS_REPORT_FILE_NAME, ex );
         throw new RuntimeException( "IOError writing file to output stream" );
      }
   }
}
