# FCL Compiler

Este projeto é um compilador para a linguagem FCL definida no arquivo doc/fcl.pdf.

## Compilando o projeto

O compilador FCL utiliza o ANTLR4 para gerar um parser top-down no padrão de design Visitor.

### Instalando o ANTLR4

O Ubuntu possui o ANTLR4 no repositório padrão. Para instalar, basta executar:

```
sudo apt-get install antlr4
```
Verifique se a versão instalada é a 4.5.1. Caso contrário, você deverá
baixar e substituir a biblioteca lib/antlr-4.5.1-complete.jar, utilizada
na compilação do software, pela versão correta.

### Gerando o parser

Observação: só é necessário gerar o parser caso o arquivo grammar/FCL.g4 seja modificado.
O parser para a presente versão da gramática já está gerado nos sources.

O ANTLR4 por padrão gera um parser com uma interface no padrão Listener
a ser implementada para a geração de código. Como neste projeto utilizamos
o padrão Visitor, é necessário indicar ao ANTLR4 que o parser a ser gerado
deve seguir este padrão. Para gerar o parser Visitor, utilize o seguinte comando:

```
antlr4 -no-listener -visitor -o src/com/fcl/compiler/antlr4/ grammar/FCL.g4
```

Os arquivos gerados no diretório src/com/fcl/compiler/antlr4 devem ser:

```
FCLBaseListener.java
FCLBaseVisitor.java
FCLLexer.java
FCLParser.java
FCLVisitor.java
FCL.tokens
FCLLexer.tokens
```

Onde FCLVisitor.java é a interface a ser implementada no arquivo src/com/fcl/compiler/FCLVisitorImpl.java.

### Incluindo os arquivos no package

Os arquivos do parser são gerados dentro do pacote com.fcl.compiler.antlr4.
No entanto, resta a declaração de package dentro de cada arquivo:

```
package com.fcl.compiler.antlr4;
```

Feito isso, abra o projeto no Eclipse e execute.


