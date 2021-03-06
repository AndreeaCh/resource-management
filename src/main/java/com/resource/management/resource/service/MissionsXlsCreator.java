package com.resource.management.resource.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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

   private static final String TITLE = "MISIUNI EXECUTATE DE FORȚELE I.S.U. CLUJ ÎN DATA DE ";

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
         final Workbook workbook = buildReportFile( subUnits );
         writeReportFile( workbook );
      }
      catch ( final IOException e )
      {
         LOG.error( "Could not create XLS file. ", e );
      }
   }


   private Workbook buildReportFile( final List<SubUnit> subUnits )
   {
      final Workbook workbook = new XSSFWorkbook();

      final Sheet sheet = workbook.createSheet( "Raport" );

      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd.MM.YYYY" ).withZone( ZoneId.systemDefault() );
      final Row title = sheet.createRow( 0 );
      final Cell titleCell = title.createCell( 3 );
      titleCell.setCellValue( TITLE + formatter.format( Instant.now() ) );
      titleCell.setCellStyle( createTitleStyle( workbook ) );
      sheet.addMergedRegion( new CellRangeAddress( 0, 0, 3, 15 ) );

      final CellStyle headerStyle = createHeaderStyle( workbook, ( short ) 0 );
      final CellStyle cellStyle = createCellStyle( workbook, ( short ) 0 );

      final Row headerRow = sheet.createRow( 1 );

      // add DETASAMENT header
      int startRowNumber = 1;
      final int endRowNumber = startRowNumber + 2;
      int columnStartIndex = 0;
      int columnEndIndex = 1;
      final Cell detasamentHeader = headerRow.createCell( columnStartIndex );
      detasamentHeader.setCellValue( DETASAMENT_HEADER );
      detasamentHeader.setCellStyle( headerStyle );
      final CellRangeAddress detasamentHeaderRegion = new CellRangeAddress( startRowNumber, endRowNumber,
            columnStartIndex, columnEndIndex );
      sheet.addMergedRegion( detasamentHeaderRegion );
      setBoldBorders( sheet, detasamentHeaderRegion );

      // add DESCRIERE header
      columnStartIndex = columnEndIndex + 1;
      columnEndIndex = columnStartIndex + 15;
      final Cell descriereHeader = headerRow.createCell( columnStartIndex );
      descriereHeader.setCellValue( DESCRIERE_HEADER );
      descriereHeader.setCellStyle( headerStyle );
      final CellRangeAddress descriereHeaderRegion = new CellRangeAddress( startRowNumber, endRowNumber, columnStartIndex,
            columnEndIndex );
      sheet.addMergedRegion( descriereHeaderRegion );
      setBoldBorders( sheet, descriereHeaderRegion );

      startRowNumber = endRowNumber + 1;
      final AtomicInteger startIndex = new AtomicInteger( startRowNumber );

      subUnits.forEach( subUnit ->
      {
         final Row contentRow = sheet.createRow( startIndex.getAndAdd( 3 ) );

         // add subunits in first column
         final Cell subUnitNameCell = contentRow.createCell( 0 );
         subUnitNameCell.setCellValue( subUnit.getName() );
         subUnitNameCell.setCellStyle( headerStyle );
         final CellRangeAddress detasamentRegion = new CellRangeAddress( contentRow.getRowNum(), contentRow.getRowNum() + 2,
               0, 1 );
         sheet.addMergedRegion( detasamentRegion );
         setBoldBorders( sheet, detasamentRegion );

         // add empty cells for handwritten text
         final Cell emptyCell = contentRow.createCell( 1 );
         emptyCell.setCellStyle( cellStyle );
         final CellRangeAddress emptyCellRegion = new CellRangeAddress( contentRow.getRowNum(), contentRow.getRowNum() + 2, 2,
               17 );
         sheet.addMergedRegion( emptyCellRegion );
         RegionUtil.setBorderBottom( BorderStyle.MEDIUM, emptyCellRegion, sheet );
         RegionUtil.setBorderRight( BorderStyle.MEDIUM, emptyCellRegion, sheet );

      } );

      return workbook;
   }


   private CellStyle createHeaderStyle( final Workbook workbook, final short rotation )
   {
      final CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillForegroundColor( IndexedColors.GREY_25_PERCENT.getIndex() );
      headerStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
      final XSSFFont font = (( XSSFWorkbook ) workbook).createFont();
      font.setFontName( "Arial" );
      font.setFontHeightInPoints( ( short ) 12 );
      font.setBold( true );
      headerStyle.setFont( font );
      headerStyle.setRotation( rotation );
      headerStyle.setWrapText( false );
      headerStyle.setShrinkToFit( true );
      headerStyle.setAlignment( HorizontalAlignment.CENTER );
      headerStyle.setVerticalAlignment( VerticalAlignment.CENTER );
      headerStyle.setBorderTop( BorderStyle.MEDIUM );
      headerStyle.setBorderBottom( BorderStyle.MEDIUM );
      headerStyle.setBorderLeft( BorderStyle.MEDIUM );
      headerStyle.setBorderRight( BorderStyle.MEDIUM );
      return headerStyle;
   }


   private CellStyle createCellStyle( final Workbook workbook, final short rotation )
   {
      final CellStyle cellStyle = workbook.createCellStyle();
      final XSSFFont font = (( XSSFWorkbook ) workbook).createFont();
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


   private CellStyle createTitleStyle( final Workbook workbook )
   {
      final CellStyle headerStyle = workbook.createCellStyle();
      final XSSFFont font = (( XSSFWorkbook ) workbook).createFont();
      font.setFontName( "Arial" );
      font.setFontHeightInPoints( ( short ) 16 );
      font.setBold( true );
      headerStyle.setFont( font );
      headerStyle.setWrapText( false );
      return headerStyle;
   }


   private void setBoldBorders( final Sheet sheet, final CellRangeAddress region )
   {
      RegionUtil.setBorderBottom( BorderStyle.MEDIUM, region, sheet );
      RegionUtil.setBorderLeft( BorderStyle.MEDIUM, region, sheet );
      RegionUtil.setBorderRight( BorderStyle.MEDIUM, region, sheet );
      RegionUtil.setBorderTop( BorderStyle.MEDIUM, region, sheet );
   }


   private void writeReportFile( final Workbook workbook ) throws IOException
   {
      final FileOutputStream outputStream = new FileOutputStream( MISSIONS_REPORT_FILE_NAME );
      workbook.write( outputStream );
      workbook.close();
   }


   private void recreateReportFile() throws IOException
   {
      final File f = new File( MISSIONS_REPORT_FILE_NAME );
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
