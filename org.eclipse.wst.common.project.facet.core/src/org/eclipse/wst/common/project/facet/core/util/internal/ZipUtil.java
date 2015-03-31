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

package org.eclipse.wst.common.project.facet.core.util.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.util.NLS;

/**
 * Contains a series of static utility methods for working with zip archives.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class ZipUtil
{
    /**
     * This class is a container for static methods and is not meant to be
     * instantiated.
     */
    
    private ZipUtil() {}

    public static void zip( final File dir,
                            final File target )
    
        throws IOException
        
    {
        if( target.exists() )
        {
            delete( target );
        }
        
        final ZipOutputStream zip 
            = new ZipOutputStream( new FileOutputStream( target ) );
        
        try
        {
            zipDir( target, zip, dir, "" ); //$NON-NLS-1$
        }
        finally
        {
            try
            {
                zip.close();
            }
            catch( IOException e ) {}
        }
    }
    
    private static void zipDir( final File target,
                                final ZipOutputStream zip,
                                final File dir,
                                final String path )
    
        throws IOException
        
       {
        for( File f : dir.listFiles() )
        {
            final String cpath = path + f.getName();
            
            if( f.isDirectory() )
            {
                zipDir( target, zip, f, cpath + "/" ); //$NON-NLS-1$
            }
            else
            {
                zipFile( target, zip, f, cpath );
            }
        }
       }
    
    private static void zipFile( final File target,
                                 final ZipOutputStream zip,
                                 final File file,
                                 final String path )
    
        throws IOException
        
    {
        if( ! file.equals( target ) )
        {
            final ZipEntry ze = new ZipEntry( path );
            
            ze.setTime( file.lastModified() + 1999 );
            ze.setMethod( ZipEntry.DEFLATED );
            
            zip.putNextEntry( ze );
            
            final FileInputStream in = new FileInputStream( file );
            
            try
            {
                int bufsize = 8 * 1024;
                final long flength = file.length();
                
                if( flength == 0 )
                {
                    return;
                }
                else if( flength < bufsize )
                {
                    bufsize = (int) flength;
                }
                
                final byte[] buffer = new byte[ bufsize ];
                int count = in.read( buffer );
                
                while( count != -1 )
                {
                    zip.write( buffer, 0, count );
                    count = in.read( buffer );
                }
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch( IOException e ) {}
            }
        }
    }

    public static void unzip( final File file,
                              final File destdir )

        throws IOException
    
    {    
        unzip( file, destdir, new NullProgressMonitor() );
    }
    
    public static void unzip( final File file,
                              final File destdir, 
                              final IProgressMonitor monitor )

        throws IOException

    {
        final ZipFile zip = open( file );
        
        try
        {
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            
            final int totalWork = zip.size();
            monitor.beginTask( Resources.progressUnzipping, totalWork );
            
            int c = 0;
            
            while( entries.hasMoreElements() )
            {
                final ZipEntry entry = entries.nextElement();
                
                monitor.worked( 1 );
                
                final String taskMsg = NLS.bind( Resources.progressUnzipped, c++, totalWork);
                monitor.setTaskName( taskMsg );
                
                if( entry.isDirectory() ) continue;
    
                final File f = new File( destdir, entry.getName() );
                final File dir = f.getParentFile();
    
                if( ! dir.exists() && ! dir.mkdirs() )
                {
                    final String msg = "Could not create dir: " + dir.getPath(); //$NON-NLS-1$
                    throw new IOException( msg );
                }
    
                InputStream in = null;
                FileOutputStream out = null;
                
                try
                {
                    in = zip.getInputStream( entry );
                    out = new FileOutputStream( f );
        
                    final byte[] bytes = new byte[ 1024 ];
                    int count = in.read( bytes );
        
                    while( count != -1 )
                    {
                        out.write( bytes, 0, count );
                        count = in.read( bytes );
                    }
                    
                    out.flush();
                }
                finally
                {
                    if( in != null )
                    {
                        try
                        {
                            in.close();
                        }
                        catch( IOException e ) {}
                    }
        
                    if( out != null )
                    {
                        try
                        {
                            out.close();
                        }
                        catch( IOException e ) {}
                    }
                }
            }
        }
        finally
        {
            try
            {
                zip.close();
            }
            catch( IOException e ) {}
        }
    }
    
    public static ZipFile open( final File file )
    
        throws IOException
        
    {
        try
        {
            return new ZipFile( file );
        }
        catch( FileNotFoundException e )
        {
            final FileNotFoundException fnfe
                = new FileNotFoundException( file.getAbsolutePath() );
            
            fnfe.initCause( e );
            
            throw fnfe;
        }
    }

    public static ZipEntry getZipEntry( final ZipFile zip,
                                        final String name )
    {
        final String lcasename = name.toLowerCase();

        for( Enumeration<? extends ZipEntry> itr = zip.entries(); itr.hasMoreElements(); )
        {
            final ZipEntry zipentry = itr.nextElement();

            if( zipentry.getName().toLowerCase().equals( lcasename ) )
            {
                return zipentry;
            }
        }

        return null;
    }
    
    private static void delete( final File f )

        throws IOException
    
    {
        if( f.isDirectory() )
        {
            for( File child : f.listFiles() )
            {
                delete( child );
            }
        }
    
        if( ! f.delete() )
        {
            final String msg = "Could not delete " + f.getPath() + "."; //$NON-NLS-1$ //$NON-NLS-2$
            throw new IOException( msg );
        }
    }
    
    private static final class Resources
    
        extends NLS
    
    {
        public static String progressUnzipped;
        public static String progressUnzipping;
        static
        {
            initializeMessages( ZipUtil.class.getName(), 
                                Resources.class );
        }
    }
    
}
