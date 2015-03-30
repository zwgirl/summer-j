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

package org.eclipse.lark.common.project.facet.core.libprov.internal;

import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.beginTask;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.done;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.worked;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.lark.common.project.facet.core.internal.ClasspathUtil;
import org.eclipse.lark.common.project.facet.core.libprov.LibraryProviderOperation;
import org.eclipse.lark.common.project.facet.core.libprov.LibraryProviderOperationConfig;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class LegacyRuntimeLibraryProviderUninstallOperation

    extends LibraryProviderOperation
    
{
    public void execute( final LibraryProviderOperationConfig config,
                         final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        beginTask( monitor, "", 1 ); //$NON-NLS-1$
        
        try
        {
            final IProject project = config.getFacetedProject().getProject();
            ClasspathUtil.removeClasspathEntries( project, config.getProjectFacet() );
            
            worked( monitor, 1 );
        }
        finally
        {
            done( monitor );
        }
    }
    
}