package br.com.caelum.ingresso.model.descontos;

import java.math.BigDecimal;

public interface Desconto {
	
	BigDecimal aplicarDecontroSobre(BigDecimal precoOriginal);
	
	String getDescricao();
}
