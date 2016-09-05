package ch.idinfo.eclipse.beans.propertiessaveaction;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.cleanup.CleanUpContext;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class PropertiesCleanUp implements ICleanUp {

	private CleanUpOptions m_options;

	private RefactoringStatus m_status;

	@Override
	public RefactoringStatus checkPostConditions(IProgressMonitor arg0) throws CoreException {
		try {
			if (m_status == null || m_status.isOK()) {
				return new RefactoringStatus();
			} else {
				return m_status;
			}
		} finally {
			m_status = null;
		}
	}

	@Override
	public RefactoringStatus checkPreConditions(IJavaProject arg0, ICompilationUnit[] arg1, IProgressMonitor arg2)
			throws CoreException {
		if (m_options.isEnabled("cleanup.update_properties")) {
			m_status = new RefactoringStatus();
		}
		return new RefactoringStatus();
	}

	@Override
	public ICleanUpFix createFix(CleanUpContext context) throws CoreException {
		final CompilationUnit compilationUnit = context.getAST();
		if (compilationUnit == null) {
			return null;
		}

		return null;
	}

	@Override
	public CleanUpRequirements getRequirements() {
		final boolean changedRegionsRequired = false;
		final Map<String, String> compilerOptions = null;
		final boolean isUpdateProperties = m_options.isEnabled("cleanup.update_properties");

		return new CleanUpRequirements(isUpdateProperties, isUpdateProperties, changedRegionsRequired, compilerOptions);
	}

	@Override
	public String[] getStepDescriptions() {
		if (m_options.isEnabled("cleanup.update_properties")) {
			return new String[] { "Update properties" };
		}

		return null;
	}

	@Override
	public void setOptions(CleanUpOptions options) {
		Assert.isLegal(options != null);
		Assert.isTrue(m_options == null);
		m_options = options;
	}

}
