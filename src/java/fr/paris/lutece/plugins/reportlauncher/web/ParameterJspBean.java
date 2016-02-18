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
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Parameter features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageParameters.jsp", controllerPath = "jsp/admin/plugins/reportlauncher/", right = "REPORTLAUNCHER_MANAGEMENT" )
public class ParameterJspBean extends ManageReportlauncherJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_PARAMETERS = "/admin/plugins/reportlauncher/manage_parameters.html";
    private static final String TEMPLATE_CREATE_PARAMETER = "/admin/plugins/reportlauncher/create_parameter.html";
    private static final String TEMPLATE_MODIFY_PARAMETER = "/admin/plugins/reportlauncher/modify_parameter.html";

    // Parameters
    private static final String PARAMETER_ID_PARAMETER = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PARAMETERS = "reportlauncher.manage_parameters.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PARAMETER = "reportlauncher.modify_parameter.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PARAMETER = "reportlauncher.create_parameter.pageTitle";

    // Markers
    private static final String MARK_PARAMETER_LIST = "parameter_list";
    private static final String MARK_PARAMETER = "parameter";
    private static final String JSP_MANAGE_PARAMETERS = "jsp/admin/plugins/reportlauncher/ManageParameters.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_PARAMETER = "reportlauncher.message.confirmRemoveParameter";
    private static final String PROPERTY_DEFAULT_LIST_PARAMETER_PER_PAGE = "reportlauncher.listParameters.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "reportlauncher.model.entity.parameter.attribute.";

    // Views
    private static final String VIEW_MANAGE_PARAMETERS = "manageParameters";
    private static final String VIEW_CREATE_PARAMETER = "createParameter";
    private static final String VIEW_MODIFY_PARAMETER = "modifyParameter";

    // Actions
    private static final String ACTION_CREATE_PARAMETER = "createParameter";
    private static final String ACTION_MODIFY_PARAMETER = "modifyParameter";
    private static final String ACTION_REMOVE_PARAMETER = "removeParameter";
    private static final String ACTION_CONFIRM_REMOVE_PARAMETER = "confirmRemoveParameter";

    // Infos
    private static final String INFO_PARAMETER_CREATED = "reportlauncher.info.parameter.created";
    private static final String INFO_PARAMETER_UPDATED = "reportlauncher.info.parameter.updated";
    private static final String INFO_PARAMETER_REMOVED = "reportlauncher.info.parameter.removed";

    // Session variable to store working values
    private Parameter _parameter;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PARAMETERS, defaultView = true )
    public String getManageParameters( HttpServletRequest request )
    {
        _parameter = null;

        List<Parameter> listParameters = (List<Parameter>) ParameterHome.getParametersList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_PARAMETER_LIST, listParameters,
                JSP_MANAGE_PARAMETERS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PARAMETERS, TEMPLATE_MANAGE_PARAMETERS, model );
    }

    /**
     * Returns the form to create a parameter
     *
     * @param request The Http request
     * @return the html code of the parameter form
     */
    @View( VIEW_CREATE_PARAMETER )
    public String getCreateParameter( HttpServletRequest request )
    {
        _parameter = ( _parameter != null ) ? _parameter : new Parameter(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_PARAMETER, _parameter );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PARAMETER, TEMPLATE_CREATE_PARAMETER, model );
    }

    /**
     * Process the data capture form of a new parameter
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_PARAMETER )
    public String doCreateParameter( HttpServletRequest request )
    {
        populate( _parameter, request );

        // Check constraints
        if ( !validateBean( _parameter, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_PARAMETER );
        }

        ParameterHome.create( _parameter );
        addInfo( INFO_PARAMETER_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PARAMETERS );
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
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PARAMETER ) );
        url.addParameter( PARAMETER_ID_PARAMETER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PARAMETER,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
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
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PARAMETER ) );
        ParameterHome.remove( nId );
        addInfo( INFO_PARAMETER_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PARAMETERS );
    }

    /**
     * Returns the form to update info about a parameter
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PARAMETER )
    public String getModifyParameter( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PARAMETER ) );

        if ( ( _parameter == null ) || ( _parameter.getId(  ) != nId ) )
        {
            //_parameter = ParameterHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_PARAMETER, _parameter );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PARAMETER, TEMPLATE_MODIFY_PARAMETER, model );
    }

    /**
     * Process the change form of a parameter
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_PARAMETER )
    public String doModifyParameter( HttpServletRequest request )
    {
        populate( _parameter, request );

        // Check constraints
        if ( !validateBean( _parameter, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_PARAMETER, PARAMETER_ID_PARAMETER, _parameter.getId(  ) );
        }

        ParameterHome.update( _parameter );
        addInfo( INFO_PARAMETER_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PARAMETERS );
    }
}
