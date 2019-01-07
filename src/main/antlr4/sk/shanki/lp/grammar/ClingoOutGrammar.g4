/*
 * Copyright shanki. All rights reserved.
 */

grammar ClingoOutGrammar;

out : answer_sets sat ;

answer_sets : (answer_set NL)* ;

answer_set : literal*;

sat : 'SATISFIABLE' NL         # sat_sat
    | 'UNSATISFIABLE' NL       # unsat_sat
    ;

literal     : atom             # positive_literal
            | '-' atom         # negative_literal
            ;

atom        : predicateSymbol=SMALL_ID ('(' terms ')')? ;

terms       : term (',' term)* ;

term        : NUMBER									# number_term
            | STRING									# string_term
            | functionSymbol=SMALL_ID ('(' terms ')')?	# functional_term
            ;

NL : '\r'? '\n';

SMALL_ID	: [a-z] (ID_LETTER | DIGIT)* ;

fragment ID_LETTER	: 'a'..'z' | 'A'..'Z' | '_' ;
fragment DIGIT		: '0'..'9' ;

NUMBER		: DIGIT+
            | DIGIT+ '.' DIGIT+
			| '.' DIGIT+
			;

STRING		: '"' (ESC | .)*? '"';
fragment ESC    : '\\' [btnr"\\] ;

// toto som doplnil lebo hadzalo warning na medzerach
WS              : [ \t]+ -> skip;