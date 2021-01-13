package com.enel.ipscan;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdm.namespaces.zscpaidasrv.HR_MNG;
import vdm.namespaces.zscpaidasrv.HR_SIA;
import vdm.namespaces.zscpaidasrv.Hrorg;
import vdm.services.DefaultZSCPAIDASRVService;

import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;


@WebServlet("/Test")
public class TestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
		response.setContentType("application/json");
		
		try{
			List<HR_MNG> hr_MNG = new DefaultZSCPAIDASRVService().getAllHR_MNG()
				.filter(HR_MNG.I_UOSAP.eq("30026815"))
				.filter(HR_MNG.I_LEVEL.eq("10"))
				.select().execute(new ErpConfigContext("oDataSapECC"));
			
			List<HR_SIA> hr_SIA = new DefaultZSCPAIDASRVService().getAllHR_SIA()
				.filter(HR_SIA.I_UOSAP.eq("30026815"))
				.filter(HR_SIA.I_LEVEL.eq("10"))
				.select().execute(new ErpConfigContext("oDataSapECC"));
			
			List<Hrorg> hrorg = new DefaultZSCPAIDASRVService().getAllHrorg()
				.filter(Hrorg.I_USRID.eq(hr_MNG.get(0).getUsrid()))
				.select().execute(new ErpConfigContext("oDataSapECC"));
				
			response.setStatus(200);
			response.getWriter().append("Ok: "+hr_MNG.get(0).getUsrid());
			response.getWriter().append("Ok: "+hrorg.get(0).getEname());
		}catch(ODataException e){
			response.setStatus(500);
			response.getWriter().append("Errore: " + e.getMessage().toString());
		}
	}
}
