package fr.paris.lutece.plugins.reportlauncher.business.dto;

public class PageDTO {
	
	   private String _strName;
	   private String _strValue;

	    /**
	     * Returns the Name
	     * @return The Name
	     */
	    public String getName(  )
	    {
	        return _strName;
	    }

	    /**
	     * Sets the Name
	     * @param strName The Name
	     */
	    public void setName( String strName )
	    {
	        _strName = strName;
	    }

	    /**
	     * Returns the Value
	     * @return The Value
	     */
	    public String getValue(  )
	    {
	        return _strValue;
	    }

	    /**
	     * Sets the Value
	     * @param strValue The Value
	     */
	    public void setValue( String strValue )
	    {
	        _strValue = strValue;
	    }

}
