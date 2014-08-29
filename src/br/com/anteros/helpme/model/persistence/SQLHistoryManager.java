package br.com.anteros.helpme.model.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.persistence.parameter.NamedParameter;

import com.thoughtworks.xstream.XStream;

public class SQLHistoryManager {

	public static final String SQL_HISTORY = "sqlHistory.xml";
	private List<SQLHistoryPersistent> history = new ArrayList<SQLHistoryPersistent>();

	public List<SQLHistoryPersistent> getHistory() {
		return history;
	}

	public void setHistory(List<SQLHistoryPersistent> history) {
		this.history = history;
	}

	public void addHistory(String project, String resultClass, String sql,
			List<NamedParameter> parameters) {
		history.add(new SQLHistoryPersistent(project, resultClass, sql,
				new Date(), parameters));
	}

	public void load() {
		XStream xStream = new XStream();
		File file = new File(AnterosHelpmePlugin.getDefault()
				.getStateLocation().append(SQL_HISTORY).toOSString());
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				this.setHistory((List) xStream.fromXML(fis));
				fis.close();
			} catch (Exception e) {
				AnterosHelpmePlugin.error(
						"Não foi possível carregar o histórico de SQL'S do arquivo "
								+ file.getName(), e, true);
			}
		}
	}

	public void save() {
		FileOutputStream stream;
		try {
			List<SQLHistoryPersistent> list = this.getHistory();
			if (list != null && list.size() > 0) {
				stream = new FileOutputStream(AnterosHelpmePlugin.getDefault()
						.getStateLocation().append(SQL_HISTORY).toOSString());
				OutputStreamWriter writer = new OutputStreamWriter(stream,
						"utf-8");
				XStream xStream = new XStream();

				xStream.toXML(list, writer);
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.error(
					"Não foi possível salvar o histórico de SQL's.", e, true);
		}
	}
}
