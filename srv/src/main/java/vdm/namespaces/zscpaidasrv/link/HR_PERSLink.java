/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.namespaces.zscpaidasrv.link;

import javax.annotation.Nonnull;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.EntityLink;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject;
import vdm.namespaces.zscpaidasrv.HR_PERS;
import vdm.namespaces.zscpaidasrv.selectable.HR_PERSSelectable;


/**
 * Template class to represent entity navigation links of {@link vdm.namespaces.zscpaidasrv.HR_PERS HR_PERS} to other entities. Instances of this object are used in query modifier methods of the entity
 * fluent helpers. Contains methods to compare a field's value with a provided value.
 * 
 * Use the constants declared in each entity inner class. Instantiating directly requires knowing the underlying OData
 * field names, so use the constructor with caution.
 * 
 * @param <ObjectT>
 * Entity type of subclasses from {@link com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject VdmObject}.
 * 
 */
public class HR_PERSLink<ObjectT extends VdmObject<?> >
    extends EntityLink<HR_PERSLink<ObjectT> , HR_PERS, ObjectT>
    implements HR_PERSSelectable
{


    /**
     * Use the constants declared in each entity inner class. Instantiating directly requires knowing the underlying OData field names, so use with caution.
     * 
     * @param fieldName
     *     OData navigation field name. Must match the field returned by the underlying OData service.
     */
    public HR_PERSLink(final String fieldName) {
        super(fieldName);
    }

    private HR_PERSLink(final EntityLink<HR_PERSLink<ObjectT> , HR_PERS, ObjectT> toClone) {
        super(toClone);
    }

    @Nonnull
    @Override
    protected HR_PERSLink<ObjectT> translateLinkType(final EntityLink<HR_PERSLink<ObjectT> , HR_PERS, ObjectT> link) {
        return new HR_PERSLink<ObjectT>(link);
    }

}