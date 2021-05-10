package com.bolsadeideas.springboot.app.util.pagination;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPages;
	private int numElementForPage;
	private int currentPage;
	
	private List<PageItem> pagesItem;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pagesItem =  new ArrayList<PageItem>();
		
		numElementForPage = page.getSize(); // size contiene  una cantidad de elementos por pagina.
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1; //optenemos la pagina actual que por defecto es 0 y la aumentamos a uno.
		
		int from; int until; 
		
		if (totalPages <= numElementForPage) {
			from = 1;
			until = totalPages;
		} else {
			if (currentPage <= numElementForPage/2) { //indica la mitad de elementos por pagina.
				from = 1;
				until = numElementForPage; // rango desde que pagina a cual se mostrará dependiendo la cantidad de elmentos ej: de la pagina 1 - 6 de la 6 - 12 etc..
			
			} else if(currentPage >= totalPages - numElementForPage/2){ //calculamos el rango final.
				from = totalPages - numElementForPage + 1; // indicamos desde la pagina actual hasta la final que se va a mostrar.
				until = numElementForPage;
			
			} else { // si esta en rango intermedio	
				from = currentPage - numElementForPage/2;
				until = numElementForPage;
			}
		}
		
		for(int  i = 0; i < until; i++ ) {
			pagesItem.add(new PageItem(from + i, currentPage == from + 1)); // paginas que se van a mostrar en el paginador.
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPagesItem() {
		return pagesItem;
	}

	//Añadimos metodos para la paginación
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
	
	
}
