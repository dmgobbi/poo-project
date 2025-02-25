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
	@$(JAVAC) -d $(BIN_DIR) $(SOURCES)

$(MANIFEST):
	@echo "Main-Class: Main" > $(MANIFEST)

jar: compile $(MANIFEST)
	@$(JAR) cfm $(JAR_NAME) $(MANIFEST) -C $(BIN_DIR) .

clean:
	$(RM) $(BIN_DIR)
	$(RM) $(JAR_NAME)
	$(RM) $(MANIFEST)

# Example usage: make run ARGS="13 candidatos.csv votacao.csv 15/11/2020"
# Another example: make run ARGS="57053 ../consulta_cand_2024/consulta_cand_2024_ES.csv ../votacao_secao_2024_SIGLA-UF/votacao_secao_2024_ES.csv 06/10/2024" > output.txt
run: jar
	@$(JAVA) -jar $(JAR_NAME) $(ARGS)