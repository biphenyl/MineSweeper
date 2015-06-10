all:
	mkdir -p bin
	javac -d bin -cp src src/*.java

run:
	java -cp bin MineSweeper

clean:
	rm -r bin


