package webservice;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.5
//
// Created by Quasar Development at 31-05-2015
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;


public class Publication extends AttributeContainer implements KvmSerializable
{
    
    public Address addressid;
    
    public Category categoryId;
    
    public String description;
    
    public Long id;
    
    public String imageURL;
    
    public String pickupEndtime;
    
    public String pickupStartime;
    
    public String pickuptype;
    
    public String timestamp;
    
    public String title;
    
    public User userId;

    public Publication()
    {
    }

    public Publication(java.lang.Object paramObj, ExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        SoapObject soapObject=(SoapObject)inObj;  
        if (soapObject.hasProperty("addressid"))
        {	
	        java.lang.Object j = soapObject.getProperty("addressid");
	        this.addressid = (Address)__envelope.get(j, Address.class);
        }
        if (soapObject.hasProperty("categoryId"))
        {	
	        java.lang.Object j = soapObject.getProperty("categoryId");
	        this.categoryId = (Category)__envelope.get(j,Category.class);
        }
        if (soapObject.hasProperty("description"))
        {	
	        java.lang.Object obj = soapObject.getProperty("description");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.description = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.description = (String)obj;
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
                    this.id = new Long(j.toString());
                }
	        }
	        else if (obj!= null && obj instanceof Long){
                this.id = (Long)obj;
            }    
        }
        if (soapObject.hasProperty("imageURL"))
        {	
	        java.lang.Object obj = soapObject.getProperty("imageURL");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.imageURL = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.imageURL = (String)obj;
            }    
        }
        if (soapObject.hasProperty("pickupEndtime"))
        {	
	        java.lang.Object obj = soapObject.getProperty("pickupEndtime");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.pickupEndtime = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.pickupEndtime = (String)obj;
            }    
        }
        if (soapObject.hasProperty("pickupStartime"))
        {	
	        java.lang.Object obj = soapObject.getProperty("pickupStartime");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.pickupStartime = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.pickupStartime = (String)obj;
            }    
        }
        if (soapObject.hasProperty("pickuptype"))
        {	
	        java.lang.Object obj = soapObject.getProperty("pickuptype");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.pickuptype = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.pickuptype = (String)obj;
            }    
        }
        if (soapObject.hasProperty("timestamp"))
        {	
	        java.lang.Object obj = soapObject.getProperty("timestamp");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.timestamp = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.timestamp = (String)obj;
            }    
        }
        if (soapObject.hasProperty("title"))
        {	
	        java.lang.Object obj = soapObject.getProperty("title");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.title = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.title = (String)obj;
            }    
        }
        if (soapObject.hasProperty("userId"))
        {	
	        java.lang.Object j = soapObject.getProperty("userId");
	        this.userId = (User)__envelope.get(j, User.class);
        }


    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return this.addressid!=null?this.addressid:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.categoryId!=null?this.categoryId:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.description!=null?this.description:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.id!=null?this.id:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==4)
        {
            return this.imageURL!=null?this.imageURL:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==5)
        {
            return this.pickupEndtime!=null?this.pickupEndtime:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==6)
        {
            return this.pickupStartime!=null?this.pickupStartime:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==7)
        {
            return this.pickuptype!=null?this.pickuptype:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==8)
        {
            return this.timestamp!=null?this.timestamp:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==9)
        {
            return this.title!=null?this.title:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==10)
        {
            return this.userId!=null?this.userId:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 11;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = Address.class;
            info.name = "addressid";
            info.namespace= "";
        }
        if(propertyIndex==1)
        {
            info.type = Category.class;
            info.name = "categoryId";
            info.namespace= "";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "description";
            info.namespace= "";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.LONG_CLASS;
            info.name = "id";
            info.namespace= "";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "imageURL";
            info.namespace= "";
        }
        if(propertyIndex==5)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "pickupEndtime";
            info.namespace= "";
        }
        if(propertyIndex==6)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "pickupStartime";
            info.namespace= "";
        }
        if(propertyIndex==7)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "pickuptype";
            info.namespace= "";
        }
        if(propertyIndex==8)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "timestamp";
            info.namespace= "";
        }
        if(propertyIndex==9)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "title";
            info.namespace= "";
        }
        if(propertyIndex==10)
        {
            info.type = User.class;
            info.name = "userId";
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
