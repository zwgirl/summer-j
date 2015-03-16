package org.bark.remoting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class RemotingServiceServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
    private static final JsonBuilderFactory bf = Json.createBuilderFactory(null);
    private static final JsonWriterFactory wf = Json.createWriterFactory(null);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.process(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.process(req, resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse res){
		String rpc = req.getParameter("rpc");
		
        String q = req.getQueryString();
		boolean isStream = q != null && q.equals("stream");
		try (InputStream is = new ByteArrayInputStream(rpc.getBytes()); 
	             JsonReader rdr = Json.createReader(is); JsonWriter writer = isStream
	 	                ? wf.createWriter(res.getOutputStream())
	 	   	                : wf.createWriter(res.getWriter());) {
		
			JsonArray root = rdr.readArray();
			LarkInput li = new LarkInput();
			RemotingModel rm = (RemotingModel) li.readObject(root);
			
			String serviceClassname = rm.getClassName();
			Class<?> serviceClass = Class.forName(serviceClassname);
			
			Injector injector = Guice.createInjector(new RemotingModule());
			Object service = injector.getInstance(serviceClass);
			
			Class<?>[] parClass = new Class[rm.getParameters().length];
			for(int i = 0, length = rm.getParameters().length; i< length; i++){
				Object par = rm.getParameters()[i];
				if(par == null){
					parClass[i] = null;
				} else {
					parClass[i] = rm.getParameters()[0].getClass();
				}
			}
			Method method = serviceClass.getMethod(rm.getMethodName(), parClass);
			Object result = method.invoke(service, rm.parameters);
			
	        res.setStatus(HttpServletResponse.SC_OK);
	        res.setContentType("application/json");
	        res.setCharacterEncoding("UTF-8");
	
	        LarkOutput lo = new LarkOutput();
	        JsonArray array = lo.writeObject1(result);
	        writer.write(array);
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

class RemotingModule implements Module{

	@Override
	public void configure(Binder arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
