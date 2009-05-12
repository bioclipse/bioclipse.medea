/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/    
 ******************************************************************************/
package net.bioclipse.medea.business;

import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.IBioclipseManager;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.core.domain.ISpectrum;

public interface IMedeaManager extends IBioclipseManager {

    @PublishedMethod(
        params="IMolecule molecule",
        methodSummary="Predicts an EI mass spectrum for the given molecule"
    )
    public ISpectrum predictMassSpectrum(IMolecule molecule);

}