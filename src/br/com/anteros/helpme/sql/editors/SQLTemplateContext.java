package br.com.anteros.helpme.sql.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;

public class SQLTemplateContext extends DocumentTemplateContext {

	public SQLTemplateContext(TemplateContextType type, IDocument document, int offset, int length) {
		super(type, document, offset, length);
	}

	public SQLTemplateContext(TemplateContextType type, IDocument document, Position position) {
		super(type, document, position);
	}

	public TemplateBuffer evaluate(Template template) throws BadLocationException, TemplateException {

		if (!canEvaluate(template))
			throw new TemplateException("");

		SQLTemplateFormatter formatter = new SQLTemplateFormatter(getDocument(), getCompletionOffset());
		String pattern = formatter.format(template.getPattern());
		TemplateTranslator translator = new TemplateTranslator();
		TemplateBuffer buffer = translator.translate(pattern);
		getContextType().resolve(buffer, this);
		return buffer;
	}

}
