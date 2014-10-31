package org.summer.sdt.core;

public class TestSelect {
	public static void main(String[] args) {
		
		IJavaProject javaProject= JavaCore.create(project);
		IClasspathEntry[] buildPath= {
		   JavaCore.newSourceEntry(project.getFullPath().append("src")),
		   JavaRuntime.getDefaultJREContainerEntry()
		}; 
		javaProject.setRawClasspath(buildPath, project.getFullPath().append("bin"), null);

		IFolder folder= project.getFolder("src");
		folder.create(true, true, null);

		IPackageFragmentRoot srcFolder= javaProject.getPackageFragmentRoot(folder);
		Assert.assertTrue(srcFolder.exists()); // resource exists and is on build path


		IPackageFragment fragment= srcFolder.createPackageFragment("x.y", true, null);

		String str=
		  "package x.y;"             + "\n" +
		  "public class E  {"        + "\n" +
		  "  String first;"          + "\n" + 
		  "}";
		ICompilationUnit cu= fragment.createCompilationUnit("E.java", str, false, null);
			
		IType type= cu.getType("E");

		type.createField("String name;", null, true, null); 

		
		String content = "public class X {" + "\n" + "  String field;" + "\n"
				+ "}";
		ICompilationUnit cu = fragment.createCompilationUnit("X.java", content,
				false, null);

		int start = content.indexOf("String");
		int length = "String".length();
		IJavaElement[] declarations = cu.codeSelect(start, length);

	}
}
