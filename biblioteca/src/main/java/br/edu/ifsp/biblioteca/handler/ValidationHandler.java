package br.edu.ifsp.biblioteca.handler;

public interface ValidationHandler<T> {
	void setNext(ValidationHandler<T> handler);
	void handle(T data);
}
