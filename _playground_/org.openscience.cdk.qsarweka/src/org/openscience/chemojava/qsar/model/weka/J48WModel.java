/* $Revision: 6228 $ $Author: egonw $ $Date: 2006-05-11 18:34:42 +0200 (Thu, 11 May 2006) $
 *
 * Copyright (C) 2006-2007  Miguel Rojas <miguel.rojas@uni-koeln.de>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.chemojava.qsar.model.weka;

import java.io.InputStream;

import org.openscience.cdk.qsar.model.QSARModelException;
import org.openscience.chemojava.libio.weka.Weka;

import weka.classifiers.trees.J48;
/** 
 * A modeling class that provides the Quinlan's model C4.5 known as J48 
 * using Weka library.
 * The use of this class is shown in the following code snippet
 * <pre>
 * try {
 *     J48WModel j48 = new J48WModel(typAttrib,classAttrib,x,y); or J48WModel j48 = new J48WModel(typAttrib,classAttrib,x,y,attrib);
 *     j48.setOptions(options);
 *     j48.build();
 *     j48.setParameters(newX);
 *     j48.predict();
 * 	   String[] predictedvalues = (String[])j48.getPredictPredicted();
 * } catch (QSARModelException qme) {
 *     System.out.println(qme.toString());
 * }
 * </pre>
 * Other option is set the data from a file format arff.
 * <pre>
 * J48WModel j48 = new J48WModel("/some/where/dataTraining.arff");
 * </pre>
 * Note that when making predictions, the new X matrix must be set by calls
 * to setParameters(). The following table lists the parameters that can be set and their 
 * expected types. 
 * <center>
 * <table border=1 cellpadding=5>
 * <THEAD>
 * <tr>
 * <th>Name</th><th>Java Type</th><th>Notes</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>x</td><td>Double[][]</td><td></td>
 * </tr>
 * <tr>
 * <td>y</td><td>Double[]</td><td>Length should be equal to the rows of x</td>
 * </tr>
 * <tr>
 * <td>newX</td><td>Double[][]</td><td>Number of columns should be the same as in x</td>
 * </tr>
 * <tr>
 * <td>typAttrib</td><td>String[]</td><td>Length should be equal to the rows of x</td>
 * </tr>
 * <tr>
 * <td>classAttrib</td><td>String[]</td><td>Length should be equal to number of different classes</td>
 * </tr>
 * </tbody>
 * </table>
 * </center>
 * <p>Valid options are (according weka library):</p>
 * <p>-U: Use unpruned tree.</p>
 * <p>-C confidence: Set confidence threshold for pruning. (Defalult:0.25)</p>
 * <p>-M number: Set minimum number of instances per leaf.(Default 2)</p>
 * <p>-R: Use reduced error pruning. No subte raising is performed.</p>
 * <p>-N number: Set number of folds for reduced error pruning. One fold is used
 *  as the pruning set.(Deafult:3)</p>
 * <p>-B: Use binary splits for nominal attributes</p>
 * <p>-S: Don't perform subtree raising</p>
 * <p>-L: Do not clean up alfter the tree has been built</p>
 * <p>-A: If set, Laplace smoothing is used for predicted probabilities</p>
 * <p>-Q:The seed for reduced-error pruning</p>
 *
 * @author      Miguel Rojas
 * @cdk.require weka.jar
 * @cdk.license GPL
 * @cdk.module  qsarweka
 * @cdk.svnrev  $Revision: 9162 $
 * @see Weka
 * 
 * @cdk.keyword decision and regression trees
 * @cdk.keyword J48
 */
public class J48WModel implements IWekaModel{
	/**Dependent variable */
	private Object[] y;
	/**Independent variable */
	private Object[][] x;
	private Weka weka = null;
	/**Array of strings containing the options*/
	private String[] options;
	/**A String specifying the path of the file, format arff,
	 * which contians the variables and attributes with whose to test.*/
	private InputStream table = null;
	/** results of the prediction*/
	private String[] results = null;
	/**A Array Object containing the independent variable*/
	private Object[][] newX = null;
	/**A String specifying the path of the file, format arff,
	 * which contians the independent values with whose to predict.*/
	private String pathNewX = null;
	/**Attribute type: NUMERICAL or NOMINAL*/
	private int[] typAttrib;
	/**String with the attribut class*/
	private String[] classAttrib;
	/** String with the attributs*/
	private String[] attrib;
	/** Boolean if the attributs was set*/
	private boolean setAttrib = false;

	/**
	 * Constructor of the J48WModel object from varibles
	 * 
	 * @param typAttrib   Attribute type: NUMERICAL or NOMINAL.
	 * @param classAttrib String with the attribut class.
	 * @param y  An array containing the dependent variable.
	 * @param x  An double array containing the independent variable.
	 */
	public J48WModel(int[] typAttrib, String[] classAttrib, Object[] y, Object[][] x){
		this.typAttrib = typAttrib;
		this.classAttrib = classAttrib;
		this.y = y;
		this.x = x;
	}

	/**
	 * Constructor of the J48WModel object from varibles
	 * 
	 * @param typAttrib   Attribute type: NUMERICAL or NOMINAL.
	 * @param classAttrib String with a list of the attribut class.
	 * @param y  An array containing the dependent variable (class value).
	 * @param x  A 2D array containing the independent variable (for example: qsar results).
	 * @param attrib A string array containing the attributs
	 */
	public J48WModel(int[] typAttrib, String[] classAttrib, Object[] y, Object[][] x, String[] attrib){
		this.typAttrib = typAttrib;
		this.classAttrib = classAttrib;
		this.y = y;
		this.x = x;
		this.attrib = attrib;
		setAttrib = true;
	}
	/**
	 * Constructor of the J48WModel object from file. Default the file is found into cdk.src
	 * 
	 * @param True, if the file is found into cdk.src resource 
	 * @param pathTest Path of the dataset file format arff to train
	 */
	public J48WModel(InputStream table){
		this.table  = table;
	}

	/**
	 * Parses a given list of options. The parameters are determited from weka. 
	 * 
	 * <p>Valid options are (according weka library):</p>
	 * <p>-U: Use unpruned tree.</p>
	 * <p>-C confidence: Set confidence threshold for pruning. (Defalult:0.25)</p>
	 * <p>-M number: Set minimum number of instances per leaf.(Default 2)</p>
	 * <p>-R: Use reduced error pruning. No subte raising is performed.</p>
	 * <p>-N number: Set number of folds for reduced error pruning. One fold is used
	 *  as the pruning set.(Deafult:3)</p>
	 * <p>-B: Use binary splits for nominal attributes</p>
	 * <p>-S: Don't perform subtree raising</p>
	 * <p>-L: Do not clean up alfter the tree has been built</p>
	 * <p>-A: If set, Laplace smoothing is used for predicted probabilities</p>
	 * <p>-Q:The seed for reduced-error pruning</p>
	 *
	 * @param options An Array of strings containing the options 
	 * @throws QSARModelException if the options are of the wrong type for the given modeling function
	 * 
	 */
	public void setOptions(String[] options) throws QSARModelException {
		this.options = options;
	}
	/**
	 * Get the current settings of the classifier. The parameters are determited from weka. And are specific for each
	 * algorithm.
	 *
	 * @return An Array of strings containing the options 
	 * @throws QSARModelException if the options are of the wrong type for the given modeling function
	 * 
	 */
	public String[] getOptions() throws QSARModelException {
		return options;
	}
	/**
	 * Builds (trains) the model.
	 *
	 * @throws QSARModelException if errors occur in data types, calls to the R session. See
	 * the corresponding method in subclasses of this class for further details.
	 */
	public void build() throws QSARModelException {
		weka = new Weka();
		try {
			J48 j48 = new J48();
			if(options != null)
				j48.setOptions(options);

			if(table != null){
			    weka.setDataset(table,
			        j48
			    );
			}else{
				if (!(setAttrib)){
					this.attrib = new String[x[0].length];
					for(int i = 0 ; i < x[0].length; i++){
						attrib[i] = "X"+i;
					}
				}
				weka.setDataset(attrib,typAttrib,classAttrib,y,x,j48);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Specifies the parameters to predict. In this case will be the dependent varibles.
	 * It's found into cdk.src
	 * 
	 * @param  path  A String specifying the path of the file, format arff, which contians 
	 * 				 the dependent values with whose to predict. It's found into cdk.src
	 * @throws QSARModelException if the parameters are of the wrong type for the given modeling function
	 * 
	 */
	public void setParametersCDK(String path) throws QSARModelException {
		this.pathNewX = path;
	}
	/**
	 * Specifies the parameters to predict. In this case will be the independent varibles.
	 * 
	 * @param  newX  A 2D array Object containing the independent variable.
	 * @throws QSARModelException if the parameters are of the wrong type for the given modeling function
	 */
	public void setParameters(Object[][] newX) throws QSARModelException {
		this.newX = newX;
	}

	/**
	 * Makes predictions using a previously built model.
	 *
	 * @throws QSARModelException if errors occur in data types, calls to the R session. See
	 * the corresponding method in subclasses of this class for further details.
	 */
	public void predict() throws QSARModelException {
		try{
			if(pathNewX != null){
				Object[] object = weka.getPredictionCDK(pathNewX);
				results = new String[object.length];
				for(int i = 0 ; i < object.length; i++){
					results[i] = (String)object[i];
				}
			}
			else if(newX != null){
				Object[] object = weka.getPrediction(newX);
				results = new String[object.length];
				for(int i = 0 ; i < results.length; i++){
					results[i] = (String)object[i];
				}
			}

		} catch ( Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Returns the predicted values for the prediction set. 
	 *
	 * This function only returns meaningful results if the <code>predict</code>
	 * method of this class has been called.
	 *
	 * @return A String[] containing the predicted values
	 */
	public Object[] getPredictPredicted() {
		return results;
	}

}
