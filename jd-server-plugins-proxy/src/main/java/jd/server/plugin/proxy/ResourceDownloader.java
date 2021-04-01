package jd.server.plugin.proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ResourceDownloader {
	
	protected static void download(InputStream is,File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file) ;
		IOUtils.copy(is, fos);
		IOUtils.closeQuietly(is,fos);
		System.out.println("download:" + file.getPath());
	}
	
/*	public void run(InputStream is,File file) {

	}*/
}
