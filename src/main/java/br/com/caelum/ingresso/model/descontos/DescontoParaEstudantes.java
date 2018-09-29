package br.com.caelum.ingresso.model.descontos;

import java.math.BigDecimal;

public class DescontoParaEstudantes implements Desconto{

	@Override
	public BigDecimal aplicarDecontroSobre(BigDecimal precoOriginal) {
		// TODO Auto-generated method stub
		return precoOriginal.divide(new BigDecimal("2.0"));
	}

	@Override
	public String getDescricao() {
		// TODO Auto-generated method stub
		return "Desconto Estudantes";
	}
	
	

}
