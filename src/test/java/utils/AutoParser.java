package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class AutoParser {

	// Check if the specified method is present and uses the specified method call
	public boolean checkMethodPresenceAndUsage(String filePath, String methodName, String methodToSearch)
			throws IOException {
		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;

		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		boolean hasSpecifiedMethod = false;
		boolean usesSpecifiedMethodCall = false;

		// Check for the specified method
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals(methodName)) {
				hasSpecifiedMethod = true;

				// Check if the specified method call is used within the method
				for (MethodCallExpr methodCall : method.findAll(MethodCallExpr.class)) {
					if (methodCall.getNameAsString().equals(methodToSearch)) {
						usesSpecifiedMethodCall = true;
						System.out.println("Method '" + methodName + "' uses '" + methodToSearch + "'.");
						break;
					}
				}
			}
		}

		if (!hasSpecifiedMethod) {
			System.out.println("Method '" + methodName + "' is NOT present.");
		}

		if (hasSpecifiedMethod && !usesSpecifiedMethodCall) {
			System.out.println("Method '" + methodName + "' does NOT use '" + methodToSearch + "'.");
		}

		return hasSpecifiedMethod && usesSpecifiedMethodCall;
	}
}