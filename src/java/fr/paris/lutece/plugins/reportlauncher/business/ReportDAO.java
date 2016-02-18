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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Report objects
 */
public final class ReportDAO implements IReportDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_report ) FROM reportlauncher_report";
    private static final String SQL_QUERY_SELECT = "SELECT id_report, name, page FROM reportlauncher_report WHERE id_report = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO reportlauncher_report ( id_report, name, page) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM reportlauncher_report WHERE id_report = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE reportlauncher_report SET id_report = ?, name = ?, page = ? WHERE id_report = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_report, name, page FROM reportlauncher_report";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_report FROM reportlauncher_report";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Report insert( Report report, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        report.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, report.getId(  ) );
        daoUtil.setString( 2, report.getName(  ) );
        daoUtil.setString( 3, report.getPage(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return report;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Report load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        Report report = null;

        if ( daoUtil.next(  ) )
        {
            report = new Report(  );
            report.setId( daoUtil.getInt( 1 ) );
            report.setName( daoUtil.getString( 2 ) );
            report.setPage( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return report;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Report report, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, report.getId(  ) );
        daoUtil.setString( 2, report.getName(  ) );
        daoUtil.setString( 3, report.getPage(  ) );
        daoUtil.setInt( 4, report.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Report> selectReportsList( Plugin plugin )
    {
        Collection<Report> reportList = new ArrayList<Report>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Report report = new Report(  );

            report.setId( daoUtil.getInt( 1 ) );
            report.setName( daoUtil.getString( 2 ) );
            report.setPage( daoUtil.getString( 3 ) );

            reportList.add( report );
        }

        daoUtil.free(  );

        return reportList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdReportsList( Plugin plugin )
    {
        Collection<Integer> reportList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            reportList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return reportList;
    }
}
