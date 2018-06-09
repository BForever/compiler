package com.compiler;

import com.compiler.assemble.AssembleList;
import com.compiler.assemble.AssembleTranslator;
import com.compiler.cparser.Parser;
import com.compiler.cparser.ast.Ast;
import com.compiler.exception.SemanticError;
import com.compiler.exception.SyntaxError;
import com.compiler.ir.IrList;
import com.compiler.ir.IrTranslator;
import com.compiler.lexer.Lexer;
import com.compiler.semantic.SemanticAnalyzer;
import java.io.FileReader;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        try {
            Parser parser = new Parser(new Lexer(new FileReader(args[0])));
            Ast ast = (Ast) parser.parse().value;
            ast.print();
            SemanticAnalyzer analyzer = new SemanticAnalyzer(ast);
            analyzer.analyse();
            IrTranslator irTranslator = new IrTranslator(ast, analyzer.getSymbolTable());
            IrList irList = irTranslator.translate();
            irList.setWriter(new PrintWriter(args[1]));
            irList.print();
            AssembleTranslator assembleTranslator = new AssembleTranslator(irList);
            AssembleList assembleList = assembleTranslator.translate();
            assembleList.setWriter(new PrintWriter(args[2]));
            assembleList.print();
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
        } catch (SemanticError e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
