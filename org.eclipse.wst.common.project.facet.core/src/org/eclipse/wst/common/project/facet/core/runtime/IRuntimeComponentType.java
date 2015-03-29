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

package org.eclipse.wst.common.project.facet.core.runtime;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.wst.common.project.facet.core.DefaultVersionComparator;

/**
 * Represents the type of a runtime component. A runtime instance is composed of  multiple runtime 
 * components, each of which has a type and a version.
 * 
 * <p>This interface is not intended to be implemented outside of this framework. Runtime component
 * types are declared using <code>org.eclipse.wst.common.project.facet.core.runtimes</code>
 * extension point. Once declared, client code can get access to <code>IRuntimeComponentType</code> 
 * objects by using methods on the {@link RuntimeManager} class.</p>  
 *
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 * @see RuntimeManager#getRuntimeComponentTypes()
 * @see RuntimeManager#getRuntimeComponentType(String)
 * @see RuntimeManager#isRuntimeComponentTypeDefined(String)
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface IRuntimeComponentType

    extends IAdaptable
    
{
    /**
     * Returns the runtime component type id.
     * 
     * @return the runtime component type id
     */
    
    String getId();
    
    /**
     * Returns the plugin id of where this runtime component type is defined.
     * 
     * @return the plugin id of where this runtime component type is defined
     */
    
    String getPluginId();
    
    /**
     * Returns all of the versions of this runtime component type.
     * 
     * @return all of the versions of this runtime component type
     */
    
    Set<IRuntimeComponentVersion> getVersions();
    
    Set<IRuntimeComponentVersion> getVersions( String expr )
    
        throws CoreException;
    
    /**
     * Determines whether the specified version of this runtime component type
     * exists.
     * 
     * @param version the version string
     * @return <code>true</code> if the specified version exists, 
     *   <code>false</code> otherwise
     */
    
    boolean hasVersion( String version );
    
    /**
     * Returns the {@link IRuntimeComponentVersion} object corresponding to the
     * provided version string.
     * 
     * @param version the version string
     * @return the {@link IRuntimeComponentVersion} object corresponding to the
     *   provided version string
     * @throws IllegalArgumentException if the version does not exist
     */
    
    IRuntimeComponentVersion getVersion( String version );
    
    /**
     * Returns the latest version of this runtime component as specified by the 
     * version comparator.
     * 
     * @return returns the latest version of this runtime component
     */

    IRuntimeComponentVersion getLatestVersion()
    
        throws CoreException;
    
    /**
     * Returns a sorted list containing all of the versions of this runtime
     * component type. The sort order is determined by the version comparator. 
     * 
     * @param ascending whether versions should be sorted in ascending order
     * @return a sorted list containing all of the versions of this runtime 
     *   component type
     */
    
    List<IRuntimeComponentVersion> getSortedVersions( boolean ascending )
    
        throws CoreException;
    
    /**
     * Returns the version comparator specified for this runtime component type.
     * If no version comparator is specified, this method will return an 
     * instance of the {@link DefaultVersionComparator}.
     * 
     * @return the version comparator specified for this runtime component type
     */
    
    Comparator<String> getVersionComparator()
    
        throws CoreException;
    
}
