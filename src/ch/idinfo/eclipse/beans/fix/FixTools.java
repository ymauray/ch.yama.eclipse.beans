package ch.idinfo.eclipse.beans.fix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public final class FixTools {

	private static final List<String> FIELD_PREFIXES = new ArrayList<>();

	static {
		final String fieldPrefixes = JavaCore.getOption(JavaCore.CODEASSIST_FIELD_PREFIXES);
		if (fieldPrefixes != null) {
			FIELD_PREFIXES.addAll(Arrays.asList(fieldPrefixes.split(";")));
		}
	}

	private FixTools() {
	}

	public static String getFieldName(final FieldDeclaration fieldDeclaration) {
		final VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fieldDeclaration
				.fragments().get(0);
		final String fieldName = variableDeclarationFragment.getName().toString();
		final Optional<String> prefix = FIELD_PREFIXES.stream().filter(fieldName::startsWith).findFirst();
		final String strippedFieldName = prefix.isPresent() ? fieldName.substring(prefix.get().length()) : fieldName;
		return FixTools.withFirstLetterLowerCase(strippedFieldName);
	}

	public static String getGetterName(final FieldDeclaration fieldDeclaration) {
		final String fieldName = FixTools.getFieldName(fieldDeclaration);
		final String prefix = fieldDeclaration.getType().getNodeType() == ASTNode.BOOLEAN_LITERAL ? "is" : "get";
		return prefix + FixTools.withFirstLetterUpperCase(fieldName);
	}

	public static final String withFirstLetterLowerCase(final String string) {
		return withFirstLetter(string, String::toLowerCase);
	}

	public static final String withFirstLetterUpperCase(final String string) {
		return withFirstLetter(string, String::toUpperCase);
	}

	interface IWithFirstLetter {
		String apply(String string);
	}

	private static final String withFirstLetter(final String string, final IWithFirstLetter operation) {
		return operation.apply(string.substring(0, 1)) + (string.length() > 1 ? string.substring(1) : "");
	}
}
