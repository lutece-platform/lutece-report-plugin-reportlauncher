package fr.paris.lutece.plugns.reportlauncher.service;

import java.util.List;

import fr.paris.lutece.plugins.reportlauncher.business.dto.PageDTO;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class ReportLauncherService {

	    private static final String BEAN_PAGE_SERVICE = "reportlauncher.pageService";
	    private static ReportLauncherService _singleton;
	    private static IReportLauncherService _pageService;

	    /** private constructor */
	    private ReportLauncherService(  )
	    {
	    }

	    /**
	     * Returns the unique instance
	     * @return The unique instance
	     */
	    public static ReportLauncherService instance(  )
	    {
	        if ( _singleton == null )
	        {
	            _singleton = new ReportLauncherService(  );
	            _pageService = SpringContextService.getBean( BEAN_PAGE_SERVICE );
	        }

	        return _singleton;
	    }
	    
	    /**
	     * Returns the PageDTO pages
	     */
	    public List<PageDTO> getPage( AdminUser user  )
	    {
	    	return _pageService.getPage( user );
	    	
	    }
	    
	   /**
	    * Returns Back URL
	    * @return Back url
	    */
	    public String getBOUrl(  )
	    {
	    	return _pageService.getBOUrl( );
	    }
	    
	    /**
		 * Returns Front URL
		 * @return Front url
		 */
		  public String getFOUrl(  )
		  {
		   	return _pageService.getFOUrl( );
		  }
}
