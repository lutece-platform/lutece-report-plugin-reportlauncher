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
package fr.paris.lutece.plugins.reportlauncher.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.TransactionManager;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Report objects
 */
public final class ReportHome
{
    // Static variable pointed at the DAO instance
    private static IReportDAO _dao = SpringContextService.getBean( "reportlauncher.reportDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "reportlauncher" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReportHome(  )
    {
    }

    /**
     * Create an instance of the report class
     * @param report The instance of the Report which contains the informations to store
     * @return The  instance of report which has been created with its primary key.
     */
    public static Report create( Report report )
    {
        TransactionManager.beginTransaction( _plugin );

        try
        {
            report = _dao.insert( report, _plugin );

            for ( Parameter param : report.getParameter(  ) )
            {
                param.setIdReport( report.getId(  ) );
                ParameterHome.create( param );
            }

            TransactionManager.commitTransaction( _plugin );
        }
        catch ( Exception e )
        {
            TransactionManager.rollBack( _plugin );
            throw new AppException( e.getMessage(  ), e );
        }

        return report;
    }

    /**
     * Update of the report which is specified in parameter
     * @param report The instance of the Report which contains the data to store
     * @return The instance of the  report which has been updated
     */
    public static Report update( Report report )
    {
        TransactionManager.beginTransaction( _plugin );

        try
        {
            Collection<Integer> listIdReportParam = ParameterHome.getIdReportParametersList( report.getId(  ) );
            _dao.store( report, _plugin );

            for ( Parameter param : report.getParameter(  ) )
            {
                param.setIdReport( report.getId(  ) );

                if ( param.getId(  ) == 0 )
                {
                    ParameterHome.create( param );
                }
                else if ( listIdReportParam.contains( param.getId(  ) ) )
                {
                    ParameterHome.update( param );
                }
                else
                {
                    ParameterHome.remove( param.getId(  ) );
                }
            }

            TransactionManager.commitTransaction( _plugin );
        }
        catch ( Exception e )
        {
            TransactionManager.rollBack( _plugin );
            throw new AppException( e.getMessage(  ), e );
        }

        return report;
    }

    /**
     * Remove the report whose identifier is specified in parameter
     * @param nKey The report Id
     */
    public static void remove( Report report )
    {
        TransactionManager.beginTransaction( _plugin );

        try
        {
            _dao.delete( report.getId(  ), _plugin );
            ParameterHome.remove( report.getId(  ) );

            TransactionManager.commitTransaction( _plugin );
        }
        catch ( Exception e )
        {
            TransactionManager.rollBack( _plugin );
            throw new AppException( e.getMessage(  ), e );
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a report whose identifier is specified in parameter
     * @param nKey The report primary key
     * @return an instance of Report
     */
    public static Report findByPrimaryKey( int nKey )
    {
        Report report = _dao.load( nKey, _plugin );

        report.setParameter( (List<Parameter>) ParameterHome.findByIdReport( nKey ) );

        return report;
    }

    /**
     * Load the data of all the report objects and returns them in form of a collection
     * @return the collection which contains the data of all the report objects
     */
    public static Collection<Report> getReportsList(  )
    {
        Collection<Report> reportList = _dao.selectReportsList( _plugin );

        for ( Report rp : reportList )
        {
            rp.setParameter( (List<Parameter>) ParameterHome.findByIdReport( rp.getId(  ) ) );
        }

        return reportList;
    }

    /**
     * Load the id of all the report objects and returns them in form of a collection
     * @return the collection which contains the id of all the report objects
     */
    public static Collection<Integer> getIdReportsList(  )
    {
        return _dao.selectIdReportsList( _plugin );
    }
}
