/*****************************************************************************
 * Copyright (c) 2009 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *****************************************************************************/

package net.bioclipse.reaction.business;

import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static final Logger logger = Logger.getLogger(Activator.class);
	
	// The plug-in ID
	public static final String PLUGIN_ID = "net.bioclipse.reaction";

	// The shared instance
	private static Activator plugin;

    //For Spring
    private ServiceTracker finderTracker;
    private ServiceTracker jsFinderTracker;
    
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
        finderTracker = new ServiceTracker( context, 
        				IJavaReactionManager.class.getName(), 
        				null );

        finderTracker.open();
        jsFinderTracker = new ServiceTracker( context, 
        				IJavaScriptReactionManager.class.getName(), 
        				null );
        
        jsFinderTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	


    public IReactionManager getJavaManager() {
    	IReactionManager manager = null;
        try {
            manager = (IReactionManager) finderTracker.waitForService(1000*10);
        } catch (InterruptedException e) {
            LogUtils.debugTrace(logger, e);
            throw new IllegalStateException("Could not get the Reaction manager: " +
                e.getMessage(), e);
        }
    	
        if(manager == null) {
            throw new IllegalStateException("Could not get the Reaction manager.");
        }
    	return manager;
    }
    
    public IJavaScriptReactionManager getJavaScriptManager() {
    	IJavaScriptReactionManager manager = null;
        try {
            manager = (IJavaScriptReactionManager) jsFinderTracker.waitForService(1000*10);
        } catch (InterruptedException e) {
            LogUtils.debugTrace(logger, e);
        }
        if(manager == null) {
            throw new IllegalStateException("Could not get the CDK manager");
        }
        return manager;
    }
}
