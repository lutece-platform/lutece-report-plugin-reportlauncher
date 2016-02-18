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
import java.util.List;


/**
 * This class provides Data Access methods for Parameter objects
 */
public final class ParameterDAO implements IParameterDAO
{
    // Constants
    private static final String SQL_QUERY_SELECTALL_PARAM = "SELECT id_parameter, id_report, name, value FROM reportlauncher_parameter";
    private static final String SQL_QUERY_SELECTALL_ID_PARAM = "SELECT id_report FROM reportlauncher_parameter where id_report = ? ";
    private static final String SQL_QUERY_NEW_PK_PARAM = "SELECT max( id_parameter ) FROM reportlauncher_parameter";
    private static final String SQL_QUERY_INSERT_PARAM = "INSERT INTO reportlauncher_parameter ( id_parameter, id_report, name, value ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_PARAM = "SELECT id_parameter, id_report, name, value FROM reportlauncher_parameter WHERE id_report = ?";
    private static final String SQL_QUERY_DELETE_PARAM = "DELETE FROM reportlauncher_parameter WHERE id_parameter = ? ";
    private static final String SQL_QUERY_UPDATE_PARAM = "UPDATE reportlauncher_ SET id_parameter = ?, name = ?, value = ? WHERE id_parameter = ?";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_PARAM, plugin );
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
    public void insert( Parameter parameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PARAM, plugin );

        parameter.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, parameter.getId(  ) );
        daoUtil.setInt( 2, parameter.getIdReport(  ) );
        daoUtil.setString( 3, parameter.getName(  ) );
        daoUtil.setString( 4, parameter.getValue(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Parameter> load( int nidReport, Plugin plugin )
    {
        Collection<Parameter> parameterList = new ArrayList<Parameter>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARAM, plugin );
        daoUtil.setInt( 1, nidReport );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Parameter parameter = new Parameter(  );

            parameter.setId( daoUtil.getInt( 1 ) );
            parameter.setIdReport( daoUtil.getInt( 2 ) );

            parameter.setName( daoUtil.getString( 3 ) );
            parameter.setValue( daoUtil.getString( 4 ) );

            parameterList.add( parameter );
        }

        daoUtil.free(  );

        return parameterList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PARAM, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Parameter parameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PARAM, plugin );

        daoUtil.setInt( 1, parameter.getId(  ) );
        daoUtil.setInt( 2, parameter.getIdReport(  ) );

        daoUtil.setString( 3, parameter.getName(  ) );
        daoUtil.setString( 4, parameter.getValue(  ) );
        daoUtil.setInt( 5, parameter.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Parameter> selectParametersList( Plugin plugin )
    {
        Collection<Parameter> parameterList = new ArrayList<Parameter>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_PARAM, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Parameter parameter = new Parameter(  );

            parameter.setId( daoUtil.getInt( 1 ) );
            parameter.setIdReport( daoUtil.getInt( 2 ) );

            parameter.setName( daoUtil.getString( 3 ) );
            parameter.setValue( daoUtil.getString( 4 ) );

            parameterList.add( parameter );
        }

        daoUtil.free(  );

        return parameterList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdParametersList( int nidReport, Plugin plugin )
    {
        Collection<Integer> parameterList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID_PARAM, plugin );
        daoUtil.setInt( 1, nidReport );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            parameterList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return parameterList;
    }
}
