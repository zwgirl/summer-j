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

package org.eclipse.wst.common.project.facet.core.events;

/**
 * The root interface of all faceted project framework events. 
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface IFacetedProjectFrameworkEvent
{
    enum Type
    {
        PRESET_ADDED,
        PRESET_REMOVED
    }
    
    /**
     * Returns the type of this event.
     * 
     * @return the type of this event
     */
    
    Type getType();

}
