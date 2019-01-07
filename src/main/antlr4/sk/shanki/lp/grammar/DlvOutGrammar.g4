/*
 * Copyright shanki. All rights reserved.
 */

grammar DlvOutGrammar;

out : answer_sets ;

answer_sets : as* ;

as : answer_set                 # classical_as
   | optimal_answer_set         # optimal_as
   ;

optimal_answer_set : HEADER answer_set cost;
answer_set : '{' (literal (',' literal)*)? '}' ;

HEADER: 'Best model:';

cost: 'Cost ([Weight:Level]):' '<[' NUMBER ':' NUMBER ']>';

literal     : atom             # positive_literal
            | '-' atom         # negative_literal
            ;

atom        : predicateSymbol=SMALL_ID ('(' terms ')')? ;

terms       : term (',' term)* ;

term        : NUMBER									# number_term
            | STRING									# string_term
            | functionSymbol=SMALL_ID ('(' terms ')')?	# functional_term
            ;

SMALL_ID	: [a-z] (ID_LETTER | DIGIT)* ;

fragment ID_LETTER	: 'a'..'z' | 'A'..'Z' | '_' ;
fragment DIGIT		: '0'..'9' ;

NUMBER		: DIGIT+
            | DIGIT+ '.' DIGIT+
			| '.' DIGIT+
			;

STRING		: '"' (ESC | .)*? '"';
fragment ESC    : '\\' [btnr"\\] ;

WS              : [ \t\r\n]+ -> skip;