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
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

$(MANIFEST):
	echo "Main-Class: App" > $(MANIFEST)

jar: compile $(MANIFEST)
	$(JAR) cvfm $(JAR_NAME) $(MANIFEST) -C $(BIN_DIR) .

clean:
	$(RM) $(BIN_DIR)
	$(RM) $(JAR_NAME)
	$(RM) $(MANIFEST)

# Example usage: make run ARGS="13 candidatos.csv votacao.csv 15/11/2020"
run: jar
	$(JAVA) -Xmx4g -jar $(JAR_NAME) $(ARGS)