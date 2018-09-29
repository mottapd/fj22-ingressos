package br.com.caelum.ingresso.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.ImagemCapa;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.form.SessaoForm;
import br.com.caelum.ingresso.rest.ImdbClient;
import br.com.caelum.ingresso.validation.GerenciadorDeSessao;

@Controller
public class SessaoController {
	@Autowired
    private SalaDao salaDao;
	@Autowired
    private FilmeDao filmeDao;
	@Autowired
	private SessaoDao sessaoDao;
	@Autowired
    private ImdbClient client;
	
	@GetMapping("/admin/sessao")
	public ModelAndView form(@RequestParam("salaId") Integer salaId, SessaoForm form){
		
		Sala sala = salaDao.findOne(salaId);
		List<Filme> filmes = filmeDao.findAll();
		
		ModelAndView view = new ModelAndView("sessao/sessao");

        view.addObject("sela", sala);
        view.addObject("filmes", filmes);
        form.setSalaId(salaId);
        view.addObject("form", form);
        
        return view;
	}
	
	@PostMapping(value = "/admin/sessao")
	@Transactional
	public ModelAndView salva(@Valid SessaoForm form, BindingResult result){
		if (result.hasErrors()) return form(form.getSalaId(), form);
		
		Sessao sessao = form.toSessao(salaDao, filmeDao);
		
		List<Sessao> sessaoDaSala = sessaoDao.buscaSessoesDaSala(sessao.getSala());
		
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessaoDaSala);
		if (gerenciador.cabe(sessao)) {
			sessaoDao.save(sessao);
			return new ModelAndView ("redirect:/admin/sala/" + form.getSalaId() + "/sessoes");
		}
		
		return form(form.getSalaId(), form);
		
	}
	
	@GetMapping("/sessao/{id}/lugares")
	public ModelAndView lugaresNaSessao(@PathVariable("id") Integer sessaoId) {
		ModelAndView modelAndView = new ModelAndView("sessao/lugares");
		Sessao sessao = sessaoDao.findOne(sessaoId);
		
		Optional<ImagemCapa> imagemCapa = client.request(sessao.getFilme(), ImagemCapa.class);
		
		modelAndView.addObject("sessao", sessao);
		modelAndView.addObject("imagemCapa", imagemCapa.orElse(new ImagemCapa()));
		
		return modelAndView;
	}
}
