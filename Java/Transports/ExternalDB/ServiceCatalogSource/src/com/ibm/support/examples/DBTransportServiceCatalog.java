package com.ibm.support.examples;

import com.ibm.form.nitro.service.model.*;
import com.ibm.form.nitro.service.services.*;

import java.io.InputStreamReader;
import java.util.*;

public class DBTransportServiceCatalog     
	extends AbstractServiceCatalog
	implements IServiceCatalog {

    private IServiceDescriptionBuilderFactory mServiceDescriptionBuilderFactory;

    /**
     * This ID must match the ID defined by the group catalog
     */
    public String getGroupId()
    {
        return "com.ibm.support.services.DBTransportServiceCatalogGroup.id";
    }

    public void setServiceDescriptionBuilderFactory(IServiceDescriptionBuilderFactory pServiceDescriptionBuilderFactory)
    {
    	mServiceDescriptionBuilderFactory = pServiceDescriptionBuilderFactory;
    } 
    
    public Collection<IServiceDescription> loadDescriptions(IServiceDescriptionBuilderFactory mServiceDescriptionBuilderFactory) {
    	Collection<IServiceDescription> mServiceDescriptions = null;
    	
        mServiceDescriptions = new ArrayList<IServiceDescription>();
        try
        {            	
            IServiceDescriptionBuilder builder1 = mServiceDescriptionBuilderFactory.newDescriptionBuilder();
                        
            //add all the service descriptions that are bundled with this jar
            mServiceDescriptions.add(builder1.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Services/DB_People_All_Actions.xml"))));
            mServiceDescriptions.add(builder1.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Services/DB_People_Delete.xml"))));
            mServiceDescriptions.add(builder1.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Services/DB_People_Insert.xml"))));
            mServiceDescriptions.add(builder1.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Services/DB_People_Select.xml"))));
            mServiceDescriptions.add(builder1.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Services/DB_People_Update.xml"))));
            
            return mServiceDescriptions;
        }
        catch (ServiceException ex)
        {
            throw new RuntimeException(ex);
        }    
    }

    public Collection<IServiceDescription> getServiceDescriptions(IServiceManager pManager, IOrg pOrg,
                                                                  IUser pUser, String pFilterCatalog,
                                                                  String pFilterStatus, String pFilterText,
                                                                  boolean pFilterIncludesDesc,
                                                                  Locale pFilterLocale)
    {	    	
    	Collection<IServiceDescription> services = loadDescriptions(mServiceDescriptionBuilderFactory);
        this.filterServiceDescriptions(pManager, services, pFilterStatus, pFilterText,
                                       pFilterIncludesDesc, pFilterLocale);
    	
        return Collections.unmodifiableCollection(services);
    }

    public IServiceDescription getServiceDescription(IServiceManager pManager, IOrg pOrg, IUser pUser,
                                                     String pServiceDescriptionId)
    {
        for (IServiceDescription description : loadDescriptions(mServiceDescriptionBuilderFactory))
        {
            if (description.getId().equals(pServiceDescriptionId))
                return description;
        }
        return null;
    }    
}
