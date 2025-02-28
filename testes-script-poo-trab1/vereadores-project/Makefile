SRC_DIR = src
BIN_DIR = bin
MODEL_DIR = $(SRC_DIR)/model
COMP_DIR = $(SRC_DIR)/comparator

JAVAC = javac
JAVA = java
JAR = jar
RM = rm -rf
MKDIR = mkdir -p

JAR_NAME = vereadores.jar

SOURCES = $(wildcard $(SRC_DIR)/*.java) $(wildcard $(MODEL_DIR)/*.java) $(wildcard $(COMP_DIR)/*.java)

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

# Exemplo de uso: make run ARGS="57053 consulta_cand_2024_ES.csv votacao_secao_2024_ES.csv 06/10/2024"
run: jar
	@$(JAVA) -jar $(JAR_NAME) $(ARGS)