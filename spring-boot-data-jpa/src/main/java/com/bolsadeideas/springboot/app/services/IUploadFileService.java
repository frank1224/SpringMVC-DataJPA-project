package com.bolsadeideas.springboot.app.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	public Resource load(String filename) throws MalformedURLException;

	/*
	 * Devuelve un string con el nombre cambiado de la imagen, como nombre unico.
	 */
	public String copy(MultipartFile file) throws IOException;

	public boolean delete(String filename);
	
	/*
	 * elimina todo el directorio de manera recursiva.
	 */
	public void deleteAllUpload();
	
	/*
	 * Crea el directorio upload.
	 */
	public void initDirectoryUpload() throws IOException;

}
