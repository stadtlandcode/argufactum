package de.ifcore.argue.domain.form;

public interface FormStorage
{
	public boolean isStored(Form<?, ?> form);

	/**
	 * @param form
	 * @param entityId
	 *            nullable
	 */
	public void store(Form<?, ?> form, Object entityId);

	public Object getStoredEntityId(Form<?, ?> form);
}
