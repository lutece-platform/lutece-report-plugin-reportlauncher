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

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Parameter objects
 */
public final class ParameterHome
{
    // Static variable pointed at the DAO instance
    private static IParameterDAO _dao = SpringContextService.getBean( "reportlauncher.parameterDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "reportlauncher" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ParameterHome(  )
    {
    }

    /**
     * Create an instance of the parameter class
     * @param parameter The instance of the Parameter which contains the informations to store
     * @return The  instance of parameter which has been created with its primary key.
     */
    public static Parameter create( Parameter parameter )
    {
        _dao.insert( parameter, _plugin );

        return parameter;
    }

    /**
     * Update of the parameter which is specified in parameter
     * @param parameter The instance of the Parameter which contains the data to store
     * @return The instance of the  parameter which has been updated
     */
    public static Parameter update( Parameter parameter )
    {
        _dao.store( parameter, _plugin );

        return parameter;
    }

    /**
     * Remove the parameter whose identifier is specified in parameter
     * @param nKey The parameter Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a parameter whose identifier is specified in parameter
     * @param nKey The parameter primary key
     * @return an instance of Parameter
     */
    public static Collection<Parameter> findByIdReport( int nidReport )
    {
        return _dao.load( nidReport, _plugin );
    }

    /**
     * Load the data of all the parameter objects and returns them in form of a collection
     * @return the collection which contains the data of all the parameter objects
     */
    public static Collection<Parameter> getParametersList(  )
    {
        return _dao.selectParametersList( _plugin );
    }

    /**
     * Load the nIdReport of all the parameter objects and returns them in form of a collection
     * @return the collection which contains the id of all the parameter objects
     */
    public static Collection<Integer> getIdReportParametersList( int nIdReport )
    {
        return _dao.selectIdParametersList( nIdReport, _plugin );
    }
}
