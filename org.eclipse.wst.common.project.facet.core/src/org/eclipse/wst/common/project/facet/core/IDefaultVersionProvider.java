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

package org.eclipse.wst.common.project.facet.core;

/**
 * This interface can be implemented in order to provide the facet version that
 * is selected by default. If a version provider is not specified, the latest
 * version will be used. Note that if a runtime is selected, the runtime can 
 * override this default with a version best suited for that runtime. 
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface IDefaultVersionProvider
{
    /**
     * Returns the facet version that should be selected by default.
     * 
     * @return the facet version that should be selected by default
     */
    
    IProjectFacetVersion getDefaultVersion();

}
