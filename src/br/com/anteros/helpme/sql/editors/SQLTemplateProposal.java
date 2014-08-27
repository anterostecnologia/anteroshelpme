package br.com.anteros.helpme.sql.editors;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

public class SQLTemplateProposal extends TemplateProposal {

	public SQLTemplateProposal(Template template, TemplateContext context, IRegion region, Image image, int relevance) {
		super(template, context, region, image, relevance);
	}

	public SQLTemplateProposal(Template template, TemplateContext context, IRegion region, Image image) {
		super(template, context, region, image);
	}

	public String getTemplateName() {
		return getTemplate().getName();
	}

}
