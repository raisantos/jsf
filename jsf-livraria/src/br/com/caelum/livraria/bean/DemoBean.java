package br.com.caelum.livraria.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.dao.JPAUtil;
import br.com.caelum.livraria.modelo.Livro;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class DemoBean {
	
	public void geraPdf() throws Exception {

		System.out.println("Passou1--------------------");
		EntityManager em = new JPAUtil().getEntityManager();
		
		Query query = em.createQuery("select l from Livro l");
		@SuppressWarnings("unchecked")
		List<Livro> listOfLivros = (List<Livro>) query.getResultList();
		
		System.out.println("Passou2--------------------");
		
		//FacesContext facesContext = FacesContext.getCurrentInstance();
		JRBeanCollectionDataSource jrb = new JRBeanCollectionDataSource(listOfLivros);
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/reports.jasper");
		
		System.out.println("Passou3--------------------");
		System.out.println(reportPath);
		
		try {
			if (listOfLivros != null && listOfLivros.size() > 0) {
				System.out.println("Montando Relatório--------------------");				
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap<String, Object>(), jrb);
				HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
				ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
				FacesContext.getCurrentInstance().responseComplete();
			} else {
				 throw new Exception("Não existem registros a serem exibidos!");
			}
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
		
		System.out.println("Download do Relatório Realizado--------------------");
	}
	
	public void geraExcel() {
		
		List<Livro> livros = new DAO<Livro>(Livro.class).listaLivros();
		
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("livros");
		
		sheet1.setColumnWidth(0, 5000);
		sheet1.setColumnWidth(1, 6000);
		sheet1.setColumnWidth(2, 5000);
		sheet1.setColumnWidth(3, 5000);
		sheet1.setColumnWidth(4, 6000);
			
		Row row1 = sheet1.createRow(0);
			
		row1.setHeightInPoints(20);
			
		Cell cell0 = row1.createCell(0);
		Cell cell1 = row1.createCell(1);
		Cell cell2 = row1.createCell(2);
		Cell cell3 = row1.createCell(3);
		Cell cell4 = row1.createCell(4);
		
		cell0.setCellValue("ID");
		cell1.setCellValue("DATA DE LANÇAMENTO");
		cell2.setCellValue("ISBN");
		cell3.setCellValue("PREÇO");
		cell4.setCellValue("TÍTULO");
			
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			
		Font font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		style.setFont(font);
			
		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
			
		CellStyle cs = workbook.createCellStyle();
		DataFormat df = workbook.createDataFormat();
		cs.setDataFormat(df.getFormat("#,##0.00"));

		CellStyle cs1 = workbook.createCellStyle();
		DataFormat df1 = workbook.createDataFormat();
		cs1.setDataFormat(df1.getFormat("dd/MM/yyyy"));
		
		int index = 0;		
		for(Livro livro: livros){
			index = index + 1;
			Row row = sheet1.createRow(index);
				
			row.createCell(0).setCellValue(livro.getId());
			
			Cell data = row.createCell(1);
			data.setCellStyle(cs1);
			data.setCellValue(livro.getDataDeLancamento().getTime());
			
			row.createCell(2).setCellValue(livro.getIsbn());

			Cell preco = row.createCell(3);
			preco.setCellStyle(cs);
			preco.setCellValue(livro.getPreco());
			
			row.createCell(4).setCellValue(livro.getTitulo());
		}
		DemoBean.downloadWorkbook(workbook, "livros.xls");
	}
	
	public static void downloadWorkbook(Workbook workbook, String fileName) {

		SimpleDateFormat formatDateFile = new SimpleDateFormat("yyyyMMddHHmmss");

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest servletContext = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		String path = servletContext.getRealPath("/reports/") + formatDateFile.format(new Date()) + ".xls";

		File file = new File(path);

		try {
			FileOutputStream output = new FileOutputStream(file);
			workbook.write(output);
			output.close();
			
			DemoBean.writeBytesAsAttachedTextFile(Files.readAllBytes(Paths.get(path)), fileName);

			file.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeBytesAsAttachedTextFile(byte[] bytes, String fileName) throws Exception {
		if (bytes == null)
			throw new Exception("Array de bytes nulo.");
		
		if (fileName == null)
			throw new Exception("Nome do arquivo é nulo.");

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();
	}
}