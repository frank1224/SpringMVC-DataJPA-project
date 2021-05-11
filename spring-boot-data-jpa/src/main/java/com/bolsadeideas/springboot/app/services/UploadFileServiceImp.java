package com.bolsadeideas.springboot.app.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("UploadService")
public class UploadFileServiceImp implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOAD_FOLDER = "uploads";

	public UploadFileServiceImp() {
	}

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathImage = Paths.get(filename);

		log.info("pathImage: " + pathImage);
		Resource resource = null; // org.springframework.core.io.UrlResource

		resource = new UrlResource(pathImage.toUri());
		if (!resource.exists() && !resource.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathImage);
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = Paths.get(uniqueFile); // ruta absoluta

		log.info("rootPath: " + rootPath);

		//byte[] bytes = file.getBytes();
		Files.copy(file.getInputStream(), rootPath);

		return uniqueFile;
	}

	@Override
	public boolean delete(String filename) {

		Path rootPath = Paths.get(filename);
		File archive = rootPath.toFile();

		if (archive.exists() && archive.canRead()) {
			if (archive.delete()) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Recibe el nombre del archivo y en un path absoluto
	 */
	public Path getPath(String filename) { 
		
		return Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAllUpload() {

		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());
	}

	@Override
	public void initDirectoryUpload() throws IOException {
		
		Files.createDirectory(Paths.get(UPLOAD_FOLDER));
		
	}

}
