package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

final public class ResourceLoader {
	
	public static FileInputStream load(String path) {
		FileInputStream input = (FileInputStream) ResourceLoader.class.getResourceAsStream(path);
		if(input == null) {
			input = (FileInputStream) ResourceLoader.class.getResourceAsStream("/" +path);
		}
		return input;
	}
}
