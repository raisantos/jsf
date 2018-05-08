package br.com.caelum.livraria.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.livraria.dao.JPAUtil;
import br.com.caelum.livraria.modelo.Livro;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class ReportBean {

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
	public void geraPdf() throws JRException {
		
		System.out.println("Passou1--------------------");
		EntityManager em = new JPAUtil().getEntityManager();
		Query query = em.createQuery("select l from Livro l");
		@SuppressWarnings("unchecked")
		List<Livro> listOfLivros = (List<Livro>) query.getResultList();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		HttpServletRequest servletContext = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		String pathRel = servletContext.getRealPath("/reports/reports.jasper");
				
		//HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		// parametros
		Map<String, Object> hashMap = new HashMap<String, Object>();
		/*Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("protocolo",recibo.getProtocolo());
		parametros.put("dataRecebimento", formatDate.format(protocolo.getDataProtocolo()) );
		parametros.put("orgao",recibo.getOrgao());
		parametros.put("processo",recibo.getProcesso());
		parametros.put("responsavel",recibo.getResponsavel());
		parametros.put("logo",pathLogo);*/

		// recebe por parametro uma lista da propia classe ja populada
		JRDataSource jrds = new JRBeanCollectionDataSource(listOfLivros);

		try {
			if (listOfLivros != null && listOfLivros.size() > 0) {
				JasperPrint print = JasperFillManager.fillReport(pathRel,
						hashMap, jrds);
				byte[] bytes = JasperExportManager.exportReportToPdf(print);
				writeBytesAsAttachedTextFile(bytes, "recibos.pdf");
			} else {
				 throw new Exception("Não existem recibos ");
			}
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
		System.out.println("finalizou--------------------");

		//return null;

	}

}
