# Diretórios
SRC_DIR = src
BIN_DIR = bin
MODEL_DIR = $(SRC_DIR)/model
COMP_DIR = $(SRC_DIR)/comparators

# Comandos
JAVAC = javac
JAVA = java -Xmx4g
JAR = jar
RM = rm -rf
MKDIR = mkdir -p

# Nome do arquivo JAR
JAR_NAME = vereadores.jar

# Arquivos fonte
SOURCES = $(wildcard $(SRC_DIR)/*.java) $(wildcard $(MODEL_DIR)/*.java) $(wildcard $(COMP_DIR)/*.java)

# Manifest file
MANIFEST = $(SRC_DIR)/MANIFEST.MF

.PHONY: all clean run jar

all: jar

$(BIN_DIR):
	$(MKDIR) $(BIN_DIR)

compile: $(BIN_DIR)
	@$(JAVAC) -Xlint -d $(BIN_DIR) $(SOURCES)

$(MANIFEST):
	@echo "Main-Class: Main" > $(MANIFEST)

jar: compile $(MANIFEST)
	@$(JAR) cfm $(JAR_NAME) $(MANIFEST) -C $(BIN_DIR) .

clean:
	$(RM) $(BIN_DIR)
	$(RM) $(JAR_NAME)
	$(RM) $(MANIFEST)

run: jar
	@$(JAVA) -jar $(JAR_NAME) $(ARGS)

# Example usage: make run ARGS="13 candidatos.csv votacao.csv 15/11/2020"

## consertando ##

# make run ARGS="1473 ../testes/AC1473/in/candidatos.csv ../testes/AC1473/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/AC1473/out/output.txt

## tudo ok ##
# make run ARGS="1392 ../testes/AC1392/in/candidatos.csv ../testes/AC1392/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/AC1392/out/output.txt

# make run ARGS="57053 ../testes/ES57053/in/candidatos.csv ../testes/ES57053/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/ES57053/out/output.txt 

# make run ARGS="56472 ../testes/ES56472/in/candidatos.csv ../testes/ES56472/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/ES56472/out/output.txt

# make run ARGS="25810 ../testes/PE25810/in/candidatos.csv ../testes/PE25810/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/PE25810/out/output.txt

# make run ARGS="26298 ../testes/PE26298/in/candidatos.csv ../testes/PE26298/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/PE26298/out/output.txt

# make run ARGS="26310 ../testes/PE26310/in/candidatos.csv ../testes/PE26310/in/votacao.csv 06/10/2024" > myoutput.txt
# diff myoutput.txt ../testes/PE26310/out/output.txt