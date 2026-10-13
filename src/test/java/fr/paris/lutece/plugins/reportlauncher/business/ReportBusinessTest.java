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

import fr.paris.lutece.test.LuteceTestCase;

import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Report business test
 */
public class ReportBusinessTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String PAGE1 = "Page1";
    private static final String PAGE2 = "Page2";
    private static final String PARAM_NAME = "year";
    private static final String PARAM_VALUE = "2024";

    /**
     * Creates, reads, updates and removes a report and its parameter
     */
    @Test
    public void testBusiness( )
    {
        Report report = new Report( );
        report.setName( NAME1 );
        report.setPage( PAGE1 );

        List<Parameter> listParam = new ArrayList<>( );
        Parameter param = new Parameter( );
        param.setName( PARAM_NAME );
        param.setValue( PARAM_VALUE );
        listParam.add( param );
        report.setParameter( listParam );

        ReportHome.create( report );

        Report reportStored = ReportHome.findByPrimaryKey( report.getId( ) );
        assertEquals( NAME1, reportStored.getName( ) );
        assertEquals( PAGE1, reportStored.getPage( ) );
        assertEquals( 1, reportStored.getParameter( ).size( ) );
        assertEquals( PARAM_NAME, reportStored.getParameter( ).get( 0 ).getName( ) );
        assertEquals( PARAM_VALUE, reportStored.getParameter( ).get( 0 ).getValue( ) );

        reportStored.setName( NAME2 );
        reportStored.setPage( PAGE2 );
        reportStored.setParameter( new ArrayList<>( ) );
        ReportHome.update( reportStored );
        reportStored = ReportHome.findByPrimaryKey( report.getId( ) );
        assertEquals( NAME2, reportStored.getName( ) );
        assertEquals( PAGE2, reportStored.getPage( ) );

        assertTrue( ReportHome.getIdReportsList( ).contains( report.getId( ) ) );

        ReportHome.remove( reportStored );
        assertNull( ReportHome.findByPrimaryKey( report.getId( ) ) );
        assertTrue( ParameterHome.findByIdReport( report.getId( ) ).isEmpty( ) );
    }

    /**
     * Checks the validation constraints of a report
     */
    @Test
    public void testValidation( )
    {
        Report report = new Report( );
        report.setName( "" );
        report.setPage( PAGE1 );
        Set<ConstraintViolation<Report>> violations = BeanValidationUtil.validate( report );
        assertEquals( 1, violations.size( ) );
        assertEquals( NotEmpty.class, violations.iterator( ).next( ).getConstraintDescriptor( ).getAnnotation( ).annotationType( ) );
        assertEquals( "#i18n{reportlauncher.validation.report.Name.notEmpty}", violations.iterator( ).next( ).getMessageTemplate( ) );

        report.setName( "x".repeat( 51 ) );
        violations = BeanValidationUtil.validate( report );
        assertEquals( 1, violations.size( ) );
        assertEquals( Size.class, violations.iterator( ).next( ).getConstraintDescriptor( ).getAnnotation( ).annotationType( ) );
        assertEquals( "#i18n{reportlauncher.validation.report.Name.size}", violations.iterator( ).next( ).getMessageTemplate( ) );

        report.setName( NAME1 );
        assertTrue( BeanValidationUtil.validate( report ).isEmpty( ) );
    }
}
