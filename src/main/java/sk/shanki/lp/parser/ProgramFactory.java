/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.SemanticException;
import sk.shanki.lp.grammar.LpGrammarLexer;
import sk.shanki.lp.grammar.LpGrammarParser;

/**
 *
 * @author shanki
 */
public class ProgramFactory {
        
    public Program fromString(String input) throws SemanticException, IOException {
        Reader reader               = new StringReader(input);
        
        return fromReader(reader);
    }
    
    public Program fromFile(String path) throws IOException, SemanticException {
        FileInputStream stream      = new FileInputStream(path);
        Reader reader               = new InputStreamReader(stream);
        
        return fromReader(reader);
    }
    
    public Program fromReader(Reader reader) throws SemanticException, IOException {
        ANTLRInputStream input = new ANTLRInputStream(reader);
        LpGrammarLexer lexer = new LpGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LpGrammarParser parser = new LpGrammarParser(tokens);
        ParseTree tree = parser.program();
        LpParseVisitor visitor = new LpParseVisitor();
                      
        return visitor.transform(tree);
    }

}