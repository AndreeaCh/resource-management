/************************************************************************
 ** PROJECT:   XVP
 ** LANGUAGE:  Java, J2SE JDK 1.8
 **
 ** COPYRIGHT: FREQUENTIS AG
 **            Innovationsstrasse 1
 **            A-1100 VIENNA
 **            AUSTRIA
 **            tel +43 1 811 50-0
 **
 ** The copyright to the computer program(s) herein
 ** is the property of Frequentis AG, Austria.
 ** The program(s) shall not be used and/or copied without
 ** the written permission of Frequentis AG.
 **
 ************************************************************************/
package com.resource.management.resource.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.resource.management.resource.model.SubUnit;

@Service
public class MissionsXlsCreator
{
   private static final Logger LOG = LoggerFactory.getLogger( MissionsXlsCreator.class );

   public static final String MISSIONS_REPORT_FILE_NAME = "RaportMisiuni.xlsx";

   private static final String DETASAMENT_HEADER = "Detasament";

   private static final String DESCRIERE_HEADER = "Descriere";


   public MissionsXlsCreator() throws IOException, DocumentException
   {
   }


   public void createXls( final List<SubUnit> subUnits )
   {
      try
      {
         recreateReportFile();
         Workbook workbook = buildReportFile( subUnits );
         writeReportFile( workbook );
      }
      catch ( IOException e )
      {
         LOG.error( "Could not create XLS file. ", e );
      }
   }


   private Workbook buildReportFile( final List<SubUnit> subUnits )
   {
      Workbook workbook = new XSSFWorkbook();

      Sheet sheet = workbook.createSheet( "Raport" );

      CellStyle headerStyle = createHeaderStyle( workbook, ( short ) 0 );
      CellStyle cellStyle = createCellStyle( workbook, ( short ) 0 );
      Row header = sheet.createRow( 0 );

      int startRowNumber = 0;
      int endRowNumber = startRowNumber + 2;
      int columnStartIndex = 0;
      int columnEndIndex = 1;
      Cell detasamentHeader = header.createCell( columnStartIndex );
      detasamentHeader.setCellValue( DETASAMENT_HEADER );
      detasamentHeader.setCellStyle( headerStyle );
      CellRangeAddress detasamentHeaderRegion = new CellRangeAddress( startRowNumber, endRowNumber,
            columnStartIndex, columnEndIndex );
      sheet.addMergedRegion( detasamentHeaderRegion );

      columnStartIndex = columnEndIndex + 1;
      columnEndIndex = columnStartIndex + 5;
      Cell descriereHeader = header.createCell( columnStartIndex );
      descriereHeader.setCellValue( DESCRIERE_HEADER );
      descriereHeader.setCellStyle( headerStyle );
      CellRangeAddress descriereHeaderRegion = new CellRangeAddress( startRowNumber, endRowNumber, columnStartIndex,
            columnEndIndex );
      sheet.addMergedRegion( descriereHeaderRegion );

      startRowNumber = endRowNumber + 1;
      final AtomicInteger startIndex = new AtomicInteger( startRowNumber );

      subUnits.forEach( subUnit ->
      {
         Row contentRow = sheet.createRow( startIndex.getAndAdd( 3 ) );
         Cell subUnitNameCell = contentRow.createCell( 0 );
         subUnitNameCell.setCellValue( subUnit.getName() );
         subUnitNameCell.setCellStyle( headerStyle );
         CellRangeAddress region = new CellRangeAddress( contentRow.getRowNum(), contentRow.getRowNum() + 2, 0, 1 );
         sheet.addMergedRegion( region );

         final Cell emptyCell = contentRow.createCell( 1 );
         emptyCell.setCellStyle( cellStyle );
         CellRangeAddress emptyCellRegion = new CellRangeAddress( contentRow.getRowNum(), contentRow.getRowNum() + 2, 2,
               7 );
         sheet.addMergedRegion( emptyCellRegion );

      } );

      return workbook;
   }


   private CellStyle createHeaderStyle( Workbook workbook, short rotation )
   {
      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillForegroundColor( IndexedColors.GREY_25_PERCENT.getIndex() );
      headerStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
      XSSFFont font = (( XSSFWorkbook ) workbook).createFont();
      font.setFontName( "Arial" );
      font.setFontHeightInPoints( ( short ) 12 );
      font.setBold( true );
      headerStyle.setFont( font );
      headerStyle.setRotation( rotation );
      headerStyle.setWrapText( false );
      headerStyle.setShrinkToFit( true );
      headerStyle.setBorderTop( BorderStyle.MEDIUM );
      headerStyle.setBorderBottom( BorderStyle.MEDIUM );
      headerStyle.setBorderLeft( BorderStyle.MEDIUM );
      headerStyle.setBorderRight( BorderStyle.MEDIUM );
      return headerStyle;
   }


   private CellStyle createCellStyle( Workbook workbook, short rotation )
   {
      CellStyle cellStyle = workbook.createCellStyle();
      XSSFFont font = (( XSSFWorkbook ) workbook).createFont();
      font.setFontName( "Arial" );
      font.setFontHeightInPoints( ( short ) 12 );
      font.setBold( false );
      cellStyle.setFont( font );
      cellStyle.setRotation( rotation );
      cellStyle.setWrapText( false );
      cellStyle.setBorderTop( BorderStyle.MEDIUM );
      cellStyle.setBorderBottom( BorderStyle.MEDIUM );
      cellStyle.setBorderLeft( BorderStyle.MEDIUM );
      cellStyle.setBorderRight( BorderStyle.MEDIUM );
      return cellStyle;
   }


   private void writeReportFile( final Workbook workbook ) throws IOException
   {
      FileOutputStream outputStream = new FileOutputStream( MISSIONS_REPORT_FILE_NAME );
      workbook.write( outputStream );
      workbook.close();
   }


   private void recreateReportFile() throws IOException
   {
      File f = new File( MISSIONS_REPORT_FILE_NAME );
      if ( !f.exists() )
      {
         f.createNewFile();
      }
      else
      {
         f.delete();
         f.createNewFile();
      }
   }
}
