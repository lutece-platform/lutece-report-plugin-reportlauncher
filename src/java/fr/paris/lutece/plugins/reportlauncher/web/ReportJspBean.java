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
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;
import fr.paris.lutece.util.url.UrlItem;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides the user interface to manage Report features ( manage, create, modify, remove )
 */
@SessionScoped
@Named( "reportlauncherReportJspBean" )
@Controller( controllerJsp = ReportJspBean.CONTROLLER_JSP, controllerPath = ReportJspBean.CONTROLLER_PATH, right = ReportJspBean.RIGHT_MANAGEREPORTLAUNCHER, securityTokenEnabled = true )
public class ReportJspBean extends MVCAdminJspBean
{
    public static final String RIGHT_MANAGEREPORTLAUNCHER = "REPORTLAUNCHER_MANAGEMENT";
    static final String CONTROLLER_JSP = "ManageReports.jsp";
    static final String CONTROLLER_PATH = "jsp/admin/plugins/reportlauncher/";
    private static final long serialVersionUID = 1L;

    // templates
    private static final String TEMPLATE_MANAGE_REPORTS = "/admin/plugins/reportlauncher/manage_reports.html";
    private static final String TEMPLATE_CREATE_REPORT = "/admin/plugins/reportlauncher/create_report.html";
    private static final String TEMPLATE_MODIFY_REPORT = "/admin/plugins/reportlauncher/modify_report.html";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_PARAMETER = "reportlauncher.message.confirmRemoveParameter";
    private static final String MESSAGE_CONFIRM_REMOVE_REPORT = "reportlauncher.message.confirmRemoveReport";
    private static final String MESSAGE_REPORT_NOT_FOUND = "reportlauncher.message.reportNotFound";
    private static final String MESSAGE_PARAMETER_NOT_FOUND = "reportlauncher.message.parameterNotFound";

    // Parameters
    private static final String PARAMETER_ID_REPORT = "id";
    private static final String PARAMETER_NAME = "parameterName";
    private static final String PARAMETER_VALUE = "parameterValue";
    private static final String PARAMETER_ID_PARAMETER = "idParam";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_MANAGE_REPORTS = "reportlauncher.manage_reports.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_REPORT = "reportlauncher.modify_report.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_REPORT = "reportlauncher.create_report.pageTitle";
    private static final String PROPERTY_ITEMS_PER_PAGE = "reportlauncher.listItems.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "reportlauncher.model.entity.report.attribute.";

    // Markers
    private static final String MARK_REPORT_LIST = "report_list";
    private static final String MARK_REPORT = "report";
    private static final String MARK_PAGE_LIST = "page_list";
    private static final String MARK_URL = "url";

    // Views
    private static final String VIEW_MANAGE_REPORTS = "manageReports";
    private static final String VIEW_CREATE_REPORT = "createReport";
    private static final String VIEW_MODIFY_REPORT = "modifyReport";
    private static final String VIEW_CONFIRM_REMOVE_REPORT = "confirmRemoveReport";
    private static final String VIEW_CONFIRM_REMOVE_PARAMETER = "confirmRemoveParameter";

    // Actions
    private static final String ACTION_CREATE_REPORT = "createReport";
    private static final String ACTION_MODIFY_REPORT = "modifyReport";
    private static final String ACTION_REMOVE_REPORT = "removeReport";
    private static final String ACTION_ADD_PARAM = "addParameter";
    private static final String ACTION_MOD_PARAM = "modifyParameter";
    private static final String ACTION_REMOVE_PARAMETER = "removeParameter";

    // Infos
    private static final String INFO_REPORT_CREATED = "reportlauncher.info.report.created";
    private static final String INFO_REPORT_UPDATED = "reportlauncher.info.report.updated";
    private static final String INFO_REPORT_REMOVED = "reportlauncher.info.report.removed";

    @Inject
    private ReportLauncherService _pageService;

    @Inject
    @Pager( name = "reportlauncher.reports", listBookmark = MARK_REPORT_LIST, defaultItemsPerPage = PROPERTY_ITEMS_PER_PAGE, baseUrl = CONTROLLER_PATH + CONTROLLER_JSP )
    private IPager<Report, Report> _pager;

    private Report _report;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @param model The model
     * @return The page
     */
    @View( value = VIEW_MANAGE_REPORTS, defaultView = true )
    public String getManageReports( HttpServletRequest request, Models model )
    {
        _report = null;

        List<Report> listReports = new ArrayList<>( ReportHome.getReportsList( ) );
        _pager.withListItem( listReports ).populateModels( request, model, getLocale( ) );
        model.put( MARK_URL, _pageService.getBOUrl( ) );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_REPORTS, TEMPLATE_MANAGE_REPORTS, model );
    }

    /**
     * Returns the form to create a report
     * @param model The model
     * @return the html code of the report form
     */
    @View( VIEW_CREATE_REPORT )
    public String getCreateReport( Models model )
    {
        _report = ( ( _report != null ) && ( _report.getId( ) == 0 ) ) ? _report : new Report( );

        model.put( MARK_REPORT, _report );
        model.put( MARK_PAGE_LIST, _pageService.getPage( getUser( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_REPORT, TEMPLATE_CREATE_REPORT, model );
    }

    /**
     * Process the data capture form of a new report
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_REPORT )
    public String doCreateReport( HttpServletRequest request )
    {
        _report = ( ( _report != null ) && ( _report.getId( ) == 0 ) ) ? _report : new Report( );
        populate( _report, request );
        _report.setParameter( new ArrayList<>( ) );

        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPORT );
        }

        ReportHome.create( _report );
        addInfo( INFO_REPORT_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Process the data capture form of a new report with a first parameter
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( value = ACTION_ADD_PARAM, securityTokenAction = ACTION_CREATE_REPORT )
    public String doAddParameter( HttpServletRequest request )
    {
        _report = ( ( _report != null ) && ( _report.getId( ) == 0 ) ) ? _report : new Report( );
        populate( _report, request );
        _report.setParameter( readParameter( request ) );

        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPORT );
        }

        Report report = ReportHome.create( _report );

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, report.getId( ) );
    }

    /**
     * Manages the removal form of a report whose identifier is in the http request
     * @param request The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_REPORT, securityTokenAction = ACTION_REMOVE_REPORT )
    public String getConfirmRemoveReport( HttpServletRequest request )
    {
        Report report = ReportHome.findByPrimaryKey( NumberUtils.toInt( request.getParameter( PARAMETER_ID_REPORT ), -1 ) );

        if ( report == null )
        {
            return redirectReportNotFound( request );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_REPORT ) );
        url.addParameter( PARAMETER_ID_REPORT, report.getId( ) );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_REPORT, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a report
     * @param request The Http request
     * @return the jsp URL to display the form to manage reports
     */
    @Action( ACTION_REMOVE_REPORT )
    public String doRemoveReport( HttpServletRequest request )
    {
        Report report = ReportHome.findByPrimaryKey( NumberUtils.toInt( request.getParameter( PARAMETER_ID_REPORT ), -1 ) );

        if ( report != null )
        {
            ReportHome.remove( report );
            addInfo( INFO_REPORT_REMOVED, getLocale( ) );
        }

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Returns the form to update info about a report
     * @param request The Http request
     * @param model The model
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_REPORT )
    public String getModifyReport( HttpServletRequest request, Models model )
    {
        Report report = ReportHome.findByPrimaryKey( NumberUtils.toInt( request.getParameter( PARAMETER_ID_REPORT ), -1 ) );

        if ( report == null )
        {
            return redirectReportNotFound( request );
        }

        _report = report;
        model.put( MARK_REPORT, _report );
        model.put( MARK_PAGE_LIST, _pageService.getPage( getUser( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_REPORT, TEMPLATE_MODIFY_REPORT, model );
    }

    /**
     * Process the change form of a report
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_REPORT )
    public String doModifyReport( HttpServletRequest request )
    {
        if ( _report == null )
        {
            return redirectView( request, VIEW_MANAGE_REPORTS );
        }

        populateReport( request );
        _report.setParameter( new ArrayList<>( ) );

        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId( ) );
        }

        ReportHome.update( _report );
        addInfo( INFO_REPORT_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_REPORTS );
    }

    /**
     * Process the change form of a report and adds the parameter typed in the form
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( value = ACTION_MOD_PARAM, securityTokenAction = ACTION_MODIFY_REPORT )
    public String doModifyParameter( HttpServletRequest request )
    {
        if ( _report == null )
        {
            return redirectView( request, VIEW_MANAGE_REPORTS );
        }

        populateReport( request );
        _report.setParameter( readParameter( request ) );

        if ( !validateBean( _report, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId( ) );
        }

        ReportHome.update( _report );

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, _report.getId( ) );
    }

    /**
     * Manages the removal form of a parameter whose identifier is in the http request
     * @param request The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_PARAMETER, securityTokenAction = ACTION_REMOVE_PARAMETER )
    public String getConfirmRemoveParameter( HttpServletRequest request )
    {
        int nIdReport = NumberUtils.toInt( request.getParameter( PARAMETER_ID_REPORT ), -1 );
        int nIdParameter = NumberUtils.toInt( request.getParameter( PARAMETER_ID_PARAMETER ), -1 );

        if ( !ParameterHome.getIdReportParametersList( nIdReport ).contains( nIdParameter ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_PARAMETER_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PARAMETER ) );
        url.addParameter( PARAMETER_ID_PARAMETER, nIdParameter );
        url.addParameter( PARAMETER_ID_REPORT, nIdReport );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PARAMETER, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a parameter
     * @param request The Http request
     * @return the jsp URL to display the form to modify the report
     */
    @Action( ACTION_REMOVE_PARAMETER )
    public String doRemoveParameter( HttpServletRequest request )
    {
        int nIdReport = NumberUtils.toInt( request.getParameter( PARAMETER_ID_REPORT ), -1 );
        int nIdParameter = NumberUtils.toInt( request.getParameter( PARAMETER_ID_PARAMETER ), -1 );

        if ( ParameterHome.getIdReportParametersList( nIdReport ).contains( nIdParameter ) )
        {
            ParameterHome.remove( nIdParameter );
        }

        return redirect( request, VIEW_MODIFY_REPORT, PARAMETER_ID_REPORT, nIdReport );
    }

    /**
     * Populates the report being modified from the request, keeping its identifier
     * @param request The Http request
     */
    private void populateReport( HttpServletRequest request )
    {
        int nId = _report.getId( );
        populate( _report, request );
        _report.setId( nId );
    }

    /**
     * Reads the parameter typed in the report form
     * @param request The Http request
     * @return a list holding the parameter, empty when its name or its value is missing
     */
    private static List<Parameter> readParameter( HttpServletRequest request )
    {
        List<Parameter> listParam = new ArrayList<>( );
        String strName = request.getParameter( PARAMETER_NAME );
        String strValue = request.getParameter( PARAMETER_VALUE );

        if ( ( strName != null ) && ( strValue != null ) && !strName.isEmpty( ) && !strValue.isEmpty( ) )
        {
            Parameter param = new Parameter( );
            param.setName( strName );
            param.setValue( strValue );
            listParam.add( param );
        }

        return listParam;
    }

    /**
     * Redirects to the message telling the report does not exist
     * @param request The Http request
     * @return the message URL
     */
    private String redirectReportNotFound( HttpServletRequest request )
    {
        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_REPORT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
    }
}
