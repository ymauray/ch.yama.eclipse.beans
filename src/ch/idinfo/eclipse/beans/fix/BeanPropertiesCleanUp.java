package ch.idinfo.eclipse.beans.fix;

import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.cleanup.CleanUpContext;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class BeanPropertiesCleanUp implements ICleanUp {

	private CleanUpOptions m_options;

	private RefactoringStatus m_status;

	private HashSet<IResource> m_touchedFiles;

	public BeanPropertiesCleanUp() {
		super();
	}

	@Override
	public RefactoringStatus checkPostConditions(IProgressMonitor monitor) throws CoreException {
		if (this.m_touchedFiles == null) {
			return new RefactoringStatus();
		} else {
			final IProgressMonitor finalMonitor = monitor == null ? new NullProgressMonitor() : monitor;
			finalMonitor.beginTask("", this.m_touchedFiles.size());
			try {
				final RefactoringStatus result = new RefactoringStatus();
				this.m_touchedFiles.forEach(resource -> {
					final IFile file = (IFile) resource;
					result.addInfo("Hello, " + file.getName() + " !");
					finalMonitor.worked(1);
				});
				return result;
			} finally {
				monitor.done();
				this.m_touchedFiles = null;
			}
		}
	}

	@Override
	public RefactoringStatus checkPreConditions(IJavaProject arg0, ICompilationUnit[] arg1, IProgressMonitor arg2)
			throws CoreException {
		if (this.m_options.isEnabled("cleanup.update_properties")) {
			this.m_status = new RefactoringStatus();
		}
		return new RefactoringStatus();
	}

	@Override
	public ICleanUpFix createFix(CleanUpContext context) throws CoreException {
		final CompilationUnit compilationUnit = context.getAST();
		if (compilationUnit == null) {
			return null;
		}

		/*
		 * final ICleanUpFix fix =
		 * BeanPropertiesFix.createCleanup(compilationUnit); if (fix != null) {
		 * if (this.m_touchedFiles == null) { this.m_touchedFiles = new
		 * HashSet<>(); } this.m_touchedFiles.add(((ICompilationUnit)
		 * compilationUnit.getJavaElement()).getResource()); } return fix;
		 */

		return BeanPropertiesFix.createCleanup(compilationUnit);
	}

	@Override
	public CleanUpRequirements getRequirements() {
		final boolean changedRegionsRequired = false;
		final Map<String, String> compilerOptions = null;
		final boolean isUpdateProperties = this.m_options.isEnabled("cleanup.update_properties");

		return new CleanUpRequirements(isUpdateProperties, isUpdateProperties, changedRegionsRequired, compilerOptions);
	}

	@Override
	public String[] getStepDescriptions() {
		if (this.m_options.isEnabled("cleanup.update_properties")) {
			return new String[] { "Update properties" };
		}

		return null;
	}

	@Override
	public void setOptions(CleanUpOptions options) {
		Assert.isLegal(options != null);
		Assert.isTrue(this.m_options == null);
		this.m_options = options;
	}

}
