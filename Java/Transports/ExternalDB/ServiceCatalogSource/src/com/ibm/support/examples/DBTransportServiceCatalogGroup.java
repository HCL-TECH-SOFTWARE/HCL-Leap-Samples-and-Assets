package com.ibm.support.examples;


import com.ibm.form.nitro.service.services.IServiceCatalogGroup;
import com.ibm.form.platform.service.framework.i18n.LocaleUtils;
import java.util.Locale;

public class DBTransportServiceCatalogGroup implements IServiceCatalogGroup {
	
    public String getGroupId()
    {
        return "com.ibm.support.services.DBTransportServiceCatalogGroup.id"; 
    }

    public String getGroupName()
    {
        Locale requestLocale = LocaleUtils.getRequestLocale();

        /* use request locale to determine a translated group name */

        return "DB Transport Services Demo";
    }
}
