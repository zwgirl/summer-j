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

import org.eclipse.osgi.util.NLS;

/**
 * The exception that's thrown when version string cannot be parsed.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public class VersionFormatException 
    
    extends RuntimeException 
    
{
    private static final long serialVersionUID = 1L;
    
    private final Object comparator;
    private final String version;
    
    public VersionFormatException( final Object comparator,
                                   final String version )
    {
        this.comparator = comparator;
        this.version = version;
    }
    
    public Object getComparator()
    {
        return this.comparator;
    }
    
    public String getVersion()
    {
        return this.version;
    }
    
    public String getMessage()
    {
        return NLS.bind( Resources.couldNotParse, this.version,
                         this.comparator.getClass().getName() );
    }
    
    private static final class Resources
    
        extends NLS
        
    {
        public static String couldNotParse;
        
        static
        {
            initializeMessages( VersionFormatException.class.getName(), 
                                Resources.class );
        }
    }

}
