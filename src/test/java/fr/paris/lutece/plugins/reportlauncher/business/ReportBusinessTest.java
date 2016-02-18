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


public class ReportBusinessTest extends LuteceTestCase
{
    private final static String NAME1 = "Name1";
    private final static String NAME2 = "Name2";
    private final static String FRAGMENT1 = "Fragment1";
    private final static String FRAGMENT2 = "Fragment2";
    private final static int PARAMETER1 = 1;
    private final static int PARAMETER2 = 2;

    public void testBusiness(  )
    {
        // Initialize an object
        /*    Report report = new Report(  );
            report.setName( NAME1 );
            report.setFragment( FRAGMENT1 );
            report.setParameter( PARAMETER1 );
            */

        // Create test
        /*  ReportHome.create( report );

          Report reportStored = ReportHome.findByPrimaryKey( report.getId(  ) );
          assertEquals( reportStored.getName(  ), report.getName(  ) );
          assertEquals( reportStored.getFragment(  ), report.getFragment(  ) );
          assertEquals( reportStored.getParameter(  ), report.getParameter(  ) );
          */

        // Update test
        /*  report.setName( NAME2 );
          report.setFragment( FRAGMENT2 );
          report.setParameter( PARAMETER2 );
          ReportHome.update( report );
          reportStored = ReportHome.findByPrimaryKey( report.getId(  ) );
          assertEquals( reportStored.getName(  ), report.getName(  ) );
          assertEquals( reportStored.getFragment(  ), report.getFragment(  ) );
          assertEquals( reportStored.getParameter(  ), report.getParameter(  ) );
                  */

        // List test
        /*  ReportHome.getReportsList(  );

          // Delete test
          ReportHome.remove( report.getId(  ) );
          reportStored = ReportHome.findByPrimaryKey( report.getId(  ) );
          assertNull( reportStored );
        */
    }
}
