package br.com.anteros.helpme.sql.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

public class SQLDocumentSetupParticipant implements IDocumentSetupParticipant {
	private IDocumentPartitioner partitioner;
	
	public void setup( IDocument document ) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			partitioner = new FastPartitioner( new SQLPartitionScanner(), SQLPartitionScanner.SQL_PARTITION_TYPES );
            partitioner.connect( document );
			extension3.setDocumentPartitioner( SQLSourceViewerConfiguration.ANTEROS_PARTITIONING, partitioner );
		}
	}

	public void unsetup() {
		partitioner.disconnect();
	}
}
