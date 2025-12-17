package br.edu.ifsp.biblioteca.handler;

public abstract class BaseHandler<T> implements ValidationHandler<T> {
	private ValidationHandler<T> nextHandler;
	
	public void setNext(ValidationHandler<T> handler) {
		this.nextHandler = handler;
	}
	
	public ValidationHandler<T> getNext() {
		return this.nextHandler;
	}
	
	public void handle(T data) {
		if (nextHandler != null) {
			nextHandler.handle(data);
		}
	}
}
