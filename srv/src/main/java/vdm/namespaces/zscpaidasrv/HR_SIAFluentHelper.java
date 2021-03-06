/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.namespaces.zscpaidasrv;

import javax.annotation.Nonnull;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.FluentHelperRead;
import vdm.namespaces.zscpaidasrv.selectable.HR_SIASelectable;


/**
 * Fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_SIA HR_SIA} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. 
 * 
 */
public class HR_SIAFluentHelper
    extends FluentHelperRead<HR_SIAFluentHelper, HR_SIA, HR_SIASelectable>
{


    /**
     * Creates a fluent helper using the specified service path to send the read requests.
     * 
     * @param servicePath
     *     The service path to direct the read requests to.
     */
    public HR_SIAFluentHelper(
        @Nonnull
        final String servicePath) {
        super(servicePath);
    }

    @Override
    @Nonnull
    protected Class<HR_SIA> getEntityClass() {
        return HR_SIA.class;
    }

}
