# Netflix BST Analysis

Projeto da disciplina de Estruturas de Dados da Faculdade de Computação e Informática da Universidade Presbiteriana Mackenzie.

O programa le dados de filmes e series da Netflix e armazena os registros em uma arvore de busca binaria (BST). A chave usada na arvore e o id de cada titulo.

## Integrantes

- Danilo Oliveira Santos - RA 10410905
- Miguel Batista da Silva - RA 10736250

## O que o programa faz

- Le o arquivo titles.csv e monta a arvore BST;
- descarta registros com atributos vazios;
- insere, busca e remove programas;
- mostra a altura da arvore;
- executa cinco analises sobre os filmes e series;
- salva os dados atuais em um novo arquivo CSV.

## Requisitos

- JDK 23 ou superior;
- terminal ou uma IDE Java.

## Como executar

Na raiz do projeto, compile os arquivos.

Windows:

    javac -encoding UTF-8 -d out src\*.java
    java -cp out Main

Linux ou macOS:

    javac -encoding UTF-8 -d out src/*.java
    java -cp out Main

Quando o programa solicitar o arquivo CSV, informe data/titles.csv.

## Menu

1. Ler dados de arquivo
2. Executar analises
3. Inserir programa
4. Buscar programa
5. Remover programa
6. Exibir altura da arvore
7. Salvar dados em arquivo
8. Encerrar a aplicacao

Nas analises que recebem uma quantidade de resultados, o valor minimo aceito e 6.

## Dataset

O arquivo utilizado e o titles.csv, obtido do dataset publico de filmes e series da Netflix. O arquivo possui 15 atributos, como titulo, tipo, ano de lancamento, classificacao etaria, duracao, notas do IMDb e notas do TMDB.

O arquivo credits.csv nao e utilizado neste projeto, conforme especificado no enunciado.

## Estrutura do projeto

    .
    ├── README.md
    ├── .gitignore
    ├── data/
    │   └── titles.csv
    ├── docs/
    │   └── enunciado-projeto-2.pdf
    └── src/
        ├── BST.java
        ├── Main.java
        ├── NoBST.java
        └── ProgramaNetFlix.java
