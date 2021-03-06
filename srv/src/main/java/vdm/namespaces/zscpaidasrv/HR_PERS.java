/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

/*
 * Generated by OData VDM code generator of SAP Cloud SDK in version 2.19.0
 */

package vdm.namespaces.zscpaidasrv;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.adapter.ODataField;
import com.sap.cloud.sdk.s4hana.datamodel.odata.annotation.Key;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity;
import com.sap.cloud.sdk.typeconverter.TypeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vdm.namespaces.zscpaidasrv.field.HR_PERSField;
import vdm.namespaces.zscpaidasrv.selectable.HR_PERSSelectable;
import vdm.services.ZSCPAIDASRVService;


/**
 * <p>Original entity name from the Odata EDM: <b>HR_PERS</b></p>
 * 
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(doNotUseGetters = true, callSuper = true)
@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
@JsonAdapter(com.sap.cloud.sdk.s4hana.datamodel.odata.adapter.ODataVdmEntityAdapterFactory.class)
public class HR_PERS
    extends VdmEntity<HR_PERS>
{

    /**
     * Selector for all available fields of HR_PERS.
     * 
     */
    public final static HR_PERSSelectable ALL_FIELDS = new HR_PERSSelectable() {


        @Nonnull
        @Override
        public String getFieldName() {
            return "*";
        }

        @Nonnull
        @Override
        public List<String> getSelections() {
            return Collections.singletonList("*");
        }

    }
    ;
    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>ILastname</b></p>
     * 
     * @return
     *     Cognome
     */
    @SerializedName("ILastname")
    @JsonProperty("ILastname")
    @Nullable
    @ODataField(odataName = "ILastname")
    private String iLastname;
    /**
     * Use with available fluent helpers to apply the <b>ILastname</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> I_LASTNAME = new HR_PERSField<String>("ILastname");
    /**
     * Constraints: Not nullable, Maximum length: 1 <p>Original property name from the Odata EDM: <b>Type</b></p>
     * 
     * @return
     *     The type_2 contained in this entity.
     */
    @SerializedName("Type")
    @JsonProperty("Type")
    @Nullable
    @ODataField(odataName = "Type")
    private String type_2;
    /**
     * Use with available fluent helpers to apply the <b>Type</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> TYPE_2 = new HR_PERSField<String>("Type");
    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>IName</b></p>
     * 
     * @return
     *     Nome
     */
    @SerializedName("IName")
    @JsonProperty("IName")
    @Nullable
    @ODataField(odataName = "IName")
    private String iName;
    /**
     * Use with available fluent helpers to apply the <b>IName</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> I_NAME = new HR_PERSField<String>("IName");
    /**
     * Constraints: Not nullable, Maximum length: 220 <p>Original property name from the Odata EDM: <b>Message</b></p>
     * 
     * @return
     *     The message contained in this entity.
     */
    @SerializedName("Message")
    @JsonProperty("Message")
    @Nullable
    @ODataField(odataName = "Message")
    private String message;
    /**
     * Use with available fluent helpers to apply the <b>Message</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> MESSAGE = new HR_PERSField<String>("Message");
    /**
     * Constraints: Not nullable, Maximum length: 30 <p>Original property name from the Odata EDM: <b>IUsrid</b></p>
     * 
     * @return
     *     ID sistema
     */
    @SerializedName("IUsrid")
    @JsonProperty("IUsrid")
    @Nullable
    @ODataField(odataName = "IUsrid")
    private String iUsrid;
    /**
     * Use with available fluent helpers to apply the <b>IUsrid</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> I_USRID = new HR_PERSField<String>("IUsrid");
    /**
     * (Key Field) Constraints: Not nullable, Maximum length: 30 <p>Original property name from the Odata EDM: <b>Usrid</b></p>
     * 
     * @return
     *     ID sistema
     */
    @Key
    @SerializedName("Usrid")
    @JsonProperty("Usrid")
    @Nullable
    @ODataField(odataName = "Usrid")
    private String usrid;
    /**
     * Use with available fluent helpers to apply the <b>Usrid</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> USRID = new HR_PERSField<String>("Usrid");
    /**
     * Constraints: Not nullable, Maximum length: 8 <p>Original property name from the Odata EDM: <b>Pernr</b></p>
     * 
     * @return
     *     C.I.D.
     */
    @SerializedName("Pernr")
    @JsonProperty("Pernr")
    @Nullable
    @ODataField(odataName = "Pernr")
    private String pernr;
    /**
     * Use with available fluent helpers to apply the <b>Pernr</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> PERNR = new HR_PERSField<String>("Pernr");
    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Nachn</b></p>
     * 
     * @return
     *     Cognome
     */
    @SerializedName("Nachn")
    @JsonProperty("Nachn")
    @Nullable
    @ODataField(odataName = "Nachn")
    private String nachn;
    /**
     * Use with available fluent helpers to apply the <b>Nachn</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> NACHN = new HR_PERSField<String>("Nachn");
    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Vorna</b></p>
     * 
     * @return
     *     Nome
     */
    @SerializedName("Vorna")
    @JsonProperty("Vorna")
    @Nullable
    @ODataField(odataName = "Vorna")
    private String vorna;
    /**
     * Use with available fluent helpers to apply the <b>Vorna</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> VORNA = new HR_PERSField<String>("Vorna");
    /**
     * Constraints: Not nullable, Maximum length: 10 <p>Original property name from the Odata EDM: <b>Gbdat</b></p>
     * 
     * @return
     *     Data nascita
     */
    @SerializedName("Gbdat")
    @JsonProperty("Gbdat")
    @Nullable
    @ODataField(odataName = "Gbdat")
    private String gbdat;
    /**
     * Use with available fluent helpers to apply the <b>Gbdat</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> GBDAT = new HR_PERSField<String>("Gbdat");
    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Gbort</b></p>
     * 
     * @return
     *     L.go nasc.
     */
    @SerializedName("Gbort")
    @JsonProperty("Gbort")
    @Nullable
    @ODataField(odataName = "Gbort")
    private String gbort;
    /**
     * Use with available fluent helpers to apply the <b>Gbort</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> GBORT = new HR_PERSField<String>("Gbort");
    /**
     * Constraints: Not nullable, Maximum length: 1 <p>Original property name from the Odata EDM: <b>Gesch</b></p>
     * 
     * @return
     *     Sesso
     */
    @SerializedName("Gesch")
    @JsonProperty("Gesch")
    @Nullable
    @ODataField(odataName = "Gesch")
    private String gesch;
    /**
     * Use with available fluent helpers to apply the <b>Gesch</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> GESCH = new HR_PERSField<String>("Gesch");
    /**
     * Constraints: Not nullable, Maximum length: 20 <p>Original property name from the Odata EDM: <b>Perid</b></p>
     * 
     * @return
     *     Cd.fiscale
     */
    @SerializedName("Perid")
    @JsonProperty("Perid")
    @Nullable
    @ODataField(odataName = "Perid")
    private String perid;
    /**
     * Use with available fluent helpers to apply the <b>Perid</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> PERID = new HR_PERSField<String>("Perid");
    /**
     * Constraints: Not nullable, Maximum length: 3 <p>Original property name from the Odata EDM: <b>Natio</b></p>
     * 
     * @return
     *     Cittadinanza
     */
    @SerializedName("Natio")
    @JsonProperty("Natio")
    @Nullable
    @ODataField(odataName = "Natio")
    private String natio;
    /**
     * Use with available fluent helpers to apply the <b>Natio</b> field to query operations.
     * 
     */
    public final static HR_PERSField<String> NATIO = new HR_PERSField<String>("Natio");

    /**
     * {@inheritDoc}
     * 
     */
    @Nonnull
    @Override
    public Class<HR_PERS> getType() {
        return HR_PERS.class;
    }

    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>ILastname</b></p>
     * 
     * @param iLastname
     *     Cognome
     */
    public void setILastname(
        @Nullable
        final String iLastname) {
        rememberChangedField("ILastname", this.iLastname);
        this.iLastname = iLastname;
    }

    /**
     * Constraints: Not nullable, Maximum length: 1 <p>Original property name from the Odata EDM: <b>Type</b></p>
     * 
     * @param type_2
     *     The type_2 to set.
     */
    public void setType_2(
        @Nullable
        final String type_2) {
        rememberChangedField("Type", this.type_2);
        this.type_2 = type_2;
    }

    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>IName</b></p>
     * 
     * @param iName
     *     Nome
     */
    public void setIName(
        @Nullable
        final String iName) {
        rememberChangedField("IName", this.iName);
        this.iName = iName;
    }

    /**
     * Constraints: Not nullable, Maximum length: 220 <p>Original property name from the Odata EDM: <b>Message</b></p>
     * 
     * @param message
     *     The message to set.
     */
    public void setMessage(
        @Nullable
        final String message) {
        rememberChangedField("Message", this.message);
        this.message = message;
    }

    /**
     * Constraints: Not nullable, Maximum length: 30 <p>Original property name from the Odata EDM: <b>IUsrid</b></p>
     * 
     * @param iUsrid
     *     ID sistema
     */
    public void setIUsrid(
        @Nullable
        final String iUsrid) {
        rememberChangedField("IUsrid", this.iUsrid);
        this.iUsrid = iUsrid;
    }

    /**
     * (Key Field) Constraints: Not nullable, Maximum length: 30 <p>Original property name from the Odata EDM: <b>Usrid</b></p>
     * 
     * @param usrid
     *     ID sistema
     */
    public void setUsrid(
        @Nullable
        final String usrid) {
        rememberChangedField("Usrid", this.usrid);
        this.usrid = usrid;
    }

    /**
     * Constraints: Not nullable, Maximum length: 8 <p>Original property name from the Odata EDM: <b>Pernr</b></p>
     * 
     * @param pernr
     *     C.I.D.
     */
    public void setPernr(
        @Nullable
        final String pernr) {
        rememberChangedField("Pernr", this.pernr);
        this.pernr = pernr;
    }

    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Nachn</b></p>
     * 
     * @param nachn
     *     Cognome
     */
    public void setNachn(
        @Nullable
        final String nachn) {
        rememberChangedField("Nachn", this.nachn);
        this.nachn = nachn;
    }

    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Vorna</b></p>
     * 
     * @param vorna
     *     Nome
     */
    public void setVorna(
        @Nullable
        final String vorna) {
        rememberChangedField("Vorna", this.vorna);
        this.vorna = vorna;
    }

    /**
     * Constraints: Not nullable, Maximum length: 10 <p>Original property name from the Odata EDM: <b>Gbdat</b></p>
     * 
     * @param gbdat
     *     Data nascita
     */
    public void setGbdat(
        @Nullable
        final String gbdat) {
        rememberChangedField("Gbdat", this.gbdat);
        this.gbdat = gbdat;
    }

    /**
     * Constraints: Not nullable, Maximum length: 40 <p>Original property name from the Odata EDM: <b>Gbort</b></p>
     * 
     * @param gbort
     *     L.go nasc.
     */
    public void setGbort(
        @Nullable
        final String gbort) {
        rememberChangedField("Gbort", this.gbort);
        this.gbort = gbort;
    }

    /**
     * Constraints: Not nullable, Maximum length: 1 <p>Original property name from the Odata EDM: <b>Gesch</b></p>
     * 
     * @param gesch
     *     Sesso
     */
    public void setGesch(
        @Nullable
        final String gesch) {
        rememberChangedField("Gesch", this.gesch);
        this.gesch = gesch;
    }

    /**
     * Constraints: Not nullable, Maximum length: 20 <p>Original property name from the Odata EDM: <b>Perid</b></p>
     * 
     * @param perid
     *     Cd.fiscale
     */
    public void setPerid(
        @Nullable
        final String perid) {
        rememberChangedField("Perid", this.perid);
        this.perid = perid;
    }

    /**
     * Constraints: Not nullable, Maximum length: 3 <p>Original property name from the Odata EDM: <b>Natio</b></p>
     * 
     * @param natio
     *     Cittadinanza
     */
    public void setNatio(
        @Nullable
        final String natio) {
        rememberChangedField("Natio", this.natio);
        this.natio = natio;
    }

    @Override
    protected String getEntityCollection() {
        return "HR_PERSSet";
    }

    @Nonnull
    @Override
    protected Map<String, Object> getKey() {
        final Map<String, Object> result = Maps.newHashMap();
        result.put("Usrid", getUsrid());
        return result;
    }

    @Nonnull
    @Override
    protected Map<String, Object> toMapOfFields() {
        final Map<String, Object> values = super.toMapOfFields();
        values.put("ILastname", getILastname());
        values.put("Type", getType_2());
        values.put("IName", getIName());
        values.put("Message", getMessage());
        values.put("IUsrid", getIUsrid());
        values.put("Usrid", getUsrid());
        values.put("Pernr", getPernr());
        values.put("Nachn", getNachn());
        values.put("Vorna", getVorna());
        values.put("Gbdat", getGbdat());
        values.put("Gbort", getGbort());
        values.put("Gesch", getGesch());
        values.put("Perid", getPerid());
        values.put("Natio", getNatio());
        return values;
    }

    @Override
    protected void fromMap(final Map<String, Object> inputValues) {
        final Map<String, Object> values = Maps.newHashMap(inputValues);
        // simple properties
        {
            if (values.containsKey("ILastname")) {
                final Object value = values.remove("ILastname");
                if ((value == null)||(!value.equals(getILastname()))) {
                    setILastname(((String) value));
                }
            }
            if (values.containsKey("Type")) {
                final Object value = values.remove("Type");
                if ((value == null)||(!value.equals(getType_2()))) {
                    setType_2(((String) value));
                }
            }
            if (values.containsKey("IName")) {
                final Object value = values.remove("IName");
                if ((value == null)||(!value.equals(getIName()))) {
                    setIName(((String) value));
                }
            }
            if (values.containsKey("Message")) {
                final Object value = values.remove("Message");
                if ((value == null)||(!value.equals(getMessage()))) {
                    setMessage(((String) value));
                }
            }
            if (values.containsKey("IUsrid")) {
                final Object value = values.remove("IUsrid");
                if ((value == null)||(!value.equals(getIUsrid()))) {
                    setIUsrid(((String) value));
                }
            }
            if (values.containsKey("Usrid")) {
                final Object value = values.remove("Usrid");
                if ((value == null)||(!value.equals(getUsrid()))) {
                    setUsrid(((String) value));
                }
            }
            if (values.containsKey("Pernr")) {
                final Object value = values.remove("Pernr");
                if ((value == null)||(!value.equals(getPernr()))) {
                    setPernr(((String) value));
                }
            }
            if (values.containsKey("Nachn")) {
                final Object value = values.remove("Nachn");
                if ((value == null)||(!value.equals(getNachn()))) {
                    setNachn(((String) value));
                }
            }
            if (values.containsKey("Vorna")) {
                final Object value = values.remove("Vorna");
                if ((value == null)||(!value.equals(getVorna()))) {
                    setVorna(((String) value));
                }
            }
            if (values.containsKey("Gbdat")) {
                final Object value = values.remove("Gbdat");
                if ((value == null)||(!value.equals(getGbdat()))) {
                    setGbdat(((String) value));
                }
            }
            if (values.containsKey("Gbort")) {
                final Object value = values.remove("Gbort");
                if ((value == null)||(!value.equals(getGbort()))) {
                    setGbort(((String) value));
                }
            }
            if (values.containsKey("Gesch")) {
                final Object value = values.remove("Gesch");
                if ((value == null)||(!value.equals(getGesch()))) {
                    setGesch(((String) value));
                }
            }
            if (values.containsKey("Perid")) {
                final Object value = values.remove("Perid");
                if ((value == null)||(!value.equals(getPerid()))) {
                    setPerid(((String) value));
                }
            }
            if (values.containsKey("Natio")) {
                final Object value = values.remove("Natio");
                if ((value == null)||(!value.equals(getNatio()))) {
                    setNatio(((String) value));
                }
            }
        }
        // structured properties
        {
        }
        // navigation properties
        {
        }
        super.fromMap(values);
    }

    /**
     * Use with available fluent helpers to apply an extension field to query operations.
     * 
     * @param fieldName
     *     The name of the extension field as returned by the OData service.
     * @param <T>
     *     The type of the extension field when performing value comparisons.
     * @param fieldType
     *     The Java type to use for the extension field when performing value comparisons.
     * @return
     *     A representation of an extension field from this entity.
     */
    @Nonnull
    public static<T >HR_PERSField<T> field(
        @Nonnull
        final String fieldName,
        @Nonnull
        final Class<T> fieldType) {
        return new HR_PERSField<T>(fieldName);
    }

    /**
     * Use with available fluent helpers to apply an extension field to query operations.
     * 
     * @param typeConverter
     *     A TypeConverter<T, DomainT> instance whose first generic type matches the Java type of the field
     * @param fieldName
     *     The name of the extension field as returned by the OData service.
     * @param <T>
     *     The type of the extension field when performing value comparisons.
     * @param <DomainT>
     *     The type of the extension field as returned by the OData service.
     * @return
     *     A representation of an extension field from this entity, holding a reference to the given TypeConverter.
     */
    @Nonnull
    public static<T,DomainT >HR_PERSField<T> field(
        @Nonnull
        final String fieldName,
        @Nonnull
        final TypeConverter<T, DomainT> typeConverter) {
        return new HR_PERSField<T>(fieldName, typeConverter);
    }

    @Override
    @Nullable
    public ErpConfigContext getErpConfigContext() {
        return super.getErpConfigContext();
    }

    /**
     * 
     * @deprecated
     *     Use {@link #attachToService(String, ErpConfigContext)} instead.
     */
    @Override
    @Deprecated
    public void setErpConfigContext(
        @Nullable
        final ErpConfigContext erpConfigContext) {
        super.setErpConfigContext(erpConfigContext);
    }

    @Override
    protected void setServicePathForFetch(
        @Nullable
        final String servicePathForFetch) {
        super.setServicePathForFetch(servicePathForFetch);
    }

    @Override
    public void attachToService(
        @Nullable
        final String servicePath,
        @Nullable
        final ErpConfigContext configContext) {
        super.attachToService(servicePath, configContext);
    }

    @Override
    protected String getDefaultServicePath() {
        return ZSCPAIDASRVService.DEFAULT_SERVICE_PATH;
    }

}
