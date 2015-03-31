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

package org.eclipse.wst.common.project.facet.core.events.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetsChangedEvent;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class ProjectFacetsChangedEvent

    extends FacetedProjectEvent
    implements IProjectFacetsChangedEvent
    
{
    private final Set<IProjectFacetVersion> addedFacets;
    private final Set<IProjectFacetVersion> removedFacets;
    private final Set<IProjectFacetVersion> changedVersions;
    private final Set<IProjectFacetVersion> allAffectedFacets;
    
    public ProjectFacetsChangedEvent( final IFacetedProjectWorkingCopy fpjwc,
                                      final Set<IProjectFacetVersion> addedFacets,
                                      final Set<IProjectFacetVersion> removedFacets,
                                      final Set<IProjectFacetVersion> changedVersions )
    {
        super( fpjwc, IFacetedProjectEvent.Type.PROJECT_FACETS_CHANGED );
        
        this.addedFacets = Collections.unmodifiableSet( addedFacets );
        this.removedFacets = Collections.unmodifiableSet( removedFacets );
        this.changedVersions = Collections.unmodifiableSet( changedVersions );
        
        final Set<IProjectFacetVersion> allAffectedFacets = new HashSet<IProjectFacetVersion>();
        
        allAffectedFacets.addAll( addedFacets );
        allAffectedFacets.addAll( removedFacets );
        allAffectedFacets.addAll( changedVersions );
        
        this.allAffectedFacets = Collections.unmodifiableSet( allAffectedFacets );
    }

    public Set<IProjectFacetVersion> getAddedFacets()
    {
        return this.addedFacets;
    }

    public Set<IProjectFacetVersion> getRemovedFacets()
    {
        return this.removedFacets;
    }

    public Set<IProjectFacetVersion> getFacetsWithChangedVersions()
    {
        return this.changedVersions;
    }

    public Set<IProjectFacetVersion> getAllAffectedFacets()
    {
        return this.allAffectedFacets;
    }
    
}
