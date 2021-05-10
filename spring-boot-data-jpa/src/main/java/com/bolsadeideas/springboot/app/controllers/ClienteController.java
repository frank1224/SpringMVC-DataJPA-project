package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;


import javax.validation.Valid;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.util.pagination.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	//@Qualifier("clienteServiceJPA")
	//private IClienteDao clienteDao;
	@Qualifier("clienteServiceJPA")
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	

	@GetMapping("/ver-detalle/{id}")
	public String ver(@PathVariable(value="id") Long id, Model model, RedirectAttributes flashMessenger) {
	
		Cliente cliente = clienteService.findOne(id);
		
		if (cliente==null) {
			flashMessenger.addAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.addAttribute("titulo", "Detalle cliente: " + cliente.getName());
		model.addAttribute("cliente", cliente);
		
		
		return "/ver-detalle";
	}
	
	//@RequestMapping(name = "listar",method = RequestMethod.GET)
	@GetMapping("/listar")
	public String listar(@RequestParam(value="page", defaultValue="0")int page, Model model) {
		Pageable pageRequest =  PageRequest.of(page, 4); //se indica 4 registros o elmentos por pagina.
														// defaultValue="0" indica la pagina desde donde empieza
		Page<Cliente> cliente = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", cliente);
		
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", cliente);
		model.addAttribute("page", pageRender);
		
		return "/listar";
		
	}
	
	
	@GetMapping("/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Formulario de Cliente");
		model.addAttribute("cliente", cliente);
		
		return "/form";
		
	}
	
	/*@GetMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario de Cliente");
		model.put("cliente", cliente);
		
		return "form";
		
	}*/
	
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,@RequestParam("file") MultipartFile file, RedirectAttributes flashMessenger, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if (!file.isEmpty()) {
			//Path resourcesDirectory = Paths.get("src//main//resources//static/uploads"); //ruta donde se guardaran la imagenes
			//String rootPath = resourcesDirectory.toFile().getAbsolutePath();
			String uniqueFile = UUID.randomUUID().toString().toString() + "_" + file.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve( uniqueFile); //ruta absoluta
			
			Path rootAbsolutebPath = rootPath.toAbsolutePath(); // agregamos la ruta absoluta
			
			log.info("rootAbPath: "+ rootPath); //path relativo
			log.info("rootAbsolutebPath: "+ rootAbsolutebPath);// path absoluto
			
			try {
				byte[] bytes = file.getBytes();
				//FORMA ALTERNATIVA
				Files.copy(file.getInputStream(), rootAbsolutebPath);	
				/*
				Path pathComplete = Paths.get(rootPath + "//" + file.getOriginalFilename());
				Files.write(pathComplete, bytes); //crea y escribe la imagen en el directorio 
				*/
				flashMessenger.addFlashAttribute("info", "Has subido correctamente '" + uniqueFile + "'");
				
				cliente.setPhoto(uniqueFile);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito!!!" : "Cliente creado con exito";
		
		clienteService.save(cliente);
		status.setComplete();
		flashMessenger.addFlashAttribute("success", mensajeFlash );
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Model model, RedirectAttributes flashMessenger) { //usamos @PatchVariable para que inyecte el valor del parametro de la ruta
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente==null) {
				flashMessenger.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
				return "redirect:/listar";
			}
		}else {
			flashMessenger.addFlashAttribute("error", "El ID del cleinte no puede ser cero!!!");
			return "redirect:/listar";
		}	
		model.addAttribute("titulo", "Formulario de Cliente");
		model.addAttribute("cliente", cliente);	
		
		return "/form";
		
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flashMessenger) {	
		if (id > 0) {
			clienteService.delete(id);
			flashMessenger.addFlashAttribute("succes", "Cliente eliminado con éxito!!!");
		}
		return "redirect:/listar";
	}
	

	
}
