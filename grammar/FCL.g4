grammar FCL;

chunk
    : block EOF
    ;

block
    : stat* retstat?
    ;

stat
    : ';'                                                                                  #semicolonStat
    | 'break'                                                                              #breakStat
    | 'while' exp 'do' ':' block 'end'                                                     #whileStat
    | 'for' Identifier '=' positiondt ('step' numberdt)? 'do' ':' block 'end'              #forStat
    | 'foreach' Identifier ('=' listdt)? 'as' Identifier 'do' ':' block 'end'              #foreachStat
    | 'if' exp 'then' ':' block ('elseif' exp 'then' ':' block)* ('else' ':' block)? 'end' #ifStat
    | procedurecall                                                                        #procedurecallStat
    | procedure                                                                            #procedureStat
    | attr                                                                                 #attrStat
    | printexp                                                                             #printexpStat
    | 'move' 'from' exp 'to' exp                                                           #moveStat
    | 'pickup' exp 'pippets' 'from' exp                                                    #pickupStat
    | 'discard'                                                                            #discardStat
    ;
    
procedure
    : 'procedure' Identifier '(' varlist? ')' ('uses' objlist)? ':' block 'end'
    ;
    
procedurecall
    : 'run' Identifier '(' explist? ')' ('with' varlist)?
    ;
    
objlist
    : objectdt 'as' Identifier (',' objectdt 'as' Identifier)*
    ;
    
attr
    : ('var')? Identifier '=' exp
    ;
    
varlist
    : Identifier (',' Identifier)*
    | objectdt (',' objectdt)*
    ;

retstat
    : 'return' exp? ';'?
    ;

explist
    : exp (',' exp)*
    ;
    
printexp
    : 'print' exp
    ;

exp
    : 'nil'                                #nilExp
    | numberdt                             #numberdtExp
    | booleandt                            #booleandtExp
    | stringdt                             #stringdtExp
    | positiondt                           #positiondtExp
    | objectdt                             #objectdtExp
    | listdt                               #listdtExp
    | procedurecall                        #procedurecallExp
    | Identifier                           #identifierExp
    | <assoc=right> exp operatorPower exp  #operatorPowerExp
    | operatorUnary exp                    #operatorUnaryExp
    | exp operatorMulDivMod exp            #operatorMulDivModExp
    | exp operatorAddSub exp               #operatorAddSubExp
    | <assoc=right> exp operatorStrcat exp #operatorStrcatExp
    | exp operatorComparison exp           #operatorComparisonExp
    | exp operatorAnd exp                  #operatorAndExp
    | exp operatorOr exp                   #operatorOrExp
    ;

operatorOr 
	: 'or';

operatorAnd 
	: 'and';

operatorComparison 
	: '<' | '>' | '<=' | '>=' | '!=' | '==';

operatorStrcat
	: '..';

operatorAddSub
	: '+' | '-';

operatorMulDivMod
	: '*' | '/' | '%';

operatorUnary
    : '!';

operatorPower
    : '^';

// Data types
    
numberdt
    : INT | HEX | FLOAT | HEX_FLOAT
    ;

booleandt
    : 'true'
    | 'false'
    ;

stringdt
    : NORMALSTRING | CHARSTRING | LONGSTRING
    ;
    
positiondt
    : '[' Identifier '~' Identifier ']'     #positiondtRangeIdentifier
    | '[' INT '~' INT ']'                   #positiondtRangeNumber
    | '[' Identifier ']'                    #positiondtIdentifier
    | '[' INT ']'                           #positiondtNumber
    | Identifier '[' Identifier ']' '[' Identifier ']' #positiondtArrayIdentifierIdentifier
    | Identifier '[' Identifier ']' '[' numberdt ']'   #positiondtArrayIdentifierNumber
    ;
    
objectdt
    : 'Sample' ('[' INT ']')?
    | 'Plate' ('[' INT ']')?
    | 'Rack' ('[' INT ']')?
    ;

listdt
    : '{' explist? '}'
    ;

// LEXER RULES

Identifier
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;
    
NORMALSTRING
    : '"' ( EscapeSequence | ~('\\'|'"') )* '"' 
    ;

CHARSTRING
    : '\'' ( EscapeSequence | ~('\''|'\\') )* '\''
    ;

LONGSTRING
    : '[' NESTED_STR ']'
    ;

fragment
NESTED_STR
    : '=' NESTED_STR '='
    | '[' .*? ']'
    ;

INT
    : Digit+
    ;

HEX
    : '0' [xX] HexDigit+
    ;

FLOAT
    : Digit+ '.' Digit* ExponentPart?
    | '.' Digit+ ExponentPart?
    | Digit+ ExponentPart
    ;

HEX_FLOAT
    : '0' [xX] HexDigit+ '.' HexDigit* HexExponentPart?
    | '0' [xX] '.' HexDigit+ HexExponentPart?
    | '0' [xX] HexDigit+ HexExponentPart
    ;

fragment
ExponentPart
    : [eE] [+-]? Digit+
    ;

fragment
HexExponentPart
    : [pP] [+-]? Digit+
    ;

fragment
EscapeSequence
    : '\\' [abfnrtvz"'\\]
    | '\\' '\r'? '\n'
    | DecimalEscape
    | HexEscape
    | UtfEscape
    ;
    
fragment
DecimalEscape
    : '\\' Digit
    | '\\' Digit Digit
    | '\\' [0-2] Digit Digit
    ;
    
fragment
HexEscape
    : '\\' 'x' HexDigit HexDigit
    ;

fragment
UtfEscape
    : '\\' 'u{' HexDigit+ '}'
    ;

fragment
Digit
    : [0-9]
    ;

fragment
HexDigit
    : [0-9a-fA-F]
    ;

COMMENT
    : '--[' NESTED_STR ']' -> channel(HIDDEN)
    ;
    
LINE_COMMENT
    : '--'
    (                                               // --
    | '[' '='*                                      // --[==
    | '[' '='* ~('='|'['|'\r'|'\n') ~('\r'|'\n')*   // --[==AA
    | ~('['|'\r'|'\n') ~('\r'|'\n')*                // --AAA
    ) ('\r\n'|'\r'|'\n'|EOF)
    -> channel(HIDDEN)
    ;
    
WS  
    : [ \t\u000C\r\n]+ -> skip
    ;

SHEBANG
    : '#' '!' ~('\n'|'\r')* -> channel(HIDDEN)
    ;
