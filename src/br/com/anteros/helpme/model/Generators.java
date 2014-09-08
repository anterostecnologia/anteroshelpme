package br.com.anteros.helpme.model;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.SchemaManager;
import br.com.anteros.persistence.schema.definition.GeneratorSchema;
import br.com.anteros.persistence.schema.definition.SequenceGeneratorSchema;

public class Generators extends TreeNode {

	public static final String[] GENERATORS = new String[]{"Generators"};
	
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_GENERATOR);
	}
	
	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Generator))
			throw new ModelException("O objeto Generators aceita apenas objetos do tipo Generator. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}
	
	@Override
	public String[] getName() {
		return GENERATORS;
	}
	
	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();		
			SchemaManager schemaManager = ((Project)getParent().getParent()).getSchemaManager();
			Set<GeneratorSchema> sequences = schemaManager.getSequences();
			for (GeneratorSchema g : sequences) {
				if (g instanceof SequenceGeneratorSchema){
				Generator generator = new Generator((SequenceGeneratorSchema)g);
				generator.setName(new String[]{g.getName()});
				this.addNode(generator);
				}
			}
			this.setInitialized(true);			
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error(
					"Não foi possível inicializar os Generator's(Sequences) do banco de dados.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}