/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.namespaces.zscpaidasrv;

import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.FluentHelperByKey;
import vdm.namespaces.zscpaidasrv.selectable.FornitoreSelectable;


/**
 * Fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. 
 * 
 */
public class FornitoreByKeyFluentHelper
    extends FluentHelperByKey<FornitoreByKeyFluentHelper, Fornitore, FornitoreSelectable>
{

    private final Map<String, Object> key = Maps.newHashMap();

    /**
     * Creates a fluent helper object that will fetch a single {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entity with the provided key field values. To perform execution, call the {@link #execute execute} method on the fluent helper object.
     * 
     * @param servicePath
     *     Service path to be used to fetch a single {@code Fornitore}
     * @param lifnr
     *     
     */
    public FornitoreByKeyFluentHelper(
        @Nonnull
        final String servicePath, final String lifnr) {
        super(servicePath);
        this.key.put("Lifnr", lifnr);
    }

    @Override
    @Nonnull
    protected Class<Fornitore> getEntityClass() {
        return Fornitore.class;
    }

    @Override
    @Nonnull
    protected Map<String, Object> getKey() {
        return key;
    }

}
