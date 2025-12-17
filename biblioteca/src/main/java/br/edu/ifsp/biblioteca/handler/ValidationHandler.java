package br.edu.ifsp.biblioteca.handler;

public interface ValidationHandler<T> {
	void setNext(ValidationHandler<T> handler);
	ValidationHandler<T> getNext();
	void handle(T data);
}
