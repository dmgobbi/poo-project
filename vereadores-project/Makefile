# Diretórios
SRC_DIR = src
BIN_DIR = bin
MODEL_DIR = $(SRC_DIR)/model
COMP_DIR = $(SRC_DIR)/comparators

# Comandos
JAVAC = javac
JAVA = java
RM = rm -rf
MKDIR = mkdir -p

# Arquivos fonte
SOURCES = $(wildcard $(MODEL_DIR)/*.java) $(wildcard $(COMP_DIR)/*.java)

# Alvos
.PHONY: all clean run

all: $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

$(BIN_DIR):
	$(MKDIR) $(BIN_DIR)

clean:
	$(RM) $(BIN_DIR)

run: all
	$(JAVA) -cp $(BIN_DIR) model.Teste