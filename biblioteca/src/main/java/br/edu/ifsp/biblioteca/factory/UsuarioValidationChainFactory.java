package br.edu.ifsp.biblioteca.factory;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.EmailHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.UsuarioHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class UsuarioValidationChainFactory {
	public static ValidationHandler<UsuarioCreateDto> createUsuarioChain() {
		UsuarioHandler handleAtributos = new UsuarioHandler();
		CpfHandler<UsuarioCreateDto> handleFormatCpf = new CpfHandler<>(UsuarioCreateDto::getCpf);
		SequenceCpfHandler<UsuarioCreateDto> handleSequenceCpf = new SequenceCpfHandler<>(UsuarioCreateDto::getCpf);
		EmailHandler<UsuarioCreateDto> handleEmail = new EmailHandler<>(UsuarioCreateDto::getEmail);
		handleAtributos.setNext(handleFormatCpf);
		handleFormatCpf.setNext(handleSequenceCpf);
		handleSequenceCpf.setNext(handleEmail);
		return handleAtributos;
	}
}
