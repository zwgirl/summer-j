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

package org.eclipse.wst.common.project.facet.ui;

import org.eclipse.wst.common.project.facet.core.IFacetedProject;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 * @deprecated use ModifyFacetedProjectWizard class instead
 */

public class AddRemoveFacetsWizard 

    extends ModifyFacetedProjectWizard 
    
{
    public AddRemoveFacetsWizard( final IFacetedProject fproj )
    {
        super( fproj );
    }
    
}
