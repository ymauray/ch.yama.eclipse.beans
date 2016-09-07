package ch.idinfo.eclipse.beans.preferences.cleanup;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer;

public class SaveActionCleanUpOptionsInitializer implements ICleanUpOptionsInitializer {

	public SaveActionCleanUpOptionsInitializer() {
	}

	@Override
	public void setDefaultOptions(CleanUpOptions defaultOptions) {
		defaultOptions.setOption("cleanup.update_properties", CleanUpOptions.TRUE);
	}

}
