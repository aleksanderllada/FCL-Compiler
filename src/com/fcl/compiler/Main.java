package com.fcl.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.fcl.compiler.FCLVisitorImpl.ReturnCode;
import com.fcl.compiler.antlr4.FCLLexer;
import com.fcl.compiler.antlr4.FCLParser;

public class Main {

	public static void main(String[] args) {
		
		if(args.length < 2) {
			System.out.println("Invalid number of arguments.");
			System.out.println("Usage: fclcompile file.fcl output.pfcl");
			System.exit(0);
		}
		
		String inFile = args[0];
		String outFile = args[1];
		
		// Read input.
		String code = new String();
		try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	code += line + "\n";
		    }
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Could not open the specified file.");
			System.out.println("Perhaps you should check for read permission.");
			System.exit(0);
		}
		
		// Compile the code.
		FCLLexer lexer = new FCLLexer(new ANTLRInputStream(code));
		FCLParser parser = new FCLParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.chunk();
		FCLVisitorImpl impl = new FCLVisitorImpl();
		Integer result = impl.visit(tree);
		
		if (result == null) {
			result = -1;
		}
		
		if (result != 0) {
			System.out.println("An error has occurred during compilation.");
			System.out.println(ReturnCode.valueOf(result).getMessage());
			System.exit(result);
		}
		
		List<String> instructions = impl.getInstructions();
		Path file = Paths.get(outFile);
		try {
			Files.write(file, instructions, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("An error has occurred when attempting to write to the output file.");
			System.out.println("Perhaps you should check for write permission.");
			e.printStackTrace();
		}
		
		System.out.println("Compilation successful.");
	}

}
