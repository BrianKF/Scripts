package myPackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class newRequest {

	public void doGet(
	        HttpServletRequest request,
	        HttpServletResponse response
	) {
	     
	    try {
	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.println("{");
	        out.println("\"First Name\": \"Devesh\",");
	        out.println("\"Last Name\": \"Sharma\"");
	        out.println("}");
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}