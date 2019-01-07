/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.grammar.ClingoOutGrammarLexer;
import sk.shanki.lp.grammar.ClingoOutGrammarParser;

/**
 *
 * @author shanki
 */
public class ClingoOutFactory {
    
    public AnswerSets fromString(String input) throws IOException {

        try (Reader reader = new StringReader(input)) {
            return fromReader(reader);
        }
    }
    
    public AnswerSets fromReader(Reader reader) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(reader);
        ClingoOutGrammarLexer lexer = new ClingoOutGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ClingoOutGrammarParser parser = new ClingoOutGrammarParser(tokens);
        ParseTree tree = parser.out();
        ClingoOutParseVisitor visitor = new ClingoOutParseVisitor();
                      
        return (AnswerSets)visitor.visit(tree);     
    }
    
    public AnswerSets fromFile(File file) throws FileNotFoundException, IOException {
        try (Reader r = new BufferedReader(new FileReader(file))) {
            
            return fromReader(r);
        }
    }

}