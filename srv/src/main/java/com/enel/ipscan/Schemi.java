package com.enel.ipscan;

import java.io.IOException;

import com.enel.ipscan.controller.Controller;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
@WebServlet("/Schemas/*")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "ApiScimExternal_SC" }))
public class Schemi extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");

		Controller controller = new Controller();
		try {
			response.getWriter().write(new Gson().toJson(controller.getSchemaController(request.getPathInfo())));
		} catch (Exception e) {
			response.setStatus(500);
			response.getWriter().append("Errore: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

}
