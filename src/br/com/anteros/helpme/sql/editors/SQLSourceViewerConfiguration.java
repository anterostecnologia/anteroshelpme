package br.com.anteros.helpme.sql.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;

import br.com.anteros.helpme.model.Project;


public class SQLSourceViewerConfiguration extends SourceViewerConfiguration {
	public final static String ANTEROS_PARTITIONING= "__anteros_partitioning";   
	
	public Project project;
	private ContentAssistant assistant;
	private SQLCompletionProcessor completionProcessor;
	private SQLColorManager colorManager;
	private SQLDoubleClickStrategy doubleClickStrategy;
	private PresentationReconciler reconciler;
	private SQLKeywordScanner keyWordScanner;
	
	public SQLSourceViewerConfiguration(Project project, SQLColorManager colorManager) {
		this.project = project;
		this.colorManager = colorManager;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		assistant = new ContentAssistant();
		completionProcessor = new SQLCompletionProcessor(project);
		assistant.setContentAssistProcessor(completionProcessor, IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		assistant.enableAutoInsert(true);

		return assistant;
	}

	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		return super.getContentFormatter(sourceViewer);
	}

	protected ISQLTokenScanner getSQLKeywordScanner() {
		if (keyWordScanner == null) {
			keyWordScanner = new SQLKeywordScanner(project,colorManager);
		} else {
			keyWordScanner.initialize();
		}
		return keyWordScanner;
	}


	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {IDocument.DEFAULT_CONTENT_TYPE, SQLPartitionScanner.SQL_COMMENT, SQLPartitionScanner.SQL_STRING};
	}

	static class SingleTokenScanner extends BufferedRuleBasedScanner {

		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	};

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		reconciler = new PresentationReconciler();
		initializeDamagerRepairer();
		return reconciler;
	}

	public void updatePreferences(IDocument document) {

		getSQLKeywordScanner().initialize();

		NonRuleBasedDamagerRepairer commentDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(new RGB(0,128,0))));
		commentDR.setDocument(document);

		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		NonRuleBasedDamagerRepairer stringDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(new RGB(0,0,255))));
		stringDR.setDocument(document);

		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);


	}

	private void initializeDamagerRepairer() {
		DefaultDamagerRepairer commentDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(new RGB(0,128,0)))));
		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		DefaultDamagerRepairer stringDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(new RGB(0,0,255)))));
		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		DefaultDamagerRepairer keywordDR = new DefaultDamagerRepairer(getSQLKeywordScanner());
		reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);

	}
	
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new SQLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		completionProcessor.setProject(project);
		keyWordScanner.setProject(project);		
	}

	public ContentAssistant getAssistant() {
		return assistant;
	}

}
