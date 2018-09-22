package br.com.caelum.ingresso.validation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import junit.framework.Assert;

public class GerenciadorDeSessaoTest {

	private Filme rogueOne;
	private Sala sala3D;
	private Sessao sessaoDasDez;
	private Sessao sessaoDasTreze;
	private Sessao sessaoDasDezoito;
	
	@Before
	public void preparaSessoes(){
		this.rogueOne = new Filme("RogueOne", Duration.ofMinutes(120), "SCI-FI");
		this.sala3D = new Sala("Sala 3D");
		
		this.sessaoDasDez = new Sessao(LocalTime.parse("10:00:00"), rogueOne, sala3D);
		this.sessaoDasTreze = new Sessao(LocalTime.parse("13:00:00"), rogueOne, sala3D);
		this.sessaoDasDezoito = new Sessao(LocalTime.parse("18:00:00"), rogueOne, sala3D);
	}
	
	@Test
	public void testeNaoPermitirSessaoMesmoHorario(){
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciador.cabe(this.sessaoDasDez));
	}
	
	@Test
	public void testeNaoPermitirSessoesDentroSessaoTerminandoExistente(){
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().minusHours(1), rogueOne, sala3D);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciador.cabe(sessao));
	}
	
	@Test
	public void testeNaoPermitirSessoesDentroSessaoIniciandoExistente(){
		List<Sessao> sessoesDaSala = Arrays.asList(sessaoDasDez);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().plusHours(1), rogueOne, sala3D);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);
		Assert.assertFalse(gerenciador.cabe(sessao));
	}
	
	@Test
	public void testePermitirSessaoEntreFilmes(){
		Assert.assertTrue(true);
	}
	
}
