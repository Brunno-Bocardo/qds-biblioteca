package br.edu.ifsp.biblioteca.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.service.EmprestimoService;


@WebMvcTest(EmprestimoController.class)
@AutoConfigureMockMvc(addFilters = false)

public class EmprestimoControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@SuppressWarnings("removal")
	@MockBean
    private EmprestimoService emprestimoService;
	
	@Autowired
    private ObjectMapper objectMapper;
	 
	 @Test
	 void deveRegistrarEmprestimo() throws Exception {

	     EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
	             .cpf("12345")
	             .codigoExemplar("12345")
	             .build();

	     Emprestimo emprestimo = new Emprestimo();

	     when(emprestimoService.registrarEmprestimo(any(EmprestimoCreateDto.class)))
	             .thenReturn(emprestimo);

	     mockMvc.perform(post("/library/emprestimo")
	             .contentType(MediaType.APPLICATION_JSON)
	             .content(objectMapper.writeValueAsString(dto)))
	             .andExpect(status().isOk())
	             .andExpect(jsonPath("$").exists());
	 }
	 
	 @Test
	 void deveRetornarListaDeEmprestimos() throws Exception {
	     Emprestimo emprestimo = new Emprestimo();
	     List<Emprestimo> lista = List.of(emprestimo);

	     when(emprestimoService.listarEmprestimos())
	             .thenReturn(lista);

	     mockMvc.perform(get("/library/emprestimo"))
	             .andExpect(status().isOk())
	             .andExpect(jsonPath("$.length()").value(1));
	 }
	 
	 @Test
	 void deveRegistrarDevolucao() throws Exception {
		 Emprestimo emprestimo = new Emprestimo();

		    when(emprestimoService.registrarDevolucaoEmprestimo(1))
		            .thenReturn(emprestimo);

		    mockMvc.perform(put("/library/emprestimo/1/devolucao"))
		            .andExpect(status().isOk());
	 }

}
