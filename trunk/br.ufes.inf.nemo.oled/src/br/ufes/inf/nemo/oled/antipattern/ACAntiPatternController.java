package br.ufes.inf.nemo.oled.antipattern;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.ufes.inf.nemo.antipattern.ACAntiPattern;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.oled.model.AlloySpecification;
import br.ufes.inf.nemo.oled.model.UmlProject;
import br.ufes.inf.nemo.oled.ui.ModelTree;
import br.ufes.inf.nemo.ontouml2alloy.Onto2AlloyOptions;

/**
 * @author John Guerson
 */

public class ACAntiPatternController {

	private ACAntiPatternPane acView;
	private ACAntiPattern acModel;
	
	/**
	 * Constructor.
	 * 
	 * @param acView
	 * @param acModel
	 */
	public ACAntiPatternController(ACAntiPatternPane acView, ACAntiPattern acModel)
	{
		this.acView = acView;
		this.acModel = acModel;		
		
		acView.addExecuteWithAnalzyerListener(new ExecuteWithAnalzyerListener());
		acView.addOCLSolutionListener(new OCLSolutionListener());
	}
		
	/**
	 * Execute With Analyzer Action Listener
	 * 
	 * @author John
	 */
	class ExecuteWithAnalzyerListener implements ActionListener 
	{
	    @SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) 
	    {			
	    	try{
	    		String predicates = new String();
	    		OntoUMLParser refparser = ModelTree.getParserFor(acView.getFrame().getDiagramManager().getCurrentProject());
	    		UmlProject project = acView.getFrame().getDiagramManager().getCurrentProject();
	    		
	    		if(acView.isSelectedOpenCycle()) 
	    		{
	    			predicates += "\n\n"+acModel.generatePredicate(
	    				refparser, acView.getScope(), acModel.OPEN
	    			);
	    		}
			
	    		if(acView.isSelectedClosedCycle())				
	    		{
	    			predicates += "\n\n"+acModel.generatePredicate(
	    				refparser, acView.getScope(), acModel.CLOSED
	    			); 
	    		}
				
	    		/*=======================================================*/
	    		
	    		Onto2AlloyOptions refOptions = ModelTree.getOntoUMLOptionsFor(project);
	    		AlloySpecification alloymodel = ModelTree.getAlloySpecFor(project);
	    		
	    		//set parser...
	    		acModel.setSelected(refparser);
	    		
	    		// set options to false, because the simulated model is partial
	    		refOptions.identityPrinciple = false;
	    		refOptions.relatorAxiom = false;
	    		refOptions.weakSupplementationAxiom = false;
	    		refOptions.antiRigidity = false;
	    		
	    		// set alloy path
	    		String alsPath = project.getTempDir()+"ac.als";	    		
	    		alloymodel.setAlloyModel(alsPath);
	    		
	    		// set alloy model from ontoUML transformation
	    		alloymodel.setAlloyModel(refparser,refOptions);	    		
	    		String content = alloymodel.getContent();
	    		
	    		// add predicates to alloy content
	    		content = content+"\n"+predicates;	    		
	    		alloymodel.setContent(content);
	    		
	    		// open alloy model
	    		acView.getFrame().getDiagramManager().openAlloyAnalyzer(true,-1);
	    			    		
	    		/*=======================================================*/
	    		
	    	}catch(Exception exception){
	    		acView.getFrame().showErrorMessageDialog("Executing AC AntiPattern",exception.getMessage());
	    	}
	    	
	    }
	}
	
	/**
	 * Generate OCL Solution
	 * 
	 * @author John
	 */
	class OCLSolutionListener implements ActionListener 
	{
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) 
	    {
			Boolean openCycle = acView.isSelectedOpenCycle();
			Boolean closedCycle = acView.isSelectedClosedCycle();

			String constraints = new String();
			
			OntoUMLParser refparser = ModelTree.getParserFor(acView.getFrame().getDiagramManager().getCurrentProject());
			
			if(openCycle) 
				constraints = acModel.generateCycleOcl(acModel.OPEN, refparser)+"\n\n";		
			if(closedCycle)
				constraints += acModel.generateCycleOcl(acModel.CLOSED, refparser)+"\n\n";		
							
			acView.getFrame().getDiagramManager().getCurrentWrapper().addConstraints(constraints);
	    }
	}
	
}
