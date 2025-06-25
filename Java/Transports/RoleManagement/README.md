# Role Management Transport and Services

This custom transport and related services enable an application author to dynamically add, look up, or remove users and groups from roles.

The functions are:

**DynamicRoleLookup** - Returns the id's of the user(s) or group(s) for the specified role. The ID is the configured log-in property for FEB. For example if your users log-in with an email address then the id will be the email address.

**DynamicRoleUpdate** - Adds the user(s) or group(s) to the specified role. The function leverages existing components of FEB. It queries the FEB USER table, if the user cannot be found then it queries the configured federated repository and if found creates a record in the USERS table.

*Limitation* - Querying the federated repository for valid groups returns an error. Therefore if the group does not already exist in the GROUPS table then you will be unable to add it to a role.

**UserInRoleLookup** - Returns true if the current user is in the specified role

## Required Common Variables

### Input Parameters

**action**

The function to perform. Valid values are “dynamic.user.group.lookup”, “inrole.lookup”, “dynamic.user.group.update”

**appid**

The id of the application. You can either hard-code it in the service description or you can retrieve this programmatically in your FEB application by using app.getUID();

**formid**

The id of the form. You can either hard-code it in the service description or you can retrieve this programmatically in your FEB application by using form.getId();

**recordid**

The id of the specific form record. You can programmatically retrieve this by using BO.getDataId();

**role**

The name of the role that you want to modify.

### Output Parameters

**errormessage**

The error message if one occurs.

**success**

Boolean value indicating the function was successful or not.


## DynamicRoleLookup Variables

### Input Parameters	

**type**

The role type.  Valid values are "open" or "closed", defaults to "open".   Values are case-insensitive in v1.3.
Output Parameters

### Output Parameters

**userlist**

The list of users in the specified role. Contains two child parameters: user.id and user.name.

**grouplist**

The list of groups in the specified role. Contains two child parameters: group.id and group.name.


## DynamicRoleUpdate Variables

### Input Parameters

**records.to.update**

If you want to apply role changes to more than one record then you can specify all the records using this list. For each row in the list you must specify the app id, form id, record id and role name. 

**users.to.add**

The list of users to add to the specified role. Contains one child parameter: user.id.

**users.to.remove**

The list of users to remove from the specified role. Contains one child parameter: group.id.

**groups.to.add**

The list of groups to add to the specified role. Contains one child parameter: user.id.

**groups.to.remove**

The list of groups to remove from the specified role. Contains one child parameter: group.id.

**user.to.add**

The id of user to add to the specified role.

**user.to.remove**

The id of the user to remove from the specified role.

**group.to.add**

The id of the group to add to the specified role.

**group.to.remove**

The id of the group to remove from the specified role.


## UserInRoleLookup Variables

### Output Parameters

**inrole**

Boolean value. True if the user is in the role. Will be false if the look-up fails


## Transport ID

com.ibm.UserAndGroupInfoTransport.id

## Transport

Thr [Transport](CustomRoleManagmentTransport.jar) has been provided. Place the .jar file in the Leap extensions directory.

## Service Description XML Files

Copy the xml files into the Leap ServicesCatalog/1 directory.
[DynamicRoleLookup.xml](DynamicRoleLookup.xml)
[DynamicRoleUpdate.xml](DynamicRoleUpdate.xml)
[UserInRoleLookup.xml](UserInRoleLookup.xml)

## Sample Application

A [sample application](RoleServiceTestApp.leap) has been provided. Import the application into Leap **after** the transport and service description xml files have been deployed.