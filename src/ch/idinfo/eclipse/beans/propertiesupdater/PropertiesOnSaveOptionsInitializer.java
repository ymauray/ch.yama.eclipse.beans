package ch.idinfo.eclipse.beans.propertiesupdater;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer;

public class PropertiesOnSaveOptionsInitializer implements ICleanUpOptionsInitializer {

	public PropertiesOnSaveOptionsInitializer() {
	}

	@Override
	public void setDefaultOptions(CleanUpOptions defaultOptions) {
		defaultOptions.setOption("cleanup.update_properties", CleanUpOptions.TRUE);
	}

}
