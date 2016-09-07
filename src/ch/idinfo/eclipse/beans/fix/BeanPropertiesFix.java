package ch.idinfo.eclipse.beans.fix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.corext.dom.GenericVisitor;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;

@SuppressWarnings("restriction")
public class BeanPropertiesFix extends CompilationUnitRewriteOperationsFix {

	private static final class FieldFinder extends GenericVisitor {

		private final List<CompilationUnitRewriteOperation> m_operations;

		private final Set<String> m_allowedTypes = new HashSet<>();

		private final List<String> FIELD_PREFIXES;

		public FieldFinder(final ArrayList<CompilationUnitRewriteOperation> operations) {
			m_operations = operations;
			FIELD_PREFIXES = Arrays.asList(JavaCore.getOption(JavaCore.CODEASSIST_FIELD_PREFIXES).split(","));
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean visit(final TypeDeclaration node) {
			final Javadoc javadoc = node.getJavadoc();
			final boolean isBean = javadoc == null ? false
					: ((List<TagElement>) javadoc.tags()).stream()
							.anyMatch(tagElement -> tagElement.getTagName().equals("@idbean"));
			if (isBean) {
				final String typeName = node.getName().getFullyQualifiedName();
				m_allowedTypes.add(typeName);
			}
			return super.visit(node);
		}

		@Override
		public boolean visit(final FieldDeclaration fieldDeclaration) {
			@SuppressWarnings("unchecked")
			final List<VariableDeclarationFragment> variableDeclarationFragments = fieldDeclaration.fragments();
			if (variableDeclarationFragments != null || variableDeclarationFragments.size() == 1) {
				final ASTNode parent = fieldDeclaration.getParent();
				if (parent instanceof TypeDeclaration) {
					final String parentName = ((TypeDeclaration) parent).getName().getFullyQualifiedName();
					if (m_allowedTypes.contains(parentName)) {
						final boolean match = FIELD_PREFIXES.stream().anyMatch(
								prefix -> variableDeclarationFragments.get(0).getName().toString().startsWith(prefix));
						if (match) {
							m_operations.add(new GenerateThingsForFieldOperation(fieldDeclaration));
						}
					}
				}
			}
			return super.visit(fieldDeclaration);
		}

		@Override
		public void endVisit(final FieldDeclaration node) {
			super.endVisit(node);
		}

		@Override
		public void endVisit(final TypeDeclaration node) {
			super.endVisit(node);
		};
	}

	public static ICleanUpFix createCleanup(final CompilationUnit compilationUnit) {
		final ArrayList<CompilationUnitRewriteOperation> operations = new ArrayList<>();
		final FieldFinder finder = new FieldFinder(operations);
		compilationUnit.accept(finder);

		return new BeanPropertiesFix("Fixing bean", compilationUnit,
				operations.toArray(new CompilationUnitRewriteOperation[operations.size()]));
	}

	/*
		static ICleanUpFix createCleanup(final CompilationUnit compilationUnit) {
	
	final List<FieldDeclaration> operations = new ArrayList<>();
	final FieldFinder finder = new FieldFinder(operations);compilationUnit.accept(finder);if(operations.isEmpty())
	{
			return null;
		}
	
	return null;
	}
	
	@Override
	public CompilationUnitChange createChange(final IProgressMonitor progressMonitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}*/

	public BeanPropertiesFix(final String name, final CompilationUnit compilationUnit,
			final CompilationUnitRewriteOperation[] operations) {
		super(name, compilationUnit, operations);
	}

}
