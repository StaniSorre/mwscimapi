/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.services;

import javax.annotation.Nonnull;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.batch.BatchService;
import vdm.namespaces.zscpaidasrv.FornitoreByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.FornitoreFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_MNGByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_MNGFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_PERSByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_PERSFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_SIAByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.HR_SIAFluentHelper;
import vdm.namespaces.zscpaidasrv.HrorgByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.HrorgFluentHelper;
import vdm.namespaces.zscpaidasrv.OdmByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.OdmFluentHelper;
import vdm.namespaces.zscpaidasrv.ZAIDA_HR_PERSByKeyFluentHelper;
import vdm.namespaces.zscpaidasrv.batch.ZSCPAIDASRVServiceBatch;


/**
 * <h3>Details:</h3><table summary='Details'><tr><td align='right'>OData Service:</td><td>ZSCP_AIDA_SRV</td></tr></table>
 * 
 */
public interface ZSCPAIDASRVService
    extends BatchService<ZSCPAIDASRVServiceBatch>
{

    /**
     * If no other path was provided via the {@link #withServicePath(String)} method, this is the default service path used to access the endpoint.
     * 
     */
    //String DEFAULT_SERVICE_PATH = "/sap/opu/odata/sap/ZSCP_AIDA_SRV";
    String DEFAULT_SERVICE_PATH = "/v1/in_ZSCP_AIDA_SRV";

    /**
     * Overrides the default service path and returns a new service instance with the specified service path. Also adjusts the respective entity URLs.
     * 
     * @param servicePath
     *     Service path that will override the default.
     * @return
     *     A new service instance with the specified service path.
     */
    @Nonnull
    ZSCPAIDASRVService withServicePath(
        @Nonnull
        final String servicePath);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.FornitoreFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    FornitoreFluentHelper getAllFornitore();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entity using key fields.
     * 
     * @param lifnr
     *     
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.Fornitore Fornitore} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.FornitoreByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    FornitoreByKeyFluentHelper getFornitoreByKey(final String lifnr);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.Odm Odm} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.Odm Odm} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.OdmFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    OdmFluentHelper getAllOdm();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.Odm Odm} entity using key fields.
     * 
     * @param inOrder
     *     Ordine<p>Constraints: Not nullable, Maximum length: 12</p>
     * @param ouOrderid
     *     Ordine<p>Constraints: Not nullable, Maximum length: 12</p>
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.Odm Odm} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.OdmByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    OdmByKeyFluentHelper getOdmByKey(final String inOrder, final String ouOrderid);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.Hrorg Hrorg} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.Hrorg Hrorg} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HrorgFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HrorgFluentHelper getAllHrorg();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.Hrorg Hrorg} entity using key fields.
     * 
     * @param usrid
     *     ID sistema<p>Constraints: Not nullable, Maximum length: 30</p>
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.Hrorg Hrorg} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HrorgByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HrorgByKeyFluentHelper getHrorgByKey(final String usrid);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_PERS HR_PERS} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_PERS HR_PERS} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_PERSFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_PERSFluentHelper getAllHR_PERS();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.HR_PERS HR_PERS} entity using key fields.
     * 
     * @param usrid
     *     ID sistema<p>Constraints: Not nullable, Maximum length: 30</p>
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.HR_PERS HR_PERS} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_PERSByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_PERSByKeyFluentHelper getHR_PERSByKey(final String usrid);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_SIA HR_SIA} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_SIA HR_SIA} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_SIAFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_SIAFluentHelper getAllHR_SIA();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.HR_SIA HR_SIA} entity using key fields.
     * 
     * @param mandt
     *     
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.HR_SIA HR_SIA} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_SIAByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_SIAByKeyFluentHelper getHR_SIAByKey(final String mandt);

    /**
     * Fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_MNG HR_MNG} entities.
     * 
     * @return
     *     A fluent helper to fetch multiple {@link vdm.namespaces.zscpaidasrv.HR_MNG HR_MNG} entities. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_MNGFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_MNGFluentHelper getAllHR_MNG();

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.HR_MNG HR_MNG} entity using key fields.
     * 
     * @param uosap
     *     
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.HR_MNG HR_MNG} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.HR_MNGByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    HR_MNGByKeyFluentHelper getHR_MNGByKey(final String uosap);

    /**
     * Fetch a single {@link vdm.namespaces.zscpaidasrv.ZAIDA_HR_PERS ZAIDA_HR_PERS} entity using key fields.
     * 
     * @param iUsrid
     *     ID sistema<p>Constraints: Not nullable, Maximum length: 30</p>
     * @return
     *     A fluent helper to fetch a single {@link vdm.namespaces.zscpaidasrv.ZAIDA_HR_PERS ZAIDA_HR_PERS} entity using key fields. This fluent helper allows methods which modify the underlying query to be called before executing the query itself. To perform execution, call the {@link vdm.namespaces.zscpaidasrv.ZAIDA_HR_PERSByKeyFluentHelper#execute execute} method on the fluent helper object. 
     */
    @Nonnull
    ZAIDA_HR_PERSByKeyFluentHelper getZAIDA_HR_PERSByKey(final String iUsrid);

}
