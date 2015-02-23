package br.com.anteros.helpme.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.nebula.widgets.formattedtext.DateFormatter;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.metadata.EntityCache;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;
import br.com.anteros.persistence.session.SQLSession;

public class VariableObject extends TreeNode {

	private Object source;
	private EntityCache entityCache;
	private SQLSession session;
	private boolean resultClass = false;

	public VariableObject(Object source, EntityCache entityCache,
			SQLSession session, boolean resultClass) {
		this.source = source;
		this.entityCache = entityCache;
		this.session = session;
		this.resultClass = resultClass;
	}

	@Override
	public Image getLeftImage() {
		if (isResultClass())
			return AnterosHelpmePlugin.getDefault().getImage(
					AnterosHelpmePlugin.IMG_CLASS);
		else
			return AnterosHelpmePlugin.getDefault().getImage(
					AnterosHelpmePlugin.IMG_COMPONENT);
	}

	public Object getSource() {
		return source;
	}

	public EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	public void initialized() {
		if (entityCache != null) {
			VariableObject vo;
			VariableComponent vc;
			Object fieldValue;
			try {
				if (ReflectionUtils.isImplementsInterface(source.getClass(),
						Collection.class)) {
					Collection sourceCollection = (Collection) source;
					int i = 1;
					for (Object sourceValue : sourceCollection) {
						vo = new VariableObject(sourceValue, entityCache,
								session, false);
						vo.setName(new String[] {
								entityCache.getEntityClass().getSimpleName()
										+ " " + i, "" });
						this.addNode(vo);
						i++;
					}
				} else {
					EntityCache caches[] = { entityCache };
					if (entityCache.isAbstractClass())
						caches = session.getEntityCacheManager()
								.getEntitiesBySuperClassIncluding(entityCache);

					for (EntityCache cache : caches) {
						for (DescriptionField descriptionField : cache
								.getDescriptionFields()) {
							fieldValue = descriptionField
									.getObjectValue(source);
							if (descriptionField.isRelationShip()
									|| descriptionField.isJoinTable()
									|| descriptionField.isCollectionEntity()) {
								vo = new VariableObject(fieldValue,
										descriptionField.getTargetEntity(),
										session, false);
								vo.setName(new String[] {
										descriptionField.getName(), "" });
								this.addNode(vo);
							} else if (descriptionField.isCollectionTable()
									|| descriptionField.isMapTable()) {
								vc = new VariableComponent(fieldValue,
										descriptionField);
								vc.setName(new String[] {
										descriptionField.getName(), "" });
								vc.setInitialized(true);
								this.addNode(vc);
								if (fieldValue != null) {
									if (descriptionField.isCollectionTable()) {
										Iterator it = ((Collection) fieldValue)
												.iterator();
										int i = 1;
										VariableComponent vcValue;
										while (it.hasNext()) {
											Object value = it.next();
											vcValue = new VariableComponent(
													value, descriptionField);
											vcValue.setName(new String[] {
													"Item " + i,
													value.toString() });
											vcValue.setInitialized(true);
											vc.addNode(vcValue);
											i++;
										}
									} else {
										Iterator it = ((Map) fieldValue)
												.keySet().iterator();
										VariableComponent vcValue;
										while (it.hasNext()) {
											Object key = it.next();
											String value = (String) ((Map) fieldValue)
													.get(key);
											vcValue = new VariableComponent(
													value, descriptionField);
											vcValue.setName(new String[] {
													key + "", value });
											vcValue.setInitialized(true);
											vc.addNode(vcValue);
										}
									}
								}
							} else {
								vc = new VariableComponent(fieldValue,
										descriptionField);
								if (descriptionField.isTemporalDate()) {
									DateFormatter df = new DateFormatter();
									df.setValue(fieldValue);
									vc.setName(new String[] {
											descriptionField.getName(),
											df.getDisplayString() });
								} else if (descriptionField.isTemporalDate()) {
									DateTimeFormatter df = new DateTimeFormatter();
									df.setValue(fieldValue);
									vc.setName(new String[] {
											descriptionField.getName(),
											df.getDisplayString() });
								} else if (ReflectionUtils.isExtendsClass(
										Number.class, descriptionField
												.getField().getType())) {
									NumberFormatter nf = new NumberFormatter(
											"###,###,###,##0"
													+ (descriptionField
															.getSimpleColumn()
															.getPrecision() > 0 ? "."
															+ StringUtils
																	.repeat("0",
																			descriptionField
																					.getSimpleColumn()
																					.getPrecision())
															: ""));
									nf.setValue(fieldValue);
									nf.setDecimalSeparatorAlwaysShown(true);
									vc.setName(new String[] {
											descriptionField.getName(),
											nf.getDisplayString() });
								} else {
									vc.setName(new String[] {
											descriptionField.getName(),
											fieldValue + "" });
								}
								this.addNode(vc);
							}
						}
					}
				}
			} catch (Exception e) {
				AnterosHelpmePlugin.error(
						"Não foi possível inicializar os Campos do Objeto "
								+ entityCache.getEntityClass().getName(), e,
						true);
			}
		}
	}

	public boolean isResultClass() {
		return resultClass;
	}

	public void setResultClass(boolean resultClass) {
		this.resultClass = resultClass;
	}

	@Override
	public void uninitialized() {
	}

}
