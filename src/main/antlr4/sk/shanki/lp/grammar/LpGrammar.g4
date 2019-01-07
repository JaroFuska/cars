/*
 * Copyright shanki. All rights reserved.
 */

grammar LpGrammar;

program	: statement* ;

statement   : lprule            # rule_statement
            | constraint        # constraint_statement
            | weak_constraint   # weak_constraint_statement
            | preference        # preference_statement
            ;

preference  : r1=SMALL_ID '<' r2=SMALL_ID '.';

lprule      : nameannotation? partialannotation? head ( ':-' body )? '.' ;

constraint  : ':-' body? '.';

weak_constraint : ':~' body? '.' '[' weight=NUMBER '@' level=NUMBER (',' terms)? ']' ; 

nameannotation : '@name(' name=SMALL_ID ')' ;
partialannotation: '@partial' ;

head 	: nafliteral ('v' nafliteral)*;

body	: nafliteral (',' nafliteral)*;

nafliteral  : literal           # positive_nafliteral
            | 'not' literal     # negative_nafliteral
            ;

literal     : atom             # positive_literal
            | '-' atom         # negative_literal
            ;

atom        : predicateSymbol=SMALL_ID ('(' terms ')')? ;

terms       : term (',' term)* ;

term        : NUMBER									# number_term
            | STRING									# string_term
            | variable=BIG_ID							# variable_term
			| functionSymbol=SMALL_ID ('(' terms ')')?	# functional_term
            ;

SMALL_ID	: [a-z] (ID_LETTER | DIGIT)* ;

BIG_ID          : [A-Z] (ID_LETTER | DIGIT)* ;

fragment ID_LETTER	: 'a'..'z' | 'A'..'Z' | '_' ;
fragment DIGIT		: '0'..'9' ;

NUMBER		: DIGIT+
            | DIGIT+ '.' DIGIT+
			| '.' DIGIT+
			;

COMMENT		: '%' .*? '\r'? '\n' -> skip;
MULTILINE_COMMENT: '%*' .*? '*%' -> skip;

WS              : [ \t\r\n]+ -> skip;

STRING		: '"' (ESC | .)*? '"';
fragment ESC    : '\\' [btnr"\\] ;