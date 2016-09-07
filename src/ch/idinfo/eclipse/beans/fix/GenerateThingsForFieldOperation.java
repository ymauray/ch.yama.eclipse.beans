package ch.idinfo.eclipse.beans.fix;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.internal.ui.text.correction.CorrectionMessages;

@SuppressWarnings("restriction")
public class GenerateThingsForFieldOperation extends CompilationUnitRewriteOperation {

	private final FieldDeclaration m_fieldDeclaration;

	public GenerateThingsForFieldOperation(final FieldDeclaration fieldDeclaration) {
		m_fieldDeclaration = fieldDeclaration;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void rewriteAST(final CompilationUnitRewrite cuRewrite, final LinkedProposalModel model)
			throws CoreException {

		final ASTRewrite rewrite = cuRewrite.getASTRewrite();
		final ICompilationUnit unit = cuRewrite.getCu();
		final CodeGenerationSettings settings = JavaPreferencesSettings
				.getCodeGenerationSettings(unit.getJavaProject());

		final String getterName = FixTools.getGetterName(m_fieldDeclaration);

		final TypeDeclaration decl = (TypeDeclaration) m_fieldDeclaration.getParent();
		final ITypeBinding typeBinding = decl.resolveBinding();

		final ListRewrite listRewrite = rewrite.getListRewrite(decl, decl.getBodyDeclarationsProperty());

		final AST ast = rewrite.getAST();

		final ImportRewrite imports = cuRewrite.getImportRewrite();

		final MethodDeclaration newMethodDecl = ast.newMethodDeclaration();
		final List<IExtendedModifier> modifiers = new ArrayList<>();
		modifiers.addAll(ast.newModifiers(Modifier.PUBLIC));
		newMethodDecl.modifiers().addAll(modifiers);
		newMethodDecl.setName(ast.newSimpleName(getterName));
		newMethodDecl.setConstructor(false);
		newMethodDecl.setReturnType2((Type) rewrite.createCopyTarget(m_fieldDeclaration.getType()));

		listRewrite.insertLast(newMethodDecl,
				createTextEditGroup(CorrectionMessages.GetterSetterCorrectionSubProcessor_additional_info, cuRewrite));
	}

}
