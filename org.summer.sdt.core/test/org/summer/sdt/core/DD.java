package org.summer.sdt.core;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;
import org.summer.sdt.core.dom.AST;
import org.summer.sdt.core.dom.ASTParser;
import org.summer.sdt.core.dom.CompilationUnit;
import org.summer.sdt.core.dom.ImportDeclaration;

public class DD {
	public static void main(String[] args) throws MalformedTreeException, BadLocationException {
		Document document = new Document("import java.util.List;\nclass X {}\n");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(document.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.recordModifications();
		AST ast = cu.getAST();
		ImportDeclaration id = ast.newImportDeclaration();
		id.setName(ast.newName(new String[] { "java", "util", "Set" }));
		cu.imports().add(id); // add import declaration at end
		TextEdit edits = cu.rewrite(document, null);
		UndoEdit undo = edits.apply(document);
		System.out.println(document.get());
	}
}
