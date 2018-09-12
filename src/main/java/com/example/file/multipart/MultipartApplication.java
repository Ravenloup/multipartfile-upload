package com.example.file.multipart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@Controller
@CrossOrigin("*")
public class MultipartApplication implements CommandLineRunner {

	private final Path rootLocation = Paths.get("C:\\code-drive\\upload-dir");

	public static void main(String[] args) {
		SpringApplication.run(MultipartApplication.class, args);
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String getHome() {
		return "home";
	}

	/**
	 * Multipart mapping should be alwasy* POST.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file) throws Exception {
		System.out.println("file" + file + " : " + file.getBytes());
		this.store(file);
		return "home";
	}

	private void store(MultipartFile file) throws Exception {
		try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
		} catch (Exception exception) {
			throw new Exception("failed to store file!", exception.getCause());
		}
	}

	private void init() throws Exception {
		try {
			if (!Files.isDirectory(this.rootLocation)) {
				Files.createDirectory(this.rootLocation);
			}
		} catch (IOException exception) {
			throw new Exception("failed to create file file directory", exception.getCause());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		this.init();
	}
}
