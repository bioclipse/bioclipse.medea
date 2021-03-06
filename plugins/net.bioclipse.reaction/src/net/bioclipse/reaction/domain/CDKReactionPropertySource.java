/*******************************************************************************
 * Copyright (c) 2009  Miguel Rojas <miguelrojasch@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.domain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.bioclipse.core.domain.props.BioObjectPropertySource;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IReaction;

public class CDKReactionPropertySource extends BioObjectPropertySource implements PropertyChangeListener {

    protected static final String PROPERTY_DIRECTION = "Direction Reaction";
    protected static final String PROPERTY_NUM_REACTANTS = "Number of reactants";
    protected static final String PROPERTY_NUM_PRODUCTS = "Number of products";
    protected static final String PROPERTY_COLOR = "Color";
    protected static final String PROPERTY_LABEL = "Label";

    private final Object cdkPropertiesTable[][] =
    {
        { PROPERTY_DIRECTION,
            new TextPropertyDescriptor(PROPERTY_DIRECTION,PROPERTY_DIRECTION)},
        { PROPERTY_NUM_REACTANTS,
            new TextPropertyDescriptor(PROPERTY_NUM_REACTANTS,PROPERTY_NUM_REACTANTS)},
        { PROPERTY_NUM_PRODUCTS,
            new TextPropertyDescriptor(PROPERTY_NUM_PRODUCTS,PROPERTY_NUM_PRODUCTS)}
            
            
    };
    
    private final Object boxPropertiesTable[][] =
    {
            { PROPERTY_COLOR,
            	new TextPropertyDescriptor(PROPERTY_COLOR,PROPERTY_COLOR)},
            { PROPERTY_LABEL,
            	new TextPropertyDescriptor(PROPERTY_LABEL,PROPERTY_LABEL)}
    };

    private ArrayList<IPropertyDescriptor> cdkProperties;
    private HashMap<String, Object> cdkValueMap;

    public CDKReactionPropertySource(ReactionObjectModel rModel) {
        super(new CDKReaction(rModel.getIReaction()));
        
        CDKReaction reaction = new CDKReaction(rModel.getIReaction());
        cdkProperties = setupProperties(reaction.getReaction());
        cdkValueMap = getPropertyValues(reaction,rModel);
    }

    /**
     * @param item
     */
    private HashMap<String, Object> getPropertyValues(CDKReaction item,ReactionObjectModel rModel) {
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(
        		PROPERTY_DIRECTION,
        		item.getReaction().getDirection().equals(IReaction.Direction.BIDIRECTIONAL) ?
                        "Bidirectinal" : "Forward"
        );
        valueMap.put(
        		PROPERTY_NUM_REACTANTS,
        		item.getReaction().getReactantCount()
        );
        valueMap.put(
        		PROPERTY_NUM_PRODUCTS,
        		item.getReaction().getProductCount()
        );
        // IChemObject.getProperties()
        Map<Object,Object> objectProps = item.getReaction().getProperties();
        for (Object propKey : objectProps.keySet()) {
            String label = ""+propKey;
            valueMap.put(label, ""+objectProps.get(propKey));
        }
        valueMap.put(
        		PROPERTY_COLOR,
        		ColorConstants.red
        );
        valueMap.put(
        		PROPERTY_LABEL,
        		rModel.getText()
        );
        return valueMap;
    }

    private ArrayList<IPropertyDescriptor> setupProperties(IChemObject object) {
        ArrayList<IPropertyDescriptor> cdkProperties = new ArrayList<IPropertyDescriptor>();

        // default properties
        for (int i=0;i<cdkPropertiesTable.length;i++) {
        	PropertyDescriptor descriptor = (PropertyDescriptor)cdkPropertiesTable[i][1];
            descriptor.setCategory("General");
            cdkProperties.add(descriptor);
        }
        // IChemObject.getProperties()
        Map<Object,Object> objectProps = object.getProperties();
        for (Object propKey : objectProps.keySet()) {
            String label = ""+propKey;
            PropertyDescriptor descriptor = new TextPropertyDescriptor(label,label);
            descriptor.setCategory("Reaction Properties");
            cdkProperties.add(descriptor);
        }
        // IChemObject.getProperties()
        for (int i=0;i<boxPropertiesTable.length;i++) {
        	PropertyDescriptor descriptor = (PropertyDescriptor)boxPropertiesTable[i][1];
        	descriptor.setCategory("Box Properties");
        	cdkProperties.add(descriptor);
        }
        return cdkProperties;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        // Create the property vector.

        IPropertyDescriptor[] propertyDescriptors =
            new IPropertyDescriptor[cdkProperties.size()];
        for (int i=0; i< cdkProperties.size();i++){
            propertyDescriptors[i]=(IPropertyDescriptor) cdkProperties.get(i);
        }

        // Return it.
        return propertyDescriptors;
    }

    public Object getPropertyValue(Object id) {
        if (cdkValueMap.containsKey(id))
            return cdkValueMap.get(id);

        return super.getPropertyValue(id);
    }

    public ArrayList<IPropertyDescriptor> getProperties() {
        return cdkProperties;
    }

    public void setProperties(ArrayList<IPropertyDescriptor> properties) {
        this.cdkProperties = properties;
    }

    public HashMap<String, Object> getValueMap() {
        return cdkValueMap;
    }

    public void setValueMap(HashMap<String, Object> valueMap) {
        this.cdkValueMap = valueMap;
    }
    /*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		
	}

}
