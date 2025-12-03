package br.edu.ifsp.biblioteca.factory;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class EmprestimoValidationChainFactory {
	public static ValidationHandler<EmprestimoCreateDto> createEmprestimoChain() {
		ExemplarHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
		CpfHandler<EmprestimoCreateDto> handleFormatCpf = new CpfHandler<>(EmprestimoCreateDto::getCpf);
		SequenceCpfHandler<EmprestimoCreateDto> handleSequenceCpf = new SequenceCpfHandler<>(
				EmprestimoCreateDto::getCpf);
		handleExemplar.setNext(handleFormatCpf);
		handleFormatCpf.setNext(handleSequenceCpf);
		return handleExemplar;
	}
}
