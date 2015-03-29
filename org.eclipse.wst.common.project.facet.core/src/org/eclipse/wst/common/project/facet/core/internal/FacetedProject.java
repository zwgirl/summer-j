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

import static org.eclipse.wst.common.project.facet.core.util.internal.FileUtil.validateEdit;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.beginTask;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.done;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.submon;
import static org.eclipse.wst.common.project.facet.core.util.internal.ProgressMonitorUtil.worked;
import static org.eclipse.wst.common.project.facet.core.util.internal.XmlUtil.escape;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.project.facet.core.ActionConfig;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFrameworkException;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectValidator;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.ITargetedRuntimesChangedEvent;
import org.eclipse.wst.common.project.facet.core.events.internal.FixedFacetsChangedEvent;
import org.eclipse.wst.common.project.facet.core.events.internal.LegacyListenerAdapter;
import org.eclipse.wst.common.project.facet.core.events.internal.PrimaryRuntimeChangedEvent;
import org.eclipse.wst.common.project.facet.core.events.internal.ProjectFacetActionEvent;
import org.eclipse.wst.common.project.facet.core.events.internal.ProjectListenerRegistry;
import org.eclipse.wst.common.project.facet.core.events.internal.ProjectModifiedEvent;
import org.eclipse.wst.common.project.facet.core.events.internal.TargetedRuntimesChangedEvent;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;
import org.eclipse.wst.common.project.facet.core.runtime.internal.UnknownRuntime;
import org.eclipse.wst.common.project.facet.core.util.internal.ObjectReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

/* 
 * Synchronization Notes
 * 
 * 1. There is an internal lock object that's used to synchronize access to
 *    the data structure. By synchronizing on an internal object, outside code
 *    cannot cause a deadlock by synchronizing on the FacetedProject object.
 *    
 * 2. Readers synchronize on the lock object for the duration of the method
 *    call. This protects the readers from writers and makes sure that reader
 *    is not reading stale data from thread's local memory.
 *    
 * 3. All collections that are returned by the reader methods are guaranteed
 *    to not change after the fact. This is implemented through a copy-on-write 
 *    policy.
 * 
 * 4. Writers synchronize on the lock object briefly at the start of the method 
 *    and mark the FacetedProject as being modified. If the project is already
 *    being modified, the new writer will wait. Inside the bodies of the
 *    modifier methods, the writer thread is only synchronized on the lock
 *    object while modifying the internal datastructures. These synchronization
 *    sections are kept short and they never span over code that might modify
 *    file system resources. This is done to prevent deadlocks. Once the write
 *    is complete, the writer thread synchronizes on the lock object, resets the 
 *    "being modified" flag, and notifies any writers that may be waiting. 
 */

public final class FacetedProject

    implements IFacetedProject
    
{
    public static final String METADATA_FILE
        = ".settings/" + FacetCorePlugin.PLUGIN_ID + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String TRACING_DELEGATE_CALLS
        = FacetCorePlugin.PLUGIN_ID + "/delegate/calls"; //$NON-NLS-1$
    
    private static final String TRACING_EVENTS
        = FacetCorePlugin.PLUGIN_ID + "/events/project"; //$NON-NLS-1$
    
    private static final String TRACING_EVENTS_STACKTRACE
        = FacetCorePlugin.PLUGIN_ID + "/events/project/stacktrace"; //$NON-NLS-1$

    private static final String EL_RUNTIME = "runtime"; //$NON-NLS-1$
    private static final String EL_SECONDARY_RUNTIME = "secondary-runtime"; //$NON-NLS-1$
    private static final String EL_FIXED = "fixed"; //$NON-NLS-1$
    private static final String EL_INSTALLED = "installed"; //$NON-NLS-1$
    private static final String ATTR_NAME = "name"; //$NON-NLS-1$
    private static final String ATTR_FACET = "facet"; //$NON-NLS-1$
    private static final String ATTR_VERSION = "version"; //$NON-NLS-1$

    private final IProject project;
    private final Set<IProjectFacetVersion> facets;
    private final Set<IProjectFacetVersion> facetsReadOnly;
    private final Set<IProjectFacet> fixed;
    private final Set<IProjectFacet> fixedReadOnly;
    private final Map<String,ProjectFacet> unknownFacets;
    private final Map<IProjectFacet,String> activeFacetAliases;
    private final Map<IProjectFacetVersion,String> activeFacetVersionAliases;
    private final Set<String> targetedRuntimes;
    private String primaryRuntime;
    IFile f;
    private long fModificationStamp = -1;
    private final ProjectListenerRegistry listeners;
    private final Object lock = new Object();
    private boolean isBeingModified = false;
    private Thread modifierThread = null;
    private Exception parsingException;
    private boolean isDeleted;
    
    FacetedProject( final IProject project )
    
        throws CoreException
        
    {
        this.project = project;
        this.facets = new CopyOnWriteArraySet<IProjectFacetVersion>();
        this.facetsReadOnly = Collections.unmodifiableSet( this.facets );
        this.fixed = new CopyOnWriteArraySet<IProjectFacet>();
        this.fixedReadOnly = Collections.unmodifiableSet( this.fixed );
        this.unknownFacets = new HashMap<String,ProjectFacet>();
        this.activeFacetAliases = new HashMap<IProjectFacet,String>();
        this.activeFacetVersionAliases = new HashMap<IProjectFacetVersion,String>();
        this.targetedRuntimes = new CopyOnWriteArraySet<String>();
        this.listeners = new ProjectListenerRegistry();
        this.parsingException = null;
        this.isDeleted = false;
        
        this.f = project.getFile( METADATA_FILE );
        
        refresh( true );
    }
    
    public IProject getProject()
    {
        return this.project;
    }
    
    public Set<IProjectFacetVersion> getProjectFacets()
    {
        synchronized( this.lock )
        {
            return this.facetsReadOnly;
        }
    }
    
    public boolean hasProjectFacet( final IProjectFacet f )
    {
        synchronized( this.lock )
        {
            for( IProjectFacetVersion fv : this.facets )
            {
                if( fv.getProjectFacet() == f )
                {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    public boolean hasProjectFacet( final IProjectFacetVersion fv )
    {
        synchronized( this.lock )
        {
            return this.facets.contains( fv );
        }
    }
    
    public IProjectFacetVersion getProjectFacetVersion( final IProjectFacet f )
    {
        synchronized( this.lock )
        {
            for( IProjectFacetVersion fv : this.facets )
            {
                if( fv.getProjectFacet() == f )
                {
                    return fv;
                }
            }
            
            return null;
        }
    }

    public IProjectFacetVersion getInstalledVersion( final IProjectFacet f )
    {
        return getProjectFacetVersion( f );
    }

    public void installProjectFacet( final IProjectFacetVersion fv,
                                     final Object config,
                                     final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final Action action 
            = new Action( Action.Type.INSTALL, fv, config );
            
        modify( Collections.singleton( action ), monitor );
    }

    public void uninstallProjectFacet( final IProjectFacetVersion fv,
                                       final Object config,
                                       final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final Action action 
            = new Action( Action.Type.UNINSTALL, fv, config );
            
        modify( Collections.singleton( action ), monitor );
    }
    
    public void modify( final Set<Action> actions,
                        final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final ObjectReference<Boolean> result = new ObjectReference<Boolean>( true );
        
        final IWorkspaceRunnable wr = new IWorkspaceRunnable()
        {
            public void run( final IProgressMonitor monitor ) 
            
                throws CoreException
                
            {
                beginModification();
                
                try
                {
                    final IFacetedProjectWorkingCopy fpjwc = createWorkingCopy();

                    try
                    {
                        for( Action action : actions )
                        {
                            final Action.Type type = action.getType();
                            final IProjectFacetVersion fv = action.getProjectFacetVersion();
                            
                            if( type == Action.Type.INSTALL )
                            {
                                fpjwc.addProjectFacet( fv );
                            }
                            else if( type == Action.Type.VERSION_CHANGE )
                            {
                                fpjwc.changeProjectFacetVersion( fv );
                            }
                            else if( type == Action.Type.UNINSTALL )
                            {
                                fpjwc.removeProjectFacet( fv );
                            }
                            else
                            {
                                throw new IllegalStateException();
                            }
                            
                            final Object config = action.getConfig();
                            
                            if( config != null )
                            {
                                fpjwc.setProjectFacetActionConfig( fv.getProjectFacet(), config );
                            }
                        }
                        
                        result.set( mergeChangesInternal( fpjwc, monitor ) );
                    }
                    finally
                    {
                        fpjwc.dispose();
                    }
                }
                finally
                {
                    endModification();
                }
            }
        };
        
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        ws.run( wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, monitor );
        
        if( result.get() )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
        
    private boolean modifyInternal( final Set<Action> actions,
                                    final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        beginTask( monitor, "", actions.size() * 100 ); //$NON-NLS-1$
        
        try
        {
            validateNotDeleted();
            
            final IStatus st = ProjectFacetsManager.check( this.facets, actions );
            
            if( ! st.isOK() )
            {
                throw new CoreException( st );
            }
            
            // Perform batched validateEdit call.
            
            final Set<IFile> files = new HashSet<IFile>();
            files.add( this.f );
            
            for( Action action : actions )
            {
                final Object actionConfig = action.getConfig();
                
                if( actionConfig instanceof ActionConfig )
                {
                    files.addAll( ( (ActionConfig) actionConfig ).getValidateEditFiles() );
                }
            }
            
            if( ! validateEdit( files ) )
            {
                return false;
            }
            
            // Sort the actions into the order of execution.
            
            final List<Action> copy = new ArrayList<Action>( actions );
            ProjectFacetsManager.sort( this.facets, copy );
            
            // Execute the actions.
            
            for( Action action : copy )
            {
                final Action.Type type = action.getType();
                final ProjectFacetVersion fv = (ProjectFacetVersion) action.getProjectFacetVersion();
                final IActionDefinition def = fv.getActionDefinition( this.facets, type );
                final Object config = action.getConfig();
                
                if( monitor != null )
                {
                    final String subTaskDescriptionTemplate;
                    
                    if( type == Action.Type.INSTALL )
                    {
                        subTaskDescriptionTemplate = Resources.taskInstallingFacet; 
                    }
                    else if( type == Action.Type.UNINSTALL )
                    {
                        subTaskDescriptionTemplate = Resources.taskUninstallingFacet;
                    }
                    else if( type == Action.Type.VERSION_CHANGE )
                    {
                        subTaskDescriptionTemplate = Resources.taskChangingFacetVersion;
                    }
                    else
                    {
                        throw new IllegalStateException();
                    }
                    
                    final String subTaskDescription
                        = NLS.bind( subTaskDescriptionTemplate, fv.getProjectFacet().getLabel() );
                    
                    monitor.subTask( subTaskDescription );
                }
                
                final IFacetedProjectEvent preEvent
                    = new ProjectFacetActionEvent( this, getPreEventType( type ), fv, config );
                                                  
                notifyListeners( preEvent );
                worked( monitor, 10 );
                
                final IDelegate delegate = ( (ActionDefinition) def ).getDelegate();
                
                if( delegate == null )
                {
                    worked( monitor, 80 );
                }
                else
                {
                    callDelegate( fv, delegate, config, type, submon( monitor, 80 ) );
                }
        
                synchronized( this.lock )
                {
                    apply( action );
                }
                
                save();

                final IFacetedProjectEvent postEvent
                    = new ProjectFacetActionEvent( this, getPostEventType( type ), fv, config );
                
                notifyListeners( postEvent );
                worked( monitor, 10 );
            }
            
            return true;
        }
        finally
        {
            done( monitor );
        }
    }
    
    public Set<IProjectFacet> getFixedProjectFacets()
    {
        synchronized( this.lock )
        {
            return this.fixedReadOnly;
        }
    }
    
    public boolean isFixedProjectFacet( final IProjectFacet facet )
    {
        synchronized( this.lock )
        {
            return this.fixed.contains( facet );
        }
    }
    
    public void setFixedProjectFacets( final Set<IProjectFacet> facets )
    
        throws CoreException
        
    {
        final ObjectReference<Boolean> result = new ObjectReference<Boolean>( true );
        
        final IWorkspaceRunnable wr = new IWorkspaceRunnable()
        {
            public void run( final IProgressMonitor monitor ) 
            
                throws CoreException
                
            {
                beginModification();
                
                try
                {
                    result.set( setFixedProjectFacetsInternal( facets, monitor ) );
                }
                finally
                {
                    endModification();
                }
            }
        };
        
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        ws.run( wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, null );

        if( result.get() )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
    
    private boolean setFixedProjectFacetsInternal( final Set<IProjectFacet> facets,
                                                   final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        beginTask( monitor, "", 2 ); //$NON-NLS-1$
        
        try
        {
            validateNotDeleted();
            
            Set<IProjectFacet> oldFixedFacets = null;
            
            synchronized( FacetedProject.this.lock )
            {
                if( equals( this.fixed, facets ) )
                {
                    return false;
                }
                
                oldFixedFacets = new HashSet<IProjectFacet>( this.fixed );
                
                this.fixed.clear();
                this.fixed.addAll( facets );
            }
                
            save();
            worked( monitor, 1 );

            notifyListeners( new FixedFacetsChangedEvent( this, oldFixedFacets, facets ) );
            worked( monitor, 1 );
            
            return true;
        }
        finally
        {
            done( monitor );
        }
    }
    
    /**
     * @deprecated
     */
    
    public IRuntime getRuntime()
    {
        return getPrimaryRuntime();
    }
    
    /**
     * @deprecated
     */
    
    public void setRuntime( final IRuntime runtime,
                            final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final Set<IRuntime> runtimes
            = runtime == null 
              ? Collections.<IRuntime>emptySet() : Collections.singleton( runtime );
        
        setTargetedRuntimes( runtimes, monitor );
    }
    
    public boolean isTargetable( final IRuntime runtime )
    {
        for( IProjectFacetVersion fv : getProjectFacets() )
        {
            if( ! runtime.supports( fv ) )
            {
                return false;
            }
        }
        
        return true;
    }

    public boolean isTargeted( final IRuntime runtime )
    {
        for( IRuntime r : getTargetedRuntimes() )
        {
            if( r.getName().equals( runtime.getName() ) )
            {
                return true;
            }
        }
        
        return false;
    }

    public Set<IRuntime> getTargetedRuntimes()
    {
        synchronized( this.lock )
        {
            final Set<IRuntime> result = new HashSet<IRuntime>();
            
            for( String rname : this.targetedRuntimes )
            {
                result.add( getRuntimeFromName( rname ) );
            }
            
            return Collections.unmodifiableSet( result );
        }
    }
    
    public void setTargetedRuntimes( final Set<IRuntime> runtimes,
                                     final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final ObjectReference<Boolean> result = new ObjectReference<Boolean>( true );
        
        final IWorkspaceRunnable wr = new IWorkspaceRunnable()
        {
            public void run( final IProgressMonitor monitor ) 
            
                throws CoreException
                
            {
                beginModification();
                
                try
                {
                    result.set( setTargetedRuntimesInternal( runtimes, monitor ) );
                }
                finally
                {
                    endModification();
                }
            }
        };
        
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        ws.run( wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, null );
        
        if( result.get() )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
    
    private boolean setTargetedRuntimesInternal( final Set<IRuntime> runtimes,
                                                 final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        beginTask( monitor, "", 2 ); //$NON-NLS-1$
        
        try
        {
            validateNotDeleted();
            
            final Set<IRuntime> oldRuntimes;
            final Set<IRuntime> newRuntimes;
            final IRuntime oldPrimary;
            final IRuntime newPrimary;
            
            synchronized( this.lock )
            {
                if( this.targetedRuntimes.size() == runtimes.size() )
                {
                    boolean different = false;
                    
                    for( IRuntime r : runtimes )
                    {
                        if( ! this.targetedRuntimes.contains( r.getName() ) )
                        {
                            different = true;
                            break;
                        }
                    }
                    
                    if( ! different )
                    {
                        return false;
                    }
                }
            }
            
            if( ! validateEdit( Collections.singleton( this.f ) ) )
            {
                return false;
            }
                
            synchronized( this.lock )
            {
                for( IRuntime runtime : runtimes )
                {
                    for( IProjectFacetVersion fv : this.facets )
                    {
                        if( ! runtime.supports( fv ) )
                        {
                            final String msg 
                                = NLS.bind( Resources.facetNotSupported, runtime.getLocalizedName(), 
                                            fv.toString() );
                            
                            throw new CoreException( FacetCorePlugin.createErrorStatus( msg ) );
                        }
                    }
                }
                
                oldRuntimes = new HashSet<IRuntime>( getTargetedRuntimes() );
                
                this.targetedRuntimes.clear();
                
                for( IRuntime runtime : runtimes )
                {
                    this.targetedRuntimes.add( runtime.getName() );
                }
                
                newRuntimes = new HashSet<IRuntime>( getTargetedRuntimes() );
                
                oldPrimary = getPrimaryRuntime();
                assignPrimaryRuntimeIfNecessary();
                newPrimary = getPrimaryRuntime();
            }
            
            save();
            worked( monitor, 1 );
            
            final ITargetedRuntimesChangedEvent targetedRuntimesChangedEvent
                = new TargetedRuntimesChangedEvent( this, oldRuntimes, newRuntimes );
            
            notifyListeners( targetedRuntimesChangedEvent );
            
            if( ! equals( oldPrimary, newPrimary ) )
            {
                notifyListeners( new PrimaryRuntimeChangedEvent( this, oldPrimary, newPrimary ) );
            }
            
            worked( monitor, 1 );
            
            return true;
        }
        finally
        {
            done( monitor );
        }
    }
    
    public void addTargetedRuntime( final IRuntime runtime,
                                    final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final Set<IRuntime> runtimes = new HashSet<IRuntime>( getTargetedRuntimes() );
        runtimes.add( runtime );
        setTargetedRuntimes( runtimes, monitor );
    }
    
    public void removeTargetedRuntime( final IRuntime runtime,
                                       final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final Set<IRuntime> runtimes = new HashSet<IRuntime>( getTargetedRuntimes() );
        runtimes.remove( runtime );
        setTargetedRuntimes( runtimes, monitor );
    }

    public IRuntime getPrimaryRuntime()
    {
        synchronized( this.lock )
        {
            if( this.primaryRuntime == null )
            {
                return null;
            }
            else
            {
                return getRuntimeFromName( this.primaryRuntime );
            }
        }
    }
    
    public void setPrimaryRuntime( final IRuntime runtime,
                                   final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final ObjectReference<Boolean> result = new ObjectReference<Boolean>( true );
        
        final IWorkspaceRunnable wr = new IWorkspaceRunnable()
        {
            public void run( final IProgressMonitor monitor ) 
            
                throws CoreException
                
            {
                beginModification();
                
                try
                {
                    result.set( setPrimaryRuntimeInternal( runtime, monitor ) );
                }
                finally
                {
                    endModification();
                }
            }
        };
        
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        ws.run( wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, null );
        
        if( result.get() )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
    
    private boolean setPrimaryRuntimeInternal( final IRuntime runtime,
                                               final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        beginTask( monitor, "", 2 ); //$NON-NLS-1$
        
        try
        {
            validateNotDeleted();
            
            if( runtime == null )
            {
                throw new NullPointerException();
            }
            
            if( equals( this.primaryRuntime, runtime.getName() ) )
            {
                return false;
            }

            if( ! this.targetedRuntimes.contains( runtime.getName() ) )
            {
                final String msg = Resources.newPrimaryNotTargetRuntime;
                final IStatus st = FacetCorePlugin.createErrorStatus( msg );
            
                throw new CoreException( st );
            }
            
            final IRuntime oldPrimary;
            
            synchronized( this.lock )
            {
                oldPrimary = getPrimaryRuntime();
                this.primaryRuntime = runtime.getName();
            }
            
            save();
            worked( monitor, 1 );
            
            notifyListeners( new PrimaryRuntimeChangedEvent( this, oldPrimary, runtime ) );
            worked( monitor, 1 );
            
            return true;
        }
        finally
        {
            done( monitor );
        }
    }
    
    private static IRuntime getRuntimeFromName( final String name )
    {
        IRuntime runtime = null;
        
        if( RuntimeManager.isRuntimeDefined( name ) )
        {
            runtime = RuntimeManager.getRuntime( name );
        }
        
        if( runtime == null )
        {
            for( IRuntime r : RuntimeManager.getRuntimes() )
            {
                if( r.getAlternateNames().contains( name ) )
                {
                    runtime = r;
                    break;
                }
            }
        }
        
        if( runtime == null )
        {
            runtime = new UnknownRuntime( name );
        }
        
        return runtime;
    }
    
    private void assignPrimaryRuntimeIfNecessary()
    {
        if( this.targetedRuntimes.isEmpty() )
        {
            this.primaryRuntime = null;
        }
        else
        {
            if( this.primaryRuntime == null || 
                ! this.targetedRuntimes.contains( this.primaryRuntime ) )
            {
                this.primaryRuntime = this.targetedRuntimes.iterator().next();
            }
        }
    }
    
    public IStatus validate()
    {
        return validate( null );
    }
    
    public IStatus validate( final IProgressMonitor monitor )
    {
        synchronized( this.lock )
        {
            beginTask( monitor, Resources.taskValidatingFacetedProject, 5 );
            
            try
            {
                final List<String> errors = new ArrayList<String>();
                final List<String> warnings = new ArrayList<String>(); 
                
                // Check for parsing problems.
                
                if( this.parsingException != null )
                {
                    if( this.parsingException instanceof SAXException )
                    {
                        final String msg 
                            = NLS.bind( Resources.metadataFileCorrupted, 
                                        this.f.getFullPath().toString() );
                        
                        errors.add( msg );
                    }
                    else
                    {
                        final String msg 
                            = NLS.bind( Resources.couldNotReadMetadataFile, 
                                        this.f.getFullPath().toString() );
                        
                        errors.add( msg );
                    }
                }
                
                worked( monitor, 1 );
                
                // Are any of the target runtimes not defined?
                
                for( IRuntime r : getTargetedRuntimes() )
                {
                    if( r instanceof UnknownRuntime )
                    {
                        final String msg = NLS.bind( Resources.runtimeNotDefined, r.getName() );
                        errors.add( msg );
                    }
                }
                
                worked( monitor, 1 );
                
                // Is an installed facet not supported by the runtime?
                
                for( IRuntime r : getTargetedRuntimes() )
                {
                    for( IProjectFacetVersion fv : getProjectFacets() )
                    {
                        if( ! r.supports( fv ) )
                        {
                            final String msg
                                = NLS.bind( Resources.facetNotSupportedByTarget, fv.toString(), 
                                            r.getLocalizedName() );
                            
                            errors.add( msg );
                        }
                    }
                }
                
                worked( monitor, 1 );
                
                // Does the project contain any unknown facets or versions?
                
                for( IProjectFacetVersion fv : getProjectFacets() )
                {
                    final IProjectFacet f = fv.getProjectFacet();
                    
                    if( f.getPluginId() == null )
                    {
                        final String msg 
                            = NLS.bind( Resources.installedFacetNotFound, f.getId() );
                        
                        warnings.add( msg );
                    }
                    else if( fv.getPluginId() == null )
                    {
                        final String msg
                            = NLS.bind( Resources.installedFacetVersionNotFound, 
                                        f.getId(), fv.getVersionString() );

                        warnings.add( msg );
                    }
                }
                
                worked( monitor, 1 );
                
                // Compile the result.
                
                if( errors.isEmpty() && warnings.isEmpty() )
                {
                    return Status.OK_STATUS;
                }
                else
                {
                    final String msg 
                        = NLS.bind( Resources.projectValidationFailed, 
                                    this.project.getName() );
                    
                    final IStatus[] starray 
                        = new IStatus[ errors.size() + warnings.size() ];
                    
                    for( int i = 0, n = errors.size(); i < n; i++ )
                    {
                        starray[ i ] 
                            = new Status( IStatus.ERROR, FacetCorePlugin.PLUGIN_ID,
                                          errors.get( i ) );
                    }
                    
                    for( int i = 0, n = warnings.size(), offset = errors.size(); 
                         i < n; i++ )
                    {
                        starray[ i + offset ] 
                            = new Status( IStatus.WARNING, FacetCorePlugin.PLUGIN_ID,
                                          warnings.get( i ) );
                    }
                    
                    return new MultiStatus( FacetCorePlugin.PLUGIN_ID, -1,
                                            starray, msg, null );
                }
            }
            finally
            {
                done( monitor );
            }
        }
    }
    
    public IFacetedProjectWorkingCopy createWorkingCopy()
    {
        synchronized( this.lock )
        {
            return new FacetedProjectWorkingCopy( this );
        }
    }
    
    public void mergeChanges( final IFacetedProjectWorkingCopy fpjwc,
                              final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final ObjectReference<Boolean> result = new ObjectReference<Boolean>( true );
        
        final IWorkspaceRunnable wr = new IWorkspaceRunnable()
        {
            public void run( final IProgressMonitor monitor ) 
            
                throws CoreException
                
            {
                beginModification();
                
                try
                {
                    result.set( mergeChangesInternal( fpjwc, monitor ) );
                }
                finally
                {
                    endModification();
                }
            }
        };
        
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        ws.run( wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, monitor );
        
        if( result.get() )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
    
    private boolean mergeChangesInternal( final IFacetedProjectWorkingCopy fpjwc,
                                          final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final SubMonitor pm = SubMonitor.convert( monitor, 26 );
        
        try
        {
            boolean modified = false;
            
            // Figure out whether we can set runtimes before applying facet actions. This is better
            // for performance reasons, but may not work if the project contains facets that are
            // not supported by the new runtime. You can get into this situation if the user tries
            // to simultaneously uninstall a facet and select a different runtime. The fallback
            // solution for this situation is to set the targeted runtimes to an empty list first,
            // then apply the facet actions, and then set the targeted runtimes to the new list.
            // This is more drastic than necessary in all situations, but it is not clear that
            // implementing additional optimizations is necessary either.
            
            boolean canSetRuntimesFirst = true;
            
            for( IProjectFacetVersion fv : getProjectFacets() )
            {
                for( IRuntime r : fpjwc.getTargetedRuntimes() )
                {
                    if( ! r.supports( fv ) )
                    {
                        canSetRuntimesFirst = false;
                        break;
                    }
                }
                
                if( ! canSetRuntimesFirst )
                {
                    break;
                }
            }
            
            pm.subTask( Resources.taskConfiguringRuntimes );
            
            if( canSetRuntimesFirst )
            {
                if( setTargetedRuntimesInternal( fpjwc.getTargetedRuntimes(), submon( pm, 2 ) ) )
                {
                    modified = true;
                }
                
                if( getPrimaryRuntime() != null )
                {
                    setPrimaryRuntimeInternal( fpjwc.getPrimaryRuntime(), submon( pm, 2 ) );
                }
                else
                {
                    pm.worked( 2 );
                }
            }
            else
            {
                final Set<IRuntime> emptySet = Collections.emptySet();
                setTargetedRuntimesInternal( emptySet, submon( pm, 4 ) );
            }
            
            if( modifyInternal( fpjwc.getProjectFacetActions(), 
                                pm.newChild( 16, SubMonitor.SUPPRESS_SETTASKNAME ) ) )
            {
                modified = true;
            }
            
            if( ! canSetRuntimesFirst )
            {
                pm.subTask( Resources.taskConfiguringRuntimes );
                
                if( setTargetedRuntimesInternal( fpjwc.getTargetedRuntimes(), submon( pm, 2 ) ) )
                {
                    modified = true;
                }
                
                if( getPrimaryRuntime() != null )
                {
                    if( setPrimaryRuntimeInternal( fpjwc.getPrimaryRuntime(), submon( pm, 2 ) ) )
                    {
                        modified = true;
                    }
                }
                else
                {
                    pm.worked( 2 );
                }
            }
            
            if( setFixedProjectFacetsInternal( fpjwc.getFixedProjectFacets(), submon( pm, 2 ) ) )
            {
                modified = true;
            }
            
            return modified;
        }
        finally
        {
            pm.done();
        }
    }
    
    public IMarker createErrorMarker( final String message )
    
        throws CoreException
        
    {
        return createErrorMarker( IFacetedProjectValidator.BASE_MARKER_ID, message );
    }

    public IMarker createErrorMarker( final String type,
                                      final String message )
    
        throws CoreException
        
    {
        return createMarker( IMarker.SEVERITY_ERROR, type, message );
    }
    
    public IMarker createWarningMarker( final String message )
    
        throws CoreException
        
    {
        return createWarningMarker( IFacetedProjectValidator.BASE_MARKER_ID, message );
    }
    
    public IMarker createWarningMarker( final String type,
                                      final String message )
    
        throws CoreException
        
    {
        return createMarker( IMarker.SEVERITY_WARNING, type, message );
    }
    
    private IMarker createMarker( final int severity,
                                  final String type,
                                  final String message )
      
        throws CoreException
      
    {
        final IMarker[] existing
            = this.project.findMarkers( type, false, IResource.DEPTH_ZERO );
        
        for( int i = 0; i < existing.length; i++ )
        {
            final IMarker m = existing[ i ];
            
            if( m.getAttribute( IMarker.SEVERITY, -1 ) == severity &&
                m.getAttribute( IMarker.MESSAGE, "" ).equals( message ) ) //$NON-NLS-1$
            {
                return m;
            }
        }
        
        final IMarker m = this.project.createMarker( type );
      
        m.setAttribute( IMarker.MESSAGE, message ); 
        m.setAttribute( IMarker.SEVERITY, severity );
      
        return m;
    }
    
    public void addListener( final IFacetedProjectListener listener,
                             final IFacetedProjectEvent.Type... types )
    {
        this.listeners.addListener( listener, types );
    }
    
    public void removeListener( final IFacetedProjectListener listener )
    {
        this.listeners.removeListener( listener );
    }
    
    private void notifyListeners( final IFacetedProjectEvent event )
    {
        if( FacetCorePlugin.checkDebugOption( TRACING_EVENTS ) )
        {
            if( FacetCorePlugin.checkDebugOption( TRACING_EVENTS_STACKTRACE ) )
            {
                final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                
                System.out.println( "Faceted project event generated by:" ); //$NON-NLS-1$
                
                for( int i = 2; i < stacktrace.length; i++ )
                {
                    final StackTraceElement stackTraceElement = stacktrace[ i ]; 
                    final String c = stackTraceElement.getClassName();
                    final String m = stackTraceElement.getMethodName();
                    final String f = stackTraceElement.getFileName();
                    final int line = stackTraceElement.getLineNumber();
                    
                    if( i != 2 )
                    {
                        System.out.print( "    at " ); //$NON-NLS-1$
                    }
                    
                    System.out.println( c + "." + m + "(" + f + ":" + line + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
            
            System.out.println( event );
        }

        this.listeners.notifyListeners( event );
        FacetedProjectFrameworkImpl.getInstance().getProjectListenerRegistry().notifyListeners( event );
    }
    
    /**
     * @deprecated
     */
    
    public void addListener( final org.eclipse.wst.common.project.facet.core.IFacetedProjectListener listener )
    {
        this.listeners.addListener( new LegacyListenerAdapter( listener ), 
                                    IFacetedProjectEvent.Type.PROJECT_MODIFIED );
    }
    
    /**
     * @deprecated
     */
    
    public void removeListener( final org.eclipse.wst.common.project.facet.core.IFacetedProjectListener listener )
    {
        for( IFacetedProjectListener x 
             : this.listeners.getListeners( IFacetedProjectEvent.Type.PROJECT_MODIFIED ) )
        {
            if( x instanceof LegacyListenerAdapter &&
                ( (LegacyListenerAdapter) x ).getLegacyListener() == listener )
            {
                removeListener( x );
            }
        }
    }
    
    public Preferences getPreferences( final IProjectFacet facet )
    
        throws BackingStoreException
        
    {
        return FacetedProjectFrameworkImpl.getInstance().getPreferences( facet, this );
    }

    private void beginModification()
    
        throws CoreException
        
    {
        synchronized( this.lock )
        {
            while( this.isBeingModified )
            {
                if( this.modifierThread == Thread.currentThread() )
                {
                    final String msg = Resources.illegalModificationMsg;
                    final IStatus st = FacetCorePlugin.createErrorStatus( msg );
                    
                    throw new CoreException( st );
                }
                
                try
                {
                    this.lock.wait();
                }
                catch( InterruptedException e ) {}
            }
            
            this.isBeingModified = true;
            this.modifierThread = Thread.currentThread();
        }
    }
    
    private void endModification()
    {
        synchronized( this.lock )
        {
            this.isBeingModified = false;
            this.modifierThread = null;
            this.lock.notifyAll();
        }
    }
    
    private static IFacetedProjectEvent.Type getPreEventType( final Action.Type t )
    {
        if( t == Action.Type.INSTALL )
        {
            return IFacetedProjectEvent.Type.PRE_INSTALL;
        }
        else if( t == Action.Type.UNINSTALL )
        {
            return IFacetedProjectEvent.Type.PRE_UNINSTALL;
        }
        else if( t == Action.Type.VERSION_CHANGE )
        {
            return IFacetedProjectEvent.Type.PRE_VERSION_CHANGE;
        }
        else
        {
            throw new IllegalStateException();
        }
    }
    
    private static IFacetedProjectEvent.Type getPostEventType( final Action.Type t )
    {
        if( t == Action.Type.INSTALL )
        {
            return IFacetedProjectEvent.Type.POST_INSTALL;
        }
        else if( t == Action.Type.UNINSTALL )
        {
            return IFacetedProjectEvent.Type.POST_UNINSTALL;
        }
        else if( t == Action.Type.VERSION_CHANGE )
        {
            return IFacetedProjectEvent.Type.POST_VERSION_CHANGE;
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    private void callDelegate( final IProjectFacetVersion fv,
                               final IDelegate delegate,
                               final Object config,
                               final Object context,
                               final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        final String tracingDelegateCallsStr
            = Platform.getDebugOption( TRACING_DELEGATE_CALLS );
        
        final boolean tracingDelegateCalls 
            = tracingDelegateCallsStr == null ? false 
              : tracingDelegateCallsStr.equals( "true" );  //$NON-NLS-1$
        
        long timeStarted = -1;
        
        if( tracingDelegateCalls )
        {
            final String msg
                = Resources.bind( Resources.tracingDelegateStarting,
                                  fv.getProjectFacet().getId(),
                                  fv.getVersionString(), context.toString(),
                                  delegate.getClass().getName() );
            
            System.out.println( msg );
            
            timeStarted = System.currentTimeMillis();
        }
        
        try
        {
            delegate.execute( this.project, fv, config, monitor ); 
        }
        catch( Exception e )
        {
            final String msg;
            
            if( context == Action.Type.INSTALL )
            {
                msg = NLS.bind( Resources.failedOnInstall, fv );
            }
            else if( context == Action.Type.UNINSTALL )
            {
                msg = NLS.bind( Resources.failedOnUninstall, fv );
            }
            else if( context == Action.Type.VERSION_CHANGE )
            {
                msg = NLS.bind( Resources.failedOnVersionChange, 
                                fv.getProjectFacet().getLabel(), 
                                fv.getVersionString() );
            }
            else
            {
                throw new IllegalStateException( context.toString() );
            }
            
            final IStatus status
                = new Status( IStatus.ERROR, FacetCorePlugin.PLUGIN_ID, 0, 
                              msg, e );

            final FacetedProjectFrameworkException wrapper 
                = new FacetedProjectFrameworkException( status );
            
            if( e instanceof FacetedProjectFrameworkException &&
                ( (FacetedProjectFrameworkException) e ).isExpected() )
            {
                wrapper.setExpected( true );
            }
            
            throw wrapper;
        }
        
        if( context == Action.Type.UNINSTALL )
        {
            final IProjectFacet facet = fv.getProjectFacet();
            
            try
            {
                getPreferences( facet ).removeNode();
            }
            catch( BackingStoreException e )
            {
                FacetCorePlugin.log( e );
            }
        }
        
        if( tracingDelegateCalls )
        {
            final long duration = System.currentTimeMillis() - timeStarted;
            
            final String msg 
                = NLS.bind( Resources.tracingDelegateFinished, 
                            String.valueOf( duration ) );
            
            System.out.println( msg );
        }
    }
    
    private void apply( final Action action )
    {
        final Action.Type type = action.getType();
        final IProjectFacetVersion fv = action.getProjectFacetVersion();
        
        if( type == Action.Type.INSTALL )
        {
            this.facets.add( fv );
        }
        else if( type == Action.Type.UNINSTALL )
        {
            this.facets.remove( fv );
        }
        else if( type == Action.Type.VERSION_CHANGE )
        {
            for( IProjectFacetVersion x : this.facets )
            {
                if( x.getProjectFacet() == fv.getProjectFacet() )
                {
                    this.facets.remove( x );
                    break;
                }
            }
            
            this.facets.add( fv );
        }
    }
    
    private void save()
    
        throws CoreException
        
    {
        final StringWriter w = new StringWriter();
        final PrintWriter out = new PrintWriter( w );
        
        final String nl = System.getProperty( "line.separator" ); //$NON-NLS-1$
        
        out.print( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ); //$NON-NLS-1$
        out.print( nl );
        out.print( "<faceted-project>" ); //$NON-NLS-1$
        out.print( nl );
        
        if( this.primaryRuntime != null )
        {
            out.print( "  <runtime name=\"" ); //$NON-NLS-1$
            out.print( escape( this.primaryRuntime ) );
            out.print( "\"/>" ); //$NON-NLS-1$
            out.print( nl );
        }
        
        for( String name : this.targetedRuntimes )
        {
            if( ! name.equals( this.primaryRuntime ) )
            {
                out.print( "  <secondary-runtime name=\"" ); //$NON-NLS-1$
                out.print( escape( name ) );
                out.print( "\"/>" ); //$NON-NLS-1$
                out.print( nl );
            }
        }
        
        for( IProjectFacet f : this.fixed )
        {
            String fid = this.activeFacetAliases.get( f );
            fid = ( fid == null ? f.getId() : fid );
            
            out.print( "  <fixed facet=\"" ); //$NON-NLS-1$
            out.print( escape( fid ) );
            out.print( "\"/>" ); //$NON-NLS-1$
            out.print( nl );
        }
        
        for( IProjectFacetVersion fv : this.facets )
        {
            final IProjectFacet f = fv.getProjectFacet();

            String fid = this.activeFacetAliases.get( f );
            fid = ( fid == null ? f.getId() : fid );
            
            String fverstr = this.activeFacetVersionAliases.get( fv );
            fverstr = ( fverstr == null ? fv.getVersionString() : fverstr );
            
            out.print( "  <installed facet=\"" ); //$NON-NLS-1$
            out.print( escape( fid ) );
            out.print( "\" version=\"" ); //$NON-NLS-1$
            out.print( escape( fverstr ) );
            out.print( "\"/>" ); //$NON-NLS-1$
            out.print( nl );
        }
        
        out.print( "</faceted-project>" ); //$NON-NLS-1$
        out.print( nl );
        
        final byte[] bytes;
        
        try
        {
            bytes = w.getBuffer().toString().getBytes( "UTF-8" ); //$NON-NLS-1$
        }
        catch( UnsupportedEncodingException e )
        {
            // Unexpected. All JVMs are supposed to support UTF-8.
            throw new RuntimeException( e );
        }
        
        final InputStream in = new ByteArrayInputStream( bytes );
        
        if( this.f.exists() )
        {
            validateEdit( this.f );
            this.f.setContents( in, true, false, null );
        }
        else
        {
            final IFolder parent = (IFolder) this.f.getParent();
            
            if( ! parent.exists() )
            {
                parent.create( true, true, null );
            }
            
            this.f.create( in, true, null );
        }
        
        this.fModificationStamp = this.f.getModificationStamp();
        this.parsingException = null;
    }

    public void refresh()
    
        throws CoreException
        
    {
        refresh( false );
    }
    
    private void refresh( final boolean isInitializing )
    
        throws CoreException
        
    {
        synchronized( this.lock )
        {
            if( this.isBeingModified )
            {
                return;
            }
            
            if( this.f.getModificationStamp() == this.fModificationStamp )
            {
                return;
            }
            
            beginModification();
            
            try
            {
                this.facets.clear();
                this.fixed.clear();
                this.unknownFacets.clear();
                this.activeFacetAliases.clear();
                this.activeFacetVersionAliases.clear();
                this.targetedRuntimes.clear();
                this.primaryRuntime = null;
                
                if( ! this.f.exists() )
                {
                    this.fModificationStamp = -1;
                }
                else
                {
                    this.fModificationStamp = this.f.getModificationStamp();
                    
                    Element root = null;
                    
                    try
                    {
                        root = parse( this.f );
                        this.parsingException = null;
                    }
                    catch( Exception e )
                    {
                        this.parsingException = e;
                    }
                    
                    if( this.parsingException == null )
                    {
                        final Element[] elements = children( root );
                        
                        for( int i = 0; i < elements.length; i++ )
                        {
                            final Element e = elements[ i ];
                            final String name = e.getNodeName();
                            
                            if( name.equals( EL_RUNTIME ) )
                            {
                                this.primaryRuntime = e.getAttribute( ATTR_NAME );
                                this.targetedRuntimes.add( this.primaryRuntime );
                            }
                            else if( name.equals( EL_SECONDARY_RUNTIME ) )
                            {
                                this.targetedRuntimes.add( e.getAttribute( ATTR_NAME ) );
                            }
                            else if( name.equals( EL_FIXED ) )
                            {
                                final String id = e.getAttribute( ATTR_FACET );
                                final IProjectFacet f;
                                
                                if( ProjectFacetsManager.isProjectFacetDefined( id ) )
                                {
                                    f = ProjectFacetsManager.getProjectFacet( id );
                                    
                                    if( ! f.getId().equals( id ) )
                                    {
                                        this.activeFacetAliases.put( f, id );
                                    }
                                }
                                else
                                {
                                    f = createUnknownFacet( id );
                                }
                                
                                this.fixed.add( f );
                            }
                            else if( name.equals( EL_INSTALLED ) )
                            {
                                final String id = e.getAttribute( ATTR_FACET );
                                final String version = e.getAttribute( ATTR_VERSION );
                                
                                final IProjectFacet f;
                                
                                if( ProjectFacetsManager.isProjectFacetDefined( id ) )
                                {
                                    f = ProjectFacetsManager.getProjectFacet( id );
                                    
                                    if( ! f.getId().equals( id ) )
                                    {
                                        this.activeFacetAliases.put( f, id );
                                    }
                                }
                                else
                                {
                                    f = createUnknownFacet( id );
                                }
                                
                                final IProjectFacetVersion fv;
                                
                                if( f.hasVersion( version ) )
                                {
                                    fv = f.getVersion( version );
                                    
                                    if( ! fv.getVersionString().equals( version ) )
                                    {
                                        this.activeFacetVersionAliases.put( fv, version );
                                    }
                                }
                                else
                                {
                                    fv = createUnknownFacetVersion( f, version );
                                }
                                    
                                this.facets.add( fv );
                            }
                        }
                    }
                }
            }
            finally
            {
                endModification();
            }
        }

        // If we got here, the project was changed. All of the no-op checks return early. The
        // exception is the case where the faceted project is being initialized for the first
        // time in the workbench session (for instance after workbench restart or when an 
        // existing faceted project is imported into the workspace).
        
        if( ! isInitializing )
        {
            notifyListeners( new ProjectModifiedEvent( this ) );
        }
    }
    
    void markDeleted()
    
        throws CoreException
        
    {
        synchronized( this.lock )
        {
            beginModification();
            
            try
            {
                this.facets.clear();
                this.fixed.clear();
                this.unknownFacets.clear();
                this.targetedRuntimes.clear();
                this.primaryRuntime = null;
            }
            finally
            {
                endModification();
            }
            
            this.isDeleted = true;
        }
    }
    
    private void validateNotDeleted()
    
        throws CoreException
        
    {
        if( this.isDeleted )
        {
            final String msg = Resources.cannotModifyDeletedProject;
            throw new CoreException( FacetCorePlugin.createErrorStatus( msg ) );
        }
    }
    
    private ProjectFacet createUnknownFacet( final String id )
    {
        ProjectFacet f = this.unknownFacets.get( id );
        
        if( f == null )
        {
            f = new ProjectFacet();
            f.setId( id );
            f.setLabel( id );
            f.setDescription( "" ); //$NON-NLS-1$
            
            this.unknownFacets.put( id, f );
        }
        
        return f;
    }
    
    private ProjectFacetVersion createUnknownFacetVersion( final IProjectFacet f,
                                                           final String version )
    {
        final ProjectFacetVersion fv;
        
        if( f.hasVersion( version ) )
        {
            fv = (ProjectFacetVersion) f.getVersion( version );
        }
        else
        {
            fv = new ProjectFacetVersion();
            fv.setProjectFacet( (ProjectFacet) f );
            fv.setVersionString( version );
        }
        
        return fv;
    }
    
    private static Element parse( final IFile f )
    
        throws CoreException, IOException, SAXException
        
    {
        final InputStream stream = f.getContents();
        
        try
        {
            return parse( stream );
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch( Exception e ) {}
        }
    }
    
    private static Element parse( final InputStream stream )
    
        throws IOException, SAXException
        
    {
        final DocumentBuilder docbuilder;
        
        try
        {
            final DocumentBuilderFactory factory 
                = DocumentBuilderFactory.newInstance();
            
            factory.setValidating( false );
            
            docbuilder = factory.newDocumentBuilder();
            
            docbuilder.setEntityResolver
            (
                new EntityResolver()
                {
                    public InputSource resolveEntity( final String publicID, 
                                                      final String systemID )
                    {
                        return new InputSource( new StringReader( "" ) ); //$NON-NLS-1$
                    }
                }
            );
        }
        catch( ParserConfigurationException e )
        {
            throw new RuntimeException( e );
        }

        return docbuilder.parse( stream ).getDocumentElement();
    }

    private Element[] children( final Element element )
    {
        final List<Element> list = new ArrayList<Element>();
        final NodeList nl = element.getChildNodes();
        
        for( int i = 0, n = nl.getLength(); i < n; i++ )
        {
            final Node node = nl.item( i );
            
            if( node.getNodeType() == Node.ELEMENT_NODE )
            {
                list.add( (Element) node );
            }
        }
        
        return list.toArray( new Element[ list.size() ] );
    }
    
    private static boolean equals( final Object obj1,
                                   final Object obj2 )
    {
        if( obj1 == obj2 )
        {
            return true;
        }
        else if( obj1 == null || obj2 == null )
        {
            return false;
        }
        else
        {
            return obj1.equals( obj2 );
        }
    }
    
    private static final class Resources
    
        extends NLS
        
    {
        public static String failedOnInstall;
        public static String failedOnUninstall;
        public static String failedOnVersionChange;
        public static String facetNotSupported;
        public static String illegalModificationMsg;
        public static String tracingDelegateStarting;
        public static String tracingDelegateFinished;
        public static String newPrimaryNotTargetRuntime;
        public static String cannotModifyDeletedProject;
        
        // Task Descriptions
        
        public static String taskValidatingFacetedProject;
        public static String taskInstallingFacet;
        public static String taskUninstallingFacet;
        public static String taskChangingFacetVersion;
        public static String taskConfiguringRuntimes;
        
        // Validation Messages
        
        public static String projectValidationFailed;
        public static String metadataFileCorrupted;
        public static String couldNotReadMetadataFile;
        public static String runtimeNotDefined;
        public static String facetNotSupportedByTarget;
        public static String installedFacetNotFound;
        public static String installedFacetVersionNotFound;
        
        static
        {
            initializeMessages( FacetedProject.class.getName(), 
                                Resources.class );
        }
        
        public static final String bind( final String msg,
                                         final String arg1,
                                         final String arg2,
                                         final String arg3,
                                         final String arg4 )
        {
            return NLS.bind( msg, new Object[] { arg1, arg2, arg3, arg4 } );
        }
    }

}
