/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.grammar.DlvOutGrammarLexer;
import sk.shanki.lp.grammar.DlvOutGrammarParser;

/**
 *
 * @author shanki
 */
public class DlvOutFactory {
    
    public AnswerSets fromString(String input) throws IOException {
        Reader reader               = new StringReader(input);
        return fromReader(reader);
    }
    
    private AnswerSets fromReader(Reader reader) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(reader);
        DlvOutGrammarLexer lexer = new DlvOutGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DlvOutGrammarParser parser = new DlvOutGrammarParser(tokens);
        ParseTree tree = parser.out();
        DlvOutParseVisitor visitor = new DlvOutParseVisitor();
                      
        return (AnswerSets)visitor.visit(tree);
    }
}
