# Interact with External Database

## Description

This transport provides a mechanism for a Leap application to interact directly with an external database.   I tried to build my transport so that the user would not need to know the SQL syntax being generated, they just need to have a basic idea of what is needed.  It is expected that you would create an XML file for each database/table that you want to interact with.  Included in this article is a Leap application that demonstrates the functionality, a sample service description XML file and the custom extension (.jar file).

There are many different approaches that one can take when designing a custom extension, the end result usually depends greatly on how it will be used - there is not always a "one-size fits all" solution especially when it comes to communicating with external databases.  Creating SQL queries are a science all unto themselves and the functionality that I have provided is limited to very basic queries (I have not even attempted nested queries!).  Some examples of supported queries are:

```
SELECT * FROM PEOPLE
SELECT FIRSTNAME, LASTNAME FROM PEOPLE WHERE LASTNAME = 'Dawes' ORDER BY LASTNAME
SELECT FIRSTNAME, LASTNAME FROM PEOPLE WHERE LASTNAME LIKE '%Daw%'
SELECT FIRSTNAME, LASTNAME FROM PEOPLE WHERE LASTNAME STARTSWITH '%Daw%'
DELETE FROM PEOPLE WHERE LASTNAME = 'Dawes'
INSERT INTO PEOPLE (FIRSTNAME, LASTNAME) VALUES ('Chris', 'Dawes')
UPDATE PEOPLE SET FIRSTNAME='Christopher' WHERE FIRSTNAME = 'Chris' AND LASTNAME = 'Dawes'
SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY lastname) AS rn, People.* FROM People) AS tbl WHERE rn BETWEEN 1 AND 25 ORDER BY lastname
SELECT * FROM PEOPLE WHERE FIRSTNAME IN ('Chris','Marty','Ken');
```

Hopefully from this list you can start to see how this extension can be used.  If you need to perform more complex queries it is possible but it would require the transport to be modified.

Every time I review the code for this transport I see other ways that this could have been implemented, but again it all depends on what you are trying to achieve.  Although I have sought to provide lots of different functionality within this transport you may find that in your own implementation you would prefer for a more direct approach or you may find that your needs are completely different.  I would love to hear your feedback on this extension and why it works or doesn't work for your needs.  I also hope that this is a helpful educational tool as you embark on building custom transports to integrate Leap with your back-end systems.

## Revision History

v1.7 - Sept 2018

Now supports queries using WHERE IN

v1.6 - May 11 2017

Queries are now case insensitive

v1.5 - Apr 27 2016

Added row count as output parameter for select queries
Added orderBy and orderByOp to sort SELECT query
Added SELECT query pagination using ROW_NUMBER()
Added whereOp_<column>, enables wild card searches when using the WHERE clause

v1.4 - Apr 1 2016

Params are now case-insensitive
Added more debug logging

v1.3 - May 8 2015

Any Where parameters that have values will be automatically added to the outbound parameter map.  This means that you can have individual outbound parameters that match the same name as the where parameter and they will be exported outside of the result set.  This is helpful if you want to return the values that where sent in with the query. 
Enabled the transport to use WAS DataSource.

v1.2 - April16 2014

Jar now contains DB2 and oracle drivers. Made it so that port is not required and is only added to the db url if it is not empty.

v1.1 - April 9 2014

Jar now contains the MsSQL driver, sqljdbc4.jar.  Tested with ODBC, MsSQL and DB2

v1.0 - April 8 2014

Initial deployment

## Extension Files

**DatabaseTransport.jar**

I have included **5 different service description XML files**.  There is a generic service that enables the form designer to perform all the database options, then there is a service configuration file for each of the individual services; select, insert, update and delete.  It demonstrates which parameters are applicable for each service.

 **ServiceCatalogSource** - I have also included an example service catalog which would be a way to group all the services together, rather then them all appearing under the General category.  This catalog will not be usable as-is since it is specific to my local environment.  I am providing the source files so that you could import it directly into your development IDE. Once you import the project, you can update the contained XML files and then create the JAR and deploy it to your FEB.

## Extension Variables

### Input

**dbName**

The name of the WAS dataSource registered in Resources...JDB...Data Sources.  i.e. jdbc/SampleDB

*Deprecated* If dbType = ODBC then this should be the name of the configured DSN.
	
**dbTable**

The name of the table.

**action**

The type of SQL query to be executed. Valid values are insert, select, update, delete.

*Disclaimer* - Be careful with delete and update operations.  If you do not properly supply a valid where clause then you could update or delete ALL the rows of the table you are accessing.

**dbType**

Valid Value is DataSource. 

*Deprecated* the previous values, since the individual database drivers are no longer included in the jar file: Oracle, DB2, MySQL, ODBC, MsSQL

**dbServer**

The host name of the database server.

**dbPort**

The port of the database server. (i.e. 50000)

**WAS-J2CAlias**

The alias for the user that should be used to connect to the database. Note: You can configure this in the WAS Admin Console:

    - Security...Global Security
    - Java Authentication...J2C Authentication Data...
    - New...Enter the Alias, username and password
	
**assign_<columnName>**

    Defines the columns that will be used in the assignment clause of applicable SQL queries. For example, creating the following parameters assign_name, assign_age would result in :

    Select name, age from person

    If you don't supply any then * will be used for the query:

    ```Select * from person```

**where_<columnName>**

Defines the columns that will be used in the where clause of applicable SQL queries. For example, creating the following parameters where_name would result in:

```Select * where name = <value>```

!!!note
Any parameter that has a value will automatically be added to the transport outbound parameter list.  So if you want to return the values of the where parameters as single items outside of the result set then add individual outbound parameters to your outbound mapping. (Added in v 1.3)

**whereOp_<columnName>**

Valid values are LIKE (Added in v1.5), STARTSWITH, and IN (Added in v1.7).  Only specify this for the column name if you want to use wild card (LIKE '%x%') instead of an equality (=) search.

**whereOperator**

This determines the relationship between multiple where clauses. There can only be one relationship. Valid values are AND and OR

**orderBy**

The column name to use to sort the results (SELECT only).  Required if paginating the results. (Added in v1.5)

**orderByOp**

Valid values are ASC or DESC. Optional, defaults to ASC. (Added in v1.5)

**pageNum**

Paginates the query results.  Page number.  Pagination may not work with all databases (I have only tested it against DB2), it is using the SQL query like:

```SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY lastname) AS rn, People.* FROM People) WHERE rn BETWEEN 1 AND 2 ORDER BY lastname.```
(Added in v1.5)

**itemsPerPage**

Paginates the query results. Specify Items per page. (Added in v1.5)

**includeBlankValues**

Valid values are true or false, default is false.  All of the "assign_" parameters will be used to build the query even if they are blank. This parameter is critical to a select action where you defined all the "assign_" parameters that you want returned in the query but none of them are actually being used as inputs to the service (where the fields contain values).

**includeWhereInOut**

Valid values are true or false, default is false. If true, the parameters that are defined as "where" parameters will automatically be included in the service results.

### Output

**<columnName>**

Each column of the result set will be provided (in uppercase) as an output parameter.

**success**

Returns true if the query is processed without error, otherwise false.

**rowsAffected**

The number of rows affected by the INSERT, DELETE or UPDATE query. For SELECT will contain the number of rows returned.

**queryResults**

The results of the SELECT query. This is a list object where each row contains the columns that were defined by the assignment portion of the query.  The returned parameters are in upper case, so your mapping might look something like:

```xml
<mapping source="transport:queryResults" target="parameter:queryResults">
      <mapping source="transport:FIRSTNAME" target="parameter:firstname" />
      <mapping source="transport:LASTNAME" target="parameter:lastname" />
      <mapping source="transport:EMAIL" target="parameter:email" />
</mapping>
```
**state**

The SQL state, only returned if the SQL query fails.

**message**

The SQL message, only returned if the SQL query fails.

**error**

The SQL error code, only returned if the SQL query fails.

## Installation Notes

1. Copy the jar file into the extensions directory (/opt/HCL/Leap/extensions or c:\HCL\Leap\extensions or where you have defined it).  You may have to change the ownership and permissions so that the server can access/execute the file.

2. Modify the xml file (create additional ones) for the database you want to connect to.

- Modify the xml id and service name (replace tableName with the name of the table you are connecting to) :
    ```
    <id>DatabaseTransport-Sample-tableName</id>
    <defaultLocale>en-us</defaultLocale>
    <transportId>com.ibm.support.examples.services.DatabaseTransport.id</transportId>
    <name xml:lang="en-us">DatabaseTransport - tableName</name>
    ```
- Add the columns that can be searched (returned)
- Add any columns that can be used in "where" clauses
- Modify the DB specific settings: type, name, table name, server, port, access
3. Copy the xml file(s) into the ServiceCatalog\1 directory

4. After about 1 minute the services should appear in your FEB application, restart is not required.

5. Create a DataSource for your database within WebSphere Administration Console

- Log in to the WAS Admin console.
- Navigate to Resources...JDBC...Data sources.
- Set the scope by selecting from the dropdown and click New.
- Enter the name for the data source (i.e. My DB2 database)
- Enter the JNDI name (i.e. jdbc/SampleDB) and click Next.
- If you have an existing JDBC provider for the database you are connecting to then select it, otherwise select Create new JDBC provider and click Next.
- Select the Database Type, Provider Type, Implementation Type, provide it a name and click Next.
- Specify the location on your server for the database driver (typically a jar file) and click Next.
- Provide the database name, server name, port and driver type. Click Next.
- Click Next (we will create the alias in the next step).
- Click Finish,
- Click Save to apply changes to server.

6. Create J2C Authentication Alias for the user that will be used to connect to the datasource.

- Click the link for the newly created datasource.
- Click JAAS - J2c authentication data (on the right side of the page)
- Click New.
- Provide the Alias, User ID, Password and click OK.
- Click Save to apply changes to server.
- Click the link at the top of the page to go back to the main page for your datasource.  Under Security settings select the alias that you just created in the Component-managed authentication alias.  Click OK.
- Click Save to apply changes to server.
- Click the check next to the datasource and click Test Connection to verify that you can connect to the database.

7. For troubleshooting, add the trace string com.ibm.support.examples.*=finest to the WebSphere instance.

### Using in your Leap Application

1. Using Select.

Create a service description and find the Database Transport - tableName
Define the columns that you want to be returned by the query. If you do not link any "assign" inputs then "*" will be used in the query.

<mapping target="transport:assign_firstname" source="constant:colSelect"/>
<mapping target="transport:assign_lastname" source="constant:colSelect"/>
<mapping target="transport:assign_email" source="constant:colSelect"/>

!!!note 
  In this example we map the source to a constant of "1" which is one way of making sure the parameters are used in the query.  The other approach is to use the new parameter called "includeBlankValues", which will force all the "assign_" parameters to be recognized when the SQL query is constructed.

Link any "where" inputs that you might want to use:

```
<mapping target="transport:where_lastname" source="parameter:where_lastname"/>
<mapping target="transport:whereOp_lastname" source="parameter:whereOp_lastname"/>
<mapping target="transport:where_firstname" source="parameter:where_firstname"/>
<mapping target="transport:whereOp_firstname" source="parameter:whereOp_firstname"/>
```
- The service returns a list, link the outputs to a list object (dropdown, select one/many or table)
- Setup how the service will be called (i.e. on button click, on form load, on value change of an item, etc)
- The service returns the query results and true/false

2. Using Insert

- Create a service description and find the Database Transport - tableName
- The "assign" inputs must be provided as they will determine the columns/values that are updated and they should link to fields on your form
"where" inputs are not applicable to insert
- The service returns the number of rows affected and true/false

3. Using Update

- Create a service description and find the Database Transport - tableName
- The "assign" inputs must be provided as they will determine the columns that are updated and they should link to fields on your form
"where" inputs may be used as it limits which records are updated from the table
- The service returns the number of rows affected and true/false

4. Using Delete

- Create a service description and find the Database Transport - tableName
- The "assign" inputs are not applicable
"where" inputs should be used as it limits what is deleted from the table
- The service returns the number of rows affected and true/false

## Code Review

This section is dedicated to showing certain features that you might want to incorporate into your own transports.

1. How to retrieve WAS J2C Alias     

```java
import com.ibm.wsspi.security.auth.callback.Constants;
import com.ibm.wsspi.security.auth.callback.WSMappingCallbackHandlerFactory;

import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
. . .

String j2cAlias = (String) pParameters.get("WAS-J2CAlias");

//look up J2C Alias from WebSphere to get DB credentials
if(j2cAlias != null && !j2cAlias.equalsIgnoreCase("")) {
    sLog.log(Level.FINEST, "FOUND J2C ALIAS: " + j2cAlias );
    try {
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constants.MAPPING_ALIAS, j2cAlias);
          
        CallbackHandler cbh = WSMappingCallbackHandlerFactory.getInstance().getCallbackHandler(map, null);
        LoginContext lc = new LoginContext("DefaultPrincipalMapping", cbh);
        lc.login();        
      
        Subject sub = lc.getSubject();
        Set<Object> creds = sub.getPrivateCredentials();
          
        PasswordCredential passwordCred = (PasswordCredential) creds.iterator().next();
        dbUser = passwordCred.getUserName();
        dbPassword = new String(passwordCred.getPassword());
    } catch (LoginException le) {
        sLog.log(Level.WARNING, "ERROR: The defined alias does not exist. (" + j2cAlias + ")");
        sLog.log(Level.WARNING, "ERROR: " + le.getMessage());
    } catch (Exception e) {
        sLog.log(Level.WARNING, "ERROR: " + e.getMessage());
        e.printStackTrace();
    }
} 
```

2. How to access a WAS datasource           

```java
import javax.sql.DataSource; 
import javax.naming.InitialContext;

. . .

if(dbType.equalsIgnoreCase("DataSource")) {
    sLog.log(Level.FINEST, "ESTABLISHING DB CONNECTION VIA DATASOURCE: " + dbName) ;
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(dbName);
    conn = ds.getConnection();
}
```

3. How to log within a Transport (log messages will be written to trace.log, when enabled)

```java
import java.util.logging.Level;
import java.util.logging.Logger;

. . .

private static Logger sLog = Logger.getLogger(DatabaseTransport.class.getName());

sLog.log(Level.FINEST, "DEBUG:");
```