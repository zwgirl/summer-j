/******************************************************************************
 * Copyright (c) 2010 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.wst.common.project.facet.core.internal;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class FacetedProjectPropertyTester

    extends PropertyTester
    
{
    public boolean test( final Object receiver, 
                         final String property, 
                         final Object[] args, 
                         final Object value )
    {
        try
        {
            final String val = (String) value;
            final int colon = val.indexOf( ':' );
            
            final String fid;
            final String vexpr;
            
            if( colon == -1 || colon == val.length() - 1 )
            {
                fid = val;
                vexpr = null;
            }
            else
            {
                fid = val.substring( 0, colon );
                vexpr = val.substring( colon + 1 );
            }
            
            if( receiver instanceof IResource )
            {
                final IProject pj = ( (IResource) receiver ).getProject();
                
                if( pj == null )
                {
                    return false;
                }
                
                return FacetedProjectFramework.hasProjectFacet( pj, fid, vexpr );
            }
            else if( receiver instanceof IProjectFacetVersion )
            {
                final IProjectFacetVersion fv = (IProjectFacetVersion) receiver;
                final IProjectFacet f = fv.getProjectFacet();
                
                return f.getId().equals( fid ) &&
                       ( vexpr == null || f.getVersions( vexpr ).contains( fv ) );
            }
        }
        catch( CoreException e )
        {
            FacetCorePlugin.log( e.getStatus() );
        }
            
        return false;
    }

}
