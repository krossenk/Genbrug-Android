package webservice;




//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.5
//
// Created by Quasar Development at 24-05-2015
//
//---------------------------------------------------




import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.*;

import java.util.List;


public class RecycleWebService
{
    interface IWcfMethod
    {
        ExtendedSoapSerializationEnvelope CreateSoapEnvelope() throws java.lang.Exception;

        java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object result) throws java.lang.Exception;
    }

    String url="http://vmi19372.iry.dk:8880/RecycleWebService/RecycleWebService";

    int timeOut=60000;
    public List< HeaderProperty> httpHeaders;
    public boolean enableLogging;

    IServiceEvents callback;
    public RecycleWebService(){}

    public RecycleWebService(IServiceEvents callback)
    {
        this.callback = callback;
    }
    public RecycleWebService(IServiceEvents callback, String url)
    {
        this.callback = callback;
        this.url = url;
    }

    public RecycleWebService(IServiceEvents callback, String url, int timeOut)
    {
        this.callback = callback;
        this.url = url;
        this.timeOut=timeOut;
    }

    protected org.ksoap2.transport.Transport createTransport()
    {
        try
        {
            java.net.URI uri = new java.net.URI(url);
            if(uri.getScheme().equalsIgnoreCase("https"))
            {
                int port=uri.getPort()>0?uri.getPort():443;
                return new HttpsTransportSE(uri.getHost(), port, uri.getPath(), timeOut);
            }
            else
            {
                return new HttpTransportSE(url,timeOut);
            }

        }
        catch (java.net.URISyntaxException e)
        {
        }
        return null;
    }
    
    protected ExtendedSoapSerializationEnvelope createEnvelope()
    {
        ExtendedSoapSerializationEnvelope envelope= new ExtendedSoapSerializationEnvelope(ExtendedSoapSerializationEnvelope.VER11);
        return envelope;
    }
    
    protected java.util.List sendRequest(String methodName, ExtendedSoapSerializationEnvelope envelope,org.ksoap2.transport.Transport transport  )throws java.lang.Exception
    {
        return transport.call(methodName, envelope, httpHeaders);
    }

    java.lang.Object getResult(java.lang.Class destObj,java.lang.Object source,String resultName, ExtendedSoapSerializationEnvelope __envelope) throws java.lang.Exception
    {
        if(source==null)
        {
            return null;
        }
        if(source instanceof SoapPrimitive)
        {
            SoapPrimitive soap =(SoapPrimitive)source;
            if(soap.getName().equals(resultName))
            {
                java.lang.Object instance=__envelope.get(source,destObj);
                return instance;
            }
        }
        else
        {
            SoapObject soap = (SoapObject)source;
            if (soap.hasProperty(resultName))
            {
                java.lang.Object j=soap.getProperty(resultName);
                if(j==null)
                {
                    return null;
                }
                java.lang.Object instance=__envelope.get(j,destObj);
                return instance;
            }
            else if( soap.getName().equals(resultName)) {
                java.lang.Object instance=__envelope.get(source,destObj);
                return instance;
            }
       }

       return null;
    }

        
    public getPublicationSubscriptionsResponse getPublicationSubscriptions(final Long publicationId ) throws java.lang.Exception
    {
        return (getPublicationSubscriptionsResponse)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getPublicationSubscriptions");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="publicationId";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(publicationId);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (getPublicationSubscriptionsResponse)getResult(getPublicationSubscriptionsResponse.class,__result,"getPublicationSubscriptionsResponse",__envelope);
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult<getPublicationSubscriptionsResponse>> getPublicationSubscriptionsAsync(final Long publicationId)
    {
        return executeAsync(new Functions.IFunc<getPublicationSubscriptionsResponse>() {
            public getPublicationSubscriptionsResponse Func() throws java.lang.Exception {
                return getPublicationSubscriptions( publicationId);
            }
        });
    }
    
    public Long createPublication(final Publication publication ) throws java.lang.Exception
    {
        return (Long)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                __envelope.addMapping("","publication",new Publication().getClass());
                SoapObject __soapReq = new SoapObject("http://recycle/", "createPublication");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="publication";
                __info.type= Publication.class;
                __info.setValue(publication!=null?publication:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("return");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return new Long(j.toString());
                }
                else if (obj!= null && obj instanceof Long){
                    return (Long)obj;
                }
                return null;
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult< Long>> createPublicationAsync(final Publication publication)
    {
        return executeAsync(new Functions.IFunc< Long>() {
            public Long Func() throws java.lang.Exception {
                return createPublication( publication);
            }
        });
    }
    
    public getUserSubscriptionsResponse getUserSubscriptions(final Long userId ) throws java.lang.Exception
    {
        return (getUserSubscriptionsResponse)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getUserSubscriptions");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="userId";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(userId);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (getUserSubscriptionsResponse)getResult(getUserSubscriptionsResponse.class,__result,"getUserSubscriptionsResponse",__envelope);
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult<getUserSubscriptionsResponse>> getUserSubscriptionsAsync(final Long userId)
    {
        return executeAsync(new Functions.IFunc<getUserSubscriptionsResponse>() {
            public getUserSubscriptionsResponse Func() throws java.lang.Exception {
                return getUserSubscriptions( userId);
            }
        });
    }
    
    public getAllPublicationsResponse getAllPublications( ) throws java.lang.Exception
    {
        return (getAllPublicationsResponse)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getAllPublications");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (getAllPublicationsResponse)getResult(getAllPublicationsResponse.class,__result,"getAllPublicationsResponse",__envelope);
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult<getAllPublicationsResponse>> getAllPublicationsAsync()
    {
        return executeAsync(new Functions.IFunc<getAllPublicationsResponse>() {
            public getAllPublicationsResponse Func() throws java.lang.Exception {
                return getAllPublications( );
            }
        });
    }
    
    public void createSubscription(final Long userId,final Long publicationId ) throws java.lang.Exception
    {
        execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "createSubscription");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="userId";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(userId);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="publicationId";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(publicationId);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return null;
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult< Void>> createSubscriptionAsync(final Long userId,final Long publicationId)
    {
        return executeAsync(new Functions.IFunc< Void>()
        {
            @Override
            public Void Func() throws java.lang.Exception {
                createSubscription( userId,publicationId);
                return null;
            }
        }) ;
    }
    
    public User getUser(final Long id ) throws java.lang.Exception
    {
        return (User)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getUser");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="id";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(id);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (User)getResult(User.class,__result,"return",__envelope);
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, OperationResult<User>> getUserAsync(final Long id)
    {
        return executeAsync(new Functions.IFunc<User>() {
            public User Func() throws java.lang.Exception {
                return getUser( id);
            }
        });
    }

    public void updateUser(final User User ) throws java.lang.Exception
    {
        execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                __envelope.addMapping("","User",new User().getClass());
                SoapObject __soapReq = new SoapObject("http://recycle/", "updateUser");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="User";
                __info.type=User.class;
                __info.setValue(User!=null?User:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return null;
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult< Void>> updateUserAsync(final User User)
    {
        return executeAsync(new Functions.IFunc< Void>()
        {
            @Override
            public Void Func() throws java.lang.Exception {
                updateUser( User);
                return null;
            }
        }) ;
    }

    public User validateUser(final String username,final String password ) throws java.lang.Exception
    {
        return (User)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "validateUser");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="username";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(username!=null?username:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="password";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(password!=null?password:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (User)getResult(User.class,__result,"return",__envelope);
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult< User>> validateUserAsync(final String username,final String password)
    {
        return executeAsync(new Functions.IFunc< User>() {
            public User Func() throws java.lang.Exception {
                return validateUser( username,password);
            }
        });
    }

    public Long createUser(final User user ) throws java.lang.Exception
    {
        return (Long)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                __envelope.addMapping("","user",new User().getClass());
                SoapObject __soapReq = new SoapObject("http://recycle/", "createUser");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="user";
                __info.type=User.class;
                __info.setValue(user!=null?user:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("return");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return new Long(j.toString());
                }
                else if (obj!= null && obj instanceof Long){
                    return (Long)obj;
                }
                return null;
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult< Long>> createUserAsync(final User user)
    {
        return executeAsync(new Functions.IFunc< Long>() {
            public Long Func() throws java.lang.Exception {
                return createUser( user);
            }
        });
    }

    public Publication getPublication(final Long id ) throws java.lang.Exception
    {
        return (Publication)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getPublication");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="id";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(id);
                __soapReq.addProperty(__info);
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (Publication)getResult(Publication.class,__result,"return",__envelope);
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult< Publication>> getPublicationAsync(final Long id)
    {
        return executeAsync(new Functions.IFunc< Publication>() {
            public Publication Func() throws java.lang.Exception {
                return getPublication( id);
            }
        });
    }

    public String saveImage(final String filename,final String filedata,final Long publicationId ) throws java.lang.Exception
    {
        return (String)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "saveImage");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="filename";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(filename!=null?filename:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="filedata";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(filedata!=null?filedata:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="";
                __info.name="publicationId";
                __info.type=PropertyInfo.LONG_CLASS;
                __info.setValue(publicationId);
                __soapReq.addProperty(__info);
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("return");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return j.toString();
                }
                else if (obj!= null && obj instanceof String){
                    return (String)obj;
                }
                return null;
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult< String>> saveImageAsync(final String filename,final String filedata,final Long publicationId)
    {
        return executeAsync(new Functions.IFunc< String>() {
            public String Func() throws java.lang.Exception {
                return saveImage( filename,filedata,publicationId);
            }
        });
    }

    public getAllCategoriesResponse getAllCategories( ) throws java.lang.Exception
    {
        return (getAllCategoriesResponse)execute(new IWcfMethod()
        {
            @Override
            public ExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                ExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://recycle/", "getAllCategories");
                __envelope.setOutputSoapObject(__soapReq);

                PropertyInfo __info=null;
                return __envelope;
            }

            @Override
            public java.lang.Object ProcessResult(ExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (getAllCategoriesResponse)getResult(getAllCategoriesResponse.class,__result,"getAllCategoriesResponse",__envelope);
            }
        },"");
    }

    public android.os.AsyncTask< Void, Void, OperationResult<getAllCategoriesResponse>> getAllCategoriesAsync()
    {
        return executeAsync(new Functions.IFunc<getAllCategoriesResponse>() {
            public getAllCategoriesResponse Func() throws java.lang.Exception {
                return getAllCategories( );
            }
        });
    }

    
    protected java.lang.Object execute(IWcfMethod wcfMethod,String methodName) throws java.lang.Exception
    {
        org.ksoap2.transport.Transport __httpTransport=createTransport();
        __httpTransport.debug=enableLogging;
        ExtendedSoapSerializationEnvelope __envelope=wcfMethod.CreateSoapEnvelope();
        try
        {
            sendRequest(methodName, __envelope, __httpTransport);
            
        }
        finally {
            if (__httpTransport.debug) {
                if (__httpTransport.requestDump != null) {
                    android.util.Log.i("requestDump",__httpTransport.requestDump);
                }
                if (__httpTransport.responseDump != null) {
                    android.util.Log.i("responseDump",__httpTransport.responseDump);
                }
            }
        }
        java.lang.Object __retObj = __envelope.bodyIn;
        if (__retObj instanceof org.ksoap2.SoapFault){
            org.ksoap2.SoapFault __fault = (org.ksoap2.SoapFault)__retObj;
            throw convertToException(__fault,__envelope);
        }else{
            return wcfMethod.ProcessResult(__envelope,__retObj);
        }
    }
    
    protected < T> android.os.AsyncTask< Void, Void, OperationResult< T>>  executeAsync(final Functions.IFunc< T> func)
    {
        return new android.os.AsyncTask< Void, Void, OperationResult< T>>()
        {
            @Override
            protected void onPreExecute() {
                callback.Starting();
            };
            @Override
            protected OperationResult< T> doInBackground(Void... params) {
                OperationResult< T> result = new OperationResult< T>();
                try
                {
                    result.Result= func.Func();
                }
                catch(java.lang.Exception ex)
                {
                    ex.printStackTrace();
                    result.Exception=ex;
                }
                return result;
            }
            
            @Override
            protected void onPostExecute(OperationResult< T> result)
            {
                callback.Completed(result);
            }
        }.execute();
    }
        
    java.lang.Exception convertToException(org.ksoap2.SoapFault fault, ExtendedSoapSerializationEnvelope envelope)
    {

        return new java.lang.Exception(fault.faultstring);
    }
}


