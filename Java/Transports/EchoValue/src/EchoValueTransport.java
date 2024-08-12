/********************************************************* {COPYRIGHT-TOP} ***
* Copyright 2024  HCL Corp. 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not 
* use this file except in compliance with the License. You may obtain a copy 
* of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
********************************************************** {COPYRIGHT-END} **/
package com.ibm.form.examples;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

//Required packages for logging
//import java.util.logging.Level;
//import java.util.logging.Logger;

import com.ibm.form.nitro.service.model.IUser;
import com.ibm.form.nitro.service.services.*;

public class EchoValueTransport implements IServiceTransport {
	
	//Used for logging if desired
	//private static Logger sLog = Logger.getLogger(EchoValueTransport.class.getName());

	/**
	 * Defines the ID that is used to call the transport.  This ID will be used in the 
	 * service XML file parameter -> <transportId>EchoValueTransport</transportId>
	 */
    public String getId()
    {
        return "EchoValueTransport";
    }

    /**
     * This function defines the transport "work".  All the input, constant and output parameters
     * are contained in the pParameters Map.  You can add new output parameters by adding them to 
     * the map.
     */
    public ServiceResult run(IServiceDescription pDescription,
                             IServiceCredentialsProvider pServiceCredentialsProvider, IUser pUser,
                             Map<String, Object> pParameters)
    {
        String input = (String) pParameters.get("input");
        
        pParameters.put("output", input);
        ServiceResult result = new ServiceResult(200, "success");
        return result;
    }

    /**
     * Required functions
     * As of 8.6.4 is not implemented but is required 
     */
    public Collection<IServiceDescription> getSampleServiceDescriptions()
    {
    	return Collections.emptyList();
    }

    public IServiceTransportMetadata getMetadata()
    {
        return new IServiceTransportMetadata()
        {
            public Set<String> getAllowableCredentialsProviderIds()
            {
                return Collections.emptySet();
            }
        };
    }  
}
