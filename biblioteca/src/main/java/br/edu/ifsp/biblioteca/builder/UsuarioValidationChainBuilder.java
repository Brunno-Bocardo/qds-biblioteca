package br.edu.ifsp.biblioteca.builder;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.EmailHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.UsuarioHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class UsuarioValidationChainBuilder {
	public ValidationHandler<UsuarioCreateDto> buildUsuarioChain() {
		UsuarioHandler handleAtributos = new UsuarioHandler();
		CpfHandler<UsuarioCreateDto> handleFormatCpf = new CpfHandler<>(UsuarioCreateDto::getCpf);
		SequenceCpfHandler<UsuarioCreateDto> handleSequenceCpf = new SequenceCpfHandler<>(UsuarioCreateDto::getCpf);
		EmailHandler<UsuarioCreateDto> handleEmail = new EmailHandler<>(UsuarioCreateDto::getEmail);
		
		handleAtributos.setNext(handleFormatCpf);
		handleFormatCpf.setNext(handleSequenceCpf);
		handleSequenceCpf.setNext(handleEmail);
		
		return handleAtributos;
	}
	
	public ValidationHandler<String> createCpfChain() {
		CpfHandler<String> handleCpf = new CpfHandler<>(s -> s);
		SequenceCpfHandler<String> handleSequenceCpf = new SequenceCpfHandler<>(s -> s);

		handleCpf.setNext(handleSequenceCpf);
		return handleCpf;
	}
}
