# Leap REST API Helper

This utility provides a higher level api to interact with the Leap REST api with Java.

The example code is provided as a working example of how to connect to and interact with the Leap REST API.  The onus is left to the reader to insure that any code used in production is properly tested and modified to include proper error handling.

## Setup Steps

1. Download the Java code.  Create a java application with in your favorite Java editor (i.e. Rational Application Developer, Eclipse, etc)

SampleMain.java

LeapAPIHelper.jar

2. Download the Sample FEB Application.  The application was built in 8.6.4.1 and cannot be imported to an older FEB version.  When you import the application, choose to import with data, it contains a sample record that will be used in the example below.

RESTAPIExample.nitro_s

3. Download the sample input and output files for each REST API URL.

RestApi_InputFiles.zip

## How to Use the API

1. Establish a connection to the URL (either secure or regular)

```
URL url = new URL(theUrl); HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
```

2. Set connection properties (headers, authorization, method, etc)

```
secureUrlConnection.setSSLSocketFactory(getSSLContext(SSLProtocol).getSocketFactory());                
secureUrlConnection.setHostnameVerifier(getHostNameVerifier());
secureUrlConnection.setRequestMethod(method);                        
secureUrlConnection.setRequestProperty("Accept", returnFormat);
secureUrlConnection.setRequestProperty("Content-Type", returnFormat);
secureUrlConnection.setRequestProperty("Authorization", "Basic " + getEncodedString(user, pwd));
```

3. (optional) Write content to be sent along with the URL request

4. Do something with the response from the request

5. Close the connection


To simplify the code and boost understanding, I write all the REST API responses to temporary files and I also read in the posted content from temporary files as well.  In each example that I go through I:

1. Establish an instance of the LeapHelperImpl class that contains the helper functions.

```
LeapHelperImpl apiHelper = new LeapHelperImpl("https://leapServer/apps-basic", usr, pwd, "12345");
```

2. I define the location of the file that will contain the response from calling the REST URL.

```
f = "c:\\temp\\LEAPREST_RETRIEVE_OUT.txt";
```

3. Call the function desired function, passing it the critical information (i.e. app id, form id, record id, etc).

```
LeapHelperResponse lhr = apiHelper.retrieveRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943", "application/
```

4. I print out the HTTP response code and response message.
  
```
System.out.println(lhr.responseCode);
```

5. I print the response to a file.
  
```
apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
```

### Retrieve a Single Record

This REST URL will retrieve all the data for a specific form submission.  The IDs supplied are the ones for the provided sample application and it will return a valid record.

```
f = "c:\\temp\\LEAPREST_RETRIEVE_OUT.txt";
REST_URL = "http://localhost:9081/forms-basic/secure/org/data/03739889-76ba-4231-825d-74831e521c45/F_Form1/d1a119ab-0c63-4648-8bbc-4b5dd9360943";
LeapHelperResponse lhr = apiHelper.retrieveRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943", LeapHelperReturnFormat.JSON);
System.out.println(lhr.responseCode);
      
try {
  apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
} catch (IOException e) {

  e.printStackTrace();
}
```

For you to test this code against your own local FEB you would have to modify a few things:

1. The server host name and port (optional)

2. The APP ID of the app that you want to query.  You can find the app id in the details of your application on the Manage page.

3. The Form ID, this is the ID of the form that you are querying

4. The Record ID, this is the ID of the specific record that you want to return

5. Specify your user and pwd that will be used to make the REST request

6. Modify the location of where the response file will be created.


### List all the Records for a Form

This REST URL will return all the submitted records for a specific form within an application.

```
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
```

Once again you will have to update the same items as above.

### Create a Record

This REST URL will create a form submission for a specific Form.

```
f = "c:\\temp\\LEAPREST_CREATE_OUT.txt";
createContent = "c:\\temp\\LEAPREST_CREATE_IN.txt";
LeapHelperResponse lhr = apiHelper.submitRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", jsonData);
System.out.println(lhr.responseCode);

try {
    apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
} catch (IOException e) {
    e.printStackTrace();
}
```

Once again you will have to update the same items as above.  If you check the output of the create you will see the JSON object (or XML if you changed the return format) for that newly created record.  This will become important in the next REST URL section.

### Update a Record

This REST URL will update a single form submission.

```
f = "c:\\temp\\LEAPREST_UPDATE_OUT.txt";
createContent = "c:\\temp\\LEAPREST_UPDATE_IN.txt";
LeapHelperResponse lhr = apiHelper.updateRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943", jsonData);
System.out.println(lhr.responseCode);
 
try {
    apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
} catch (IOException e) {
    e.printStackTrace();
}
```

Now with update, we see that I have added something new to the URL, "freedomIdentifyKey=1".  This is an added security measure that by default in 8.6.4 is required for ALL REST API queries.  The last parameter of the function is also passing the same "1" value, if you change what you pass in the URL then you will have to change the parameter as well.

The extra addition to this request is content that needs to be posted to the server along with the request.  When you are updating a record you need to send the XML or JSON that represents the updates to a record.  When you are updating a record you must:

1. Provide data for all required fields

2. Specify the uid

3. Specify the stage that the record is currently in (flowState)

4. Specify the ID of the button that is being pressed (pressedButton)

A file has been provided (LEAPREST_UPDATE_IN.txt) that can be used to update the record in the sample FEB application.

### Delete a Record

This REST URL will delete a single form submission.

```
f = "c:\\temp\\LEAPREST_DELETE_OUT.txt";
        
LeapHelperResponse lhr = apiHelper.deleteRecord("03739889-76ba-4231-825d-74831e521c45", "F_Form1", "d1a119ab-0c63-4648-8bbc-4b5dd9360943");
 
System.out.println(lhr.responseCode);
try {
    apiHelper.printStreamToFile(new StringBufferInputStream(lhr.responseJSON.toString()), f);
} catch (IOException e) {
    e.printStackTrace();
}
```

We have now gone through all the operations and you should have a pretty good idea of how to now interact with the FEB REST API from Java code.  Now a new wide world is open to you as you want to integrate FEB with existing systems or further enhance your applications with custom code.

One example I can think of is a routine that pulls FEB records, checks how long they have been in a certain stage and then sends a notification to the owner (next actor) if longer then desired.  There are so many cool things that you can do once you know how to interact with the FEB API.
