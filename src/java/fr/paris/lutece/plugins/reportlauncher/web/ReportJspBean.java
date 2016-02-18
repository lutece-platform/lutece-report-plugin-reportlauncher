/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.reportlauncher.web;

import fr.paris.lutece.plugins.reportlauncher.business.Parameter;
import fr.paris.lutece.plugins.reportlauncher.business.ParameterHome;
import fr.paris.lutece.plugins.reportlauncher.business.Report;
import fr.paris.lutece.plugins.reportlauncher.business.ReportHome;
import fr.paris.lutece.plugns.reportlauncher.service.ReportLauncherService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Report features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageReports.jsp", controllerPath = "jsp/admin/plugins/reportlauncher/", right = "REPORTLAUNCHER_MANAGEMENT" )
public class ReportJspBean extends ManageReportlauncherJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_REPORTS = "/admin/plugins/reportlauncher/manage_reports.html";
    private static final String TEMPLATE_CREATE_REPORT = "/admin/plugins/reportlauncher/create_report.html";
    private static final String TEMPLATE_MODIFY_REPORT = "/admin/plugins/reportlauncher/modify_report.html";

    //Message
    private static final String MESSAGE_CONFIRM_REMOVE_PARAMETER = "reportlauncher.message.confirmRemoveParameter";

    // Parameters
    private static final String PARAMETER_ID_REPORT = "id";
    private static final String PARAMETER_URL = "url";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_REPORTS = "reportlauncher.manage_reports.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_REPORT = "reportlauncher.modify_report.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_REPORT = "reportlauncher.create_report.pageTitle";

    // Markers
    private static final String MARK_REPORT_LIST = "report_list";
    private static final String MARK_REPORT = "report";
    private static final String MARK_PAGE_LIST = "page_list";
    private static final String JSP_MANAGE_REPORTS = "jsp/admin/plugins/reportlauncher/ManageReports.jsp";

    //Parameter
    private static final String PARAMETER_NAME = "parameterName";
    private static final String PARAMETER_VALUE = "parameterValue";
    private static final String PARAMETER_ID_PARAMETER = "idParam";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_REPORT = "reportlauncher.message.confirmRemoveReport";
    private static final String PROPERTY_DEFAULT_LIST_REPORT_PER_PAGE = "reportlauncher.listReports.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "reportlauncher.model.entity.report.attribute.";

    // Views
    private static final String VIEW_MANAGE_REPORTS = "manageReports";
    private static final String VIEW_CREATE_REPORT = "createReport";
    private static final String VIEW_MODIFY_REPORT = "modifyReport";

    // Actions
    private static final String ACTION_CREATE_REPORT = "createReport";
    private static final String ACTION_MODIFY_REPORT = "modifyReport";
    private static final String ACTION_REMOVE_REPORT = "removeReport";
    private static final String ACTION_CONFIRM_REMOVE_REPORT = "confirmRemoveReport";
    private static final String ACTION_ADD_PARAM = "addParameter";
    private static final String ACTION_MOD_PARAM = "modifyParameter";
    private static final String ACTION_REMOVE_PARAMETER = "removeParameter";
    private static final String ACTION_CONFIRM_REMOVE_PARAMETER = "confirmRemoveParameter";

    // Infos
    private static final String INFO_REPORT_CREATED = "reportlauncher.info.report.created";
    private static final String INFO_REPORT_UPDATED = "reportlauncher.info.report.updated";
    private static final String INFO_REPORT_REMOVED = "reportlauncher.info.report.removed";

    // Session variable to store working values
    private Report _report;
    private ReportLauncherService _pageService;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_REPORTS, defaultView = true )
    public String getManageReports( HttpServletRequest request )
    {
        _report = null;

        List<Report> listReports = (List<Report>) ReportHome.getReportsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_REPORT_LIST, listReports, JSP_MANAGE_REPORTS );
        _pageService = ( _pageService != null ) ? _pageService : ReportLauncherService.instance(  );
        model.put(PARAMETER_URL, _pageService.getBOUrl( ));
        
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_REPORTS, TEMPLATE_MANAGE_REPORTS, model );
    }

    /**
     * Returns the form to create a report
     *
     * @param request The Http request
     * @return the html code of the report form
     */
    @View( VIEW_CREATE_REPORT )
    public String getCreateReport( HttpServletRequest request )
    {
        _report = ( _report != null ) ? _report : new Report(  );
        _pageService = ( _pageService != null ) ? _pageService : ReportLauncherService.instance(  );
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_REPORT, _report );
        model.put(MARK_PAGE_LIST, _pageService.getPage( getUser( ) )); 
        return getPage( PROPERTY_PAGE_TITLE_CREATE_REPORT, TEMPLATE_CREATE_REPORT, model );
    }

    /**
     * Process the data capture form of a new report
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_REPORT )
    public String doCreateReport( HttpServletRequest request )
    {
        List<Parameter> listParam = new ArrayList<Parameter>(  );
        populate( _report, request );
        _report.setParameter( listParam );

        // Check constraints
        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPORT );
        }

        ReportHome.create( _report );

        addInfo( INFO_REPORT_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Manages the removal form of a report whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_REPORT )
    public String getConfirmRemoveReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPORT ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_REPORT ) );
        url.addParameter( PARAMETER_ID_REPORT, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_REPORT,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a report
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage reports
     */
    @Action( ACTION_REMOVE_REPORT )
    public String doRemoveReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPORT ) );
        Report report = ReportHome.findByPrimaryKey( nId );
        ReportHome.remove( report );
        addInfo( INFO_REPORT_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Returns the form to update info about a report
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_REPORT )
    public String getModifyReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPORT ) );
        _report = ReportHome.findByPrimaryKey( nId );
        _pageService = ( _pageService != null ) ? _pageService : ReportLauncherService.instance(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_REPORT, _report );
        model.put(MARK_PAGE_LIST, _pageService.getPage( getUser( ) )); 
        
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_REPORT, TEMPLATE_MODIFY_REPORT, model );
    }

    /**
     * Process the change form of a report
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_REPORT )
    public String doModifyReport( HttpServletRequest request )
    {
        List<Parameter> listParam = new ArrayList<Parameter>(  );
        populate( _report, request );
        _report.setParameter( listParam );

        // Check constraints
        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId(  ) );
        }

        ReportHome.update( _report );
        addInfo( INFO_REPORT_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Handles the removal form of a parameter
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage parameters
     */
    @Action( ACTION_REMOVE_PARAMETER )
    public String doRemoveParameter( HttpServletRequest request )
    {
        int nIdreport = Integer.parseInt( request.getParameter( PARAMETER_ID_REPORT ) );
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PARAMETER ) );
        ParameterHome.remove( nId );

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, nIdreport );

        //   return redirectView( request, VIEW_MANAGE_PARAMETERS );
    }

    /**
     * Manages the removal form of a parameter whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_PARAMETER )
    public String getConfirmRemoveParameter( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PARAMETER ) );
        int nIdReport = Integer.parseInt( request.getParameter( PARAMETER_ID_REPORT ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PARAMETER ) );
        url.addParameter( PARAMETER_ID_PARAMETER, nId );
        url.addParameter( PARAMETER_ID_REPORT, nIdReport );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PARAMETER,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Returns the form to update info about a parameter
     * @param request
     * @return
     */
    @Action( ACTION_MOD_PARAM )
    public String doModifyParamter( HttpServletRequest request )
    {
        List<Parameter> listParam = new ArrayList<Parameter>(  );
        String paramName = request.getParameter( PARAMETER_NAME );
        String paramValue = request.getParameter( PARAMETER_VALUE );
        Parameter param = new Parameter(  );
        populate( _report, request );

        if ( ( paramName != null ) && ( paramValue != null ) && !paramName.isEmpty(  ) && !paramValue.isEmpty(  ) )
        {
            param.setName( paramName );
            param.setValue( paramValue );
            listParam.add( param );
        }

        _report.setParameter( listParam );

        // Check constraints
        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId(  ) );
        }

        ReportHome.update( _report );

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId(  ) );
    }

    /**
     *   Process the data capture form of a new parameter
     * @param request
     * @return
     */
    @Action( ACTION_ADD_PARAM )
    public String doAddParamter( HttpServletRequest request )
    {
        List<Parameter> listParam = new ArrayList<Parameter>(  );
        String paramName = request.getParameter( PARAMETER_NAME );
        String paramValue = request.getParameter( PARAMETER_VALUE );
        Parameter param = new Parameter(  );
        populate( _report, request );

        if ( ( paramName != null ) && ( paramValue != null ) && !paramName.isEmpty(  ) && !paramValue.isEmpty(  ) )
        {
            param.setName( paramName );
            param.setValue( paramValue );
            listParam.add( param );
        }

        _report.setParameter( listParam );

        // Check constraints
        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPORT );
        }

        Report report = ReportHome.create( _report );

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, report.getId(  ) );
    }
}
