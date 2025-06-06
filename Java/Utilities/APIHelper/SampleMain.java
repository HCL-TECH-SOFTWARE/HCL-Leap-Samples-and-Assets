package com.ibm.form.samples;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SampleMain {

/**
 * https://opensource.hcltechsw.com/leap-doc/9.3.9/ref_data_access_rest_api.html
 * @param args
 */
	public static void main(String[] args) {
	
		/* GOLBAL VARS */
		String user = "wasadmin";
		String pwd = "wasadmin";
		String protocol = "TLSv1.2";
		
		InputStream o = null;
		LeapHelperImpl apiHelper = new LeapHelperImpl("https://leapServer/apps-basic", usr, pwd, "12345");
		String createContent = null;
		String f = "";
		String REST_URL = "";		

		//GET SINGLE RECORD
		f = "c:\\temp\\LEAPREST_RETRIEVE_OUT.txt";
		LeapHelperResponse lhr = apiHelper.retrieveRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943", 
		System.out.println(lhr.responseCode);
		
		try {
			apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
//		//LIST ALL RECORDS
		f = "c:\\temp\\LEAPREST_ALL_OUT.txt";
		
		// to filter the responses
		LeapFilters filters = new LeapFilters();
		filters.addFilter(new LeapHelperFilterParam(LeapHelperFilterMetaColumns.STAGE_ID.toString(), LeapHelperFilterOperator.EQUALS, "S_Submitted"));

		LeapHelperResponse lhr = apiHelper.listRecords("03739889-76ba-4231-825d-74831e521c45", "F_Form1", filters, LeapHelperReturnFormat.JSON);
		System.out.println(lhr.responseCode);
		
		try {
			apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
//		//CREATE RECORD
		f = "c:\\temp\\LEAPREST_CREATE_OUT.txt";
		createContent = "c:\\temp\\FEBREST_CREATE_IN.txt";
		
		LeapHelperResponse lhr = apiHelper.submitRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", jsonData);
		System.out.println(lhr.responseCode);
		
		try {
			apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
//		//UPDATE RECORD
		f = "c:\\temp\\LEAPREST_UPDATE_OUT.txt";
		createContent = "c:\\temp\\FEBREST_UPDATE_IN.txt";
		
		LeapHelperResponse lhr = apiHelper.updateRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943", jsonData);
		System.out.println(lhr.responseCode);
		
		try {
			apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		//DELETE RECORD
		f = "c:\\temp\\LEAPREST_DELETE_OUT.txt";
				
		LeapHelperResponse lhr = apiHelper.deleteRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943");
		
		System.out.println(lhr.responseCode);
		try {
			apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
