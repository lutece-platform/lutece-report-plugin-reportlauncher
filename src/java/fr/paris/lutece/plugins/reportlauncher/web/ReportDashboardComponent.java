package fr.paris.lutece.plugins.reportlauncher.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.reportlauncher.business.Report;
import fr.paris.lutece.plugins.reportlauncher.business.ReportHome;
import fr.paris.lutece.plugns.reportlauncher.service.ReportLauncherService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class ReportDashboardComponent extends DashboardComponent {

	
	 // MARKS
    private static final String MARK_URL = "url";
    private static final String MARK_REPORT_LIST = "report_list";
    private static final String VIEW_PERMISSIONS_FORM = "permissions";

    // TEMPALTES
    private static final String TEMPLATE_DASHBOARD = "/admin/plugins/reportlauncher/report_dashboard.html";
    
    
    private ReportLauncherService _pageService;
	@Override
	public String getDashboardData(AdminUser user, HttpServletRequest request) {
		
		    List<Report> listReports = (List<Report>) ReportHome.getReportsList(  );		    
	        Map<String, Object> model = new HashMap<String, Object>(  );

	        model.put( MARK_REPORT_LIST, listReports );
	        _pageService = ( _pageService != null ) ? _pageService : ReportLauncherService.instance(  );
	        model.put( MARK_URL, _pageService.getBOUrl(  ) );
	                
	        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD,
	                AdminUserService.getLocale( request ), model );

	        return template.getHtml(  );
	}

}
