# Custom Transport - Echo Value

This transport was created as a basic example of how to create a custom transport. The transport will return any value that it is given.

The transport is very simple, it takes one input (the field value that contains the id of the next user) and it has one output (the exact value that was passed as an input).

## Components

This sample was built using a version of Leap before 9.x, you can import this file into your HCL Leap 9.x server.  Included are:

- [**EchoValueTransport.jar**](EchoValueTransport.jar):  this file must be placed in the configured extensions directory (.../HCL/Leap/extensions),
- [**EchoValue.xml**](EchoValue.xml):  this file must be place in  .../HCL/Leap/serviceCatalog/1 directory, and,
- [**EchoValueTransport.nitro_s**](EchoValueTransport.nitro_s): a sample application that can be imported into your Leap server for testing.

## Sample Use Case

One of the ways that this transport can be used is in conjunction with the Assign Users (By Service Call) submit activity.  Before Leap 9.3, you could not refer to a field on a form as an input to this service.  The workaround was to use a custom service like this echo value service.

Even with this custom transport you should still insure that the value was validated before the form gets submitted.  If the user is entering a name, email, or uid then you may want to validate it by using an LDAP service, that way if the value was entered incorrectly it can be corrected before the form is submitted.