package com.exam.competitor.admin.export;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

public class FileUploadUtil {
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		
		Path uploadPath = Paths.get(uploadDir);
		
		if (!(Files.exists(uploadPath))) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not able to save file : "+fileName);
		}
	}
	
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file ->{
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.out.println("Could not able to delet file:0"+file);
					}
				}
				
			});
		} catch (IOException e) {
			System.out.println("Could not list directory : "+dirPath);
		}
	}


	public static void removeDir(String categoryDir) {
		cleanDir(categoryDir);
		
		try {
			Files.delete(Paths.get(categoryDir));
		} catch (IOException e) {
			System.out.println("Could not remove directory : "+categoryDir);
		}
	}

}
	