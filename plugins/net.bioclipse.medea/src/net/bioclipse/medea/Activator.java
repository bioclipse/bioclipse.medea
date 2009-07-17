/*******************************************************************************
 *Copyright (c) 2008 The Bioclipse Team and others.
 *All rights reserved. This program and the accompanying materials
 *are made available under the terms of the Eclipse Public License v1.0
 *which accompanies this distribution, and is available at
 *http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package net.bioclipse.medea;

import net.bioclipse.medea.business.IJavaMedeaManager;
import net.bioclipse.medea.business.IJavaScriptMedeaManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.bioclipse.medea";

    // The shared instance
    private static Activator plugin;

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

        finderTracker = new ServiceTracker(
            context,
            IJavaMedeaManager.class.getName(),
            null
        );
        finderTracker.open();
        jsFinderTracker = new ServiceTracker(
            context,
            IJavaScriptMedeaManager.class.getName(),
            null
        );
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
    
    public IJavaMedeaManager getJavaManager() {
        IJavaMedeaManager manager = null;
        try {
            manager = (IJavaMedeaManager)finderTracker.waitForService(1000*30);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Could not get medea manager", e);
        }
        if(manager == null) {
            throw new IllegalStateException("Could not get medea manager");
        }
        return manager;
    }

    public IJavaScriptMedeaManager getJavaScriptManager() {
        IJavaScriptMedeaManager manager = null;
        try {
            manager = (IJavaScriptMedeaManager)jsFinderTracker.waitForService(1000*30);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Could not get medea manager", e);
        }
        if(manager == null) {
            throw new IllegalStateException("Could not get medea manager");
        }
        return manager;
    }

}
