# Manage Utility for Leap Applications

This is a utility for managing the movement of Leap applications.  It can be used to:
- import or export applications for archival purposes
- migrate an application from one server to another (i.e. dev to prod)
 
The filename of exported applications will be the application id.
 
After running the application it will create a debug.log that contains a listing of all the exported applications and their titles
as seen in the Manager panel.

## Flags

|Flag|Description|
|-o|The operation to perform, it can be exportOneToFS, exportListToFS, exportAllToFS, import, delete, upgrade and migrateApp|
|-ps|The primary FEB server.  For example, http://myserver.server.com/, http://myserver.server.com/, http://myserver.server.com:9081|
|-u|The username to use when connecting to FEB|
|-p|The password for the specified user|
|-ignoreSSL|Flag to bypass SSL when communicating with the primary server. If used then the commType (-ct) is required.|
|-ct|The communication type (i.e. SSL, TLS, SSL_TLS, TLSv1.2, etc).  Only used when server is HTTPS and Defaults to SSL.|
|-c|The context of the forms REST API, defaults to "forms-basic".|
|-ss|The secondary FEB server. For example, http://myserver.server.com/, http://myserver.server.com/, http://myserver.server.com:9081/|
|-ss_u|The username to use when connecting to FEB|
|-ss_p|The password for the specified user|
|-ss_ignoreSSL|Flag to bypass SSL when communicating with the primary server. If used then the commType (-ct) is required.|
|-ss_ct|The communication type (i.e. SSL, TLS, SSL_TLS, TLSv1.2, etc) for secondary server is specified.|
|-ss_c|The context of the forms REST API, defaults to "forms-basic".|
|-f|The path where the files will be exported to or imported from.  This will search all sub directories.  MUST Use forward slashes in the URL.  Supports relative file paths.|
|-a|The application ID to be exported (only used with exportOneToFS and upgrade operation)|
|-t|The tags to apply to the application being imported. Comma separated list of tags.|
|-listPath|The path of the file that contains the list of app ids, the expected format is a simple text file with one application id per line. Used with exportListToFS or delete.|
|-fik|The cookie value to use for the freedomIdentifyKey|
|-deploy|This will force the deployment of the imported application|
|-data|This will include the submission data when importing, exporting or updating|
|-page|The page number to start with, only used for exportAllToFS.|
|-debug|This will enable additional debugging for the application|
|-console|Will ask for all the configuration information in the console rather than passing as command args.|
|-usage or -help|Shows the help message.|

## Usage

This application should be accessed from the cmd line.  To call the application:

1. Open a cmd console.  From Windows...Start...Run and enter "cmd" and click "OK"
2. Adjust the following cmd to access your installed java.exe:

    ```"c:\Program Files (x86)\Java\jre7\bin\java.exe" -jar ManageFEBApps.jar```


3. Add the additional flags that you require.

A .bat script has been provided that can be used to run the application.  Modify the .bat script with a text editor
and then you can simply double-click the file to run it.

## Examples

To EXPORT ONE app from a server:

```
-o exportOneToFS -ps <serverURL> -u <user> -p <pwd> -a <appID> -f C:/temp/FEBExport
 
-o exportOneToFS -ps <serverURL> -u <user> -p <pwd> -a <appID> -f "C:/temp/FEB Export"
```

To EXPORT LIST of apps from a server:

```
-o exportOneToFS -ps <serverURL> -u <user> -p <pwd> -listPath <filePath> -f C:/temp/FEBExport
```

To IMPORT ONE application (no deploy):

```
-o import -ps <serverURL> -u <user> -p <pwd> -f C:/temp/FEBExport/<filename>
```

To IMPORT ONE application (auto deploy and include data):

```
-o import -ps <serverURL> -u <user> -p <pwd> -f C:/temp/FEBExport/<filename> -deploy -data
 
-o import -ps <serverURL> -u <user> -p <pwd> -f <filename> -deploy -data
```
 
To IMPORT ALL applications from a directory (auto deploy and include data for all apps in directory):

```
-o import -ps <serverURL> -u <user> -p <pwd> -f C:/temp/FEBExport -deploy -data
 
-o import -ps <serverURL> -u <user> -p <pwd> -f "FEB Export Dir" -deploy -data
```

To UPGRADE an application (from the filesystem):

```
-o upgrade -ps <serverURL> -u <user> -p <pwd> -a <appID> -f "C:/temp/sample.nitro_s"  -debug
```

To DELETE an application from FEB:

```
-o delete -ps <serverURL> -u <user> -p <pwd> -a <appID> -debug  -ct TLSv1.2
```

To DELETE list of applications from FEB:

```
-o delete -ps <serverURL> -u <user> -p <pwd> -listPath <filePath> -debug  -ct TLSv1.2
```
 
To MIGRATE application from one server to another:

```
-o migrateApp -ps <serverURL> -ss <secondServerURL> -u <user> -p <pwd> -a <appID> -f C:/temp/FEBExport -deploy -data
 
-o migrateApp -ps <serverURL> -ss <secondServerURL> -u <user> -p <pwd> -ss_u <secuser> -ss_p <secpwd> -a <appID> -f C:/temp/FEBExport -deploy -data -tags "sample,dawes"
```