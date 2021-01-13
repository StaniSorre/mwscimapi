/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.namespaces.zscpaidasrv.batch;

import javax.annotation.Nonnull;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.batch.BatchChangeSetFluentHelperBasic;
import vdm.services.ZSCPAIDASRVService;


/**
 * Implementation of the {@link ZSCPAIDASRVServiceBatchChangeSet} interface, enabling you to combine multiple operations into one changeset. For further information have a look into the {@link vdm.services.ZSCPAIDASRVService ZSCPAIDASRVService}.
 * 
 */
public class DefaultZSCPAIDASRVServiceBatchChangeSet
    extends BatchChangeSetFluentHelperBasic<ZSCPAIDASRVServiceBatch, ZSCPAIDASRVServiceBatchChangeSet>
    implements ZSCPAIDASRVServiceBatchChangeSet
{

    @Nonnull
    private final ZSCPAIDASRVService service;

    DefaultZSCPAIDASRVServiceBatchChangeSet(
        @Nonnull
        final DefaultZSCPAIDASRVServiceBatch batchFluentHelper,
        @Nonnull
        final ZSCPAIDASRVService service) {
        super(batchFluentHelper, batchFluentHelper);
        this.service = service;
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Nonnull
    @Override
    protected DefaultZSCPAIDASRVServiceBatchChangeSet getThis() {
        return this;
    }

}
