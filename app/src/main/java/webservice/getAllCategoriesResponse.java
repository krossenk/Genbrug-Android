package webservice;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.5
//
// Created by Quasar Development at 24-05-2015
//
//---------------------------------------------------



import org.ksoap2.serialization.*;
import java.util.Vector;
import java.util.Hashtable;


public class getAllCategoriesResponse extends Vector<Category> implements KvmSerializable
{
    
    public getAllCategoriesResponse(){}
    
    public getAllCategoriesResponse(java.lang.Object inObj, ExtendedSoapSerializationEnvelope __envelope)
    {
        if (inObj == null)
            return;
        SoapObject soapObject=(SoapObject)inObj;
        int size = soapObject.getPropertyCount();
        for (int i0=0;i0< size;i0++)
        {
            java.lang.Object obj = soapObject.getProperty(i0);
            if (obj!=null && obj instanceof AttributeContainer)
            {
                AttributeContainer j =(AttributeContainer) soapObject.getProperty(i0);
                Category j1= (Category)__envelope.get(j,Category.class);
                add(j1);
            }
        }
}
    
    @Override
    public java.lang.Object getProperty(int arg0) {
        return this.get(arg0)!=null?this.get(arg0):SoapPrimitive.NullNilElement;
    }
    
    @Override
    public int getPropertyCount() {
        return this.size();
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        info.name = "return";
        info.type = Category.class;
    	info.namespace= "";
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1) {
    }

    @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }

}