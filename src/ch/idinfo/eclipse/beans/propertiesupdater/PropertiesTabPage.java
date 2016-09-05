package ch.idinfo.eclipse.beans.propertiesupdater;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpConfigurationUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

public class PropertiesTabPage implements ICleanUpConfigurationUI {

	private CleanUpOptions m_options;

	public PropertiesTabPage() {
		super();
	}

	@Override
	public Composite createContents(Composite parent) {
		final SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setFont(parent.getFont());

		return sashForm;
	}

	@Override
	public int getCleanUpCount() {
		return 1;
	}

	@Override
	public String getPreview() {
		return "My Preview !";
	}

	@Override
	public int getSelectedCleanUpCount() {
		return m_options.isEnabled("cleanup.update_properties") ? 1 : 0;
	}

	@Override
	public void setOptions(CleanUpOptions options) {
		m_options = options;
	}

}
