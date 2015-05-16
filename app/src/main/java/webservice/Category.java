package webservice;

/**
 * Created by Parsa on 16/05/2015.
 */
public class Category {

    private String _categoryName;
    private int _id;

    public void setCategotyName (String cat)
    {
        this._categoryName = cat;
    }

    public String getCategoryName ()
    {
        return _categoryName;
    }

    public void setId (int id)
    {
        this._id = id;
    }

    public int getId ()
    {
        return _id;
    }
}
