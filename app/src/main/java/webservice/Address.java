package webservice;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.5
//
// Created by Quasar Development at 24-05-2015
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;

public class Address extends AttributeContainer implements KvmSerializable
{
    
    public String city;
    
    public String country;
    
    public Integer id;
    
    public String street;
    
    public Integer zipcode;

    public Address()
    {
    }

    public Address(java.lang.Object paramObj, ExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        SoapObject soapObject=(SoapObject)inObj;  
        if (soapObject.hasProperty("city"))
        {	
	        java.lang.Object obj = soapObject.getProperty("city");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.city = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.city = (String)obj;
            }    
        }
        if (soapObject.hasProperty("country"))
        {	
	        java.lang.Object obj = soapObject.getProperty("country");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.country = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.country = (String)obj;
            }    
        }
        if (soapObject.hasProperty("id"))
        {	
	        java.lang.Object obj = soapObject.getProperty("id");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.id = Integer.parseInt(j.toString());
                }
	        }
	        else if (obj!= null && obj instanceof Integer){
                this.id = (Integer)obj;
            }    
        }
        if (soapObject.hasProperty("street"))
        {	
	        java.lang.Object obj = soapObject.getProperty("street");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.street = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.street = (String)obj;
            }    
        }
        if (soapObject.hasProperty("zipcode"))
        {	
	        java.lang.Object obj = soapObject.getProperty("zipcode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.zipcode = Integer.parseInt(j.toString());
                }
	        }
	        else if (obj!= null && obj instanceof Integer){
                this.zipcode = (Integer)obj;
            }    
        }


    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return this.city!=null?this.city:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.country!=null?this.country:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.id!=null?this.id:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.street!=null?this.street:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==4)
        {
            return this.zipcode!=null?this.zipcode:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 5;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "city";
            info.namespace= "";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "country";
            info.namespace= "";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "id";
            info.namespace= "";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "street";
            info.namespace= "";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "zipcode";
            info.namespace= "";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }

    @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }
}
