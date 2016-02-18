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

import fr.paris.lutece.plugins.reportlauncher.business.ReportHome;
import fr.paris.lutece.plugns.reportlauncher.service.ReportLauncherService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Report xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "report", pageTitleI18nKey = "reportlauncher.xpage.report.pageTitle", pagePathI18nKey = "reportlauncher.xpage.report.pagePathLabel" )
public class ReportXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_REPORTS = "/skin/plugins/reportlauncher/manage_reports.html";

    // Markers
    private static final String MARK_REPORT_LIST = "report_list";
    private static final String PARAMETER_URL = "url";

    // Views
    private static final String VIEW_MANAGE_REPORTS = "manageReports";
    private ReportLauncherService _pageService;

    @View( value = VIEW_MANAGE_REPORTS, defaultView = true )
    public XPage getManageReports( HttpServletRequest request )
    {
        Map<String, Object> model = getModel(  );
        model.put( MARK_REPORT_LIST, ReportHome.getReportsList(  ) );
        _pageService = ( _pageService != null ) ? _pageService : ReportLauncherService.instance(  );
        model.put( PARAMETER_URL, _pageService.getFOUrl(  ) );

        return getXPage( TEMPLATE_MANAGE_REPORTS, request.getLocale(  ), model );
    }
}
